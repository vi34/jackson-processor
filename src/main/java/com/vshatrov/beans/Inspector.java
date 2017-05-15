package com.vshatrov.beans;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.vshatrov.GenerationException;
import com.vshatrov.JacksonProcessor;
import com.vshatrov.annotations.AlternativeNames;
import com.vshatrov.annotations.OldProperty;
import com.vshatrov.beans.properties.Property;
import com.vshatrov.beans.properties.PropertyFabric;
import com.vshatrov.utils.Utils;

import javax.lang.model.element.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.vshatrov.JacksonProcessor.UnknownAnnotationsRule.FAIL;
import static com.vshatrov.utils.Utils.elementUtils;
import static com.vshatrov.utils.Utils.getAnnotationMirror;
import static com.vshatrov.utils.Utils.messager;
import static javax.lang.model.type.TypeKind.*;

/**
 * @author Viktor Shatrov.
 */
public class Inspector {
    public static Map<String, BeanDescription> beansCache;
    private static PropertyFabric fabric = new PropertyFabric();
    private List<VariableElement> fields;
    private List<Symbol.MethodSymbol> methods;

    public Inspector() {
        fields = new ArrayList<>();
        methods = new ArrayList<>();
    }

    public BeanDescription inspect(TypeElement element) {
        BeanDescription definition = null;
        try {
            checkAnnotationSupport(element);
            List<? extends Element> members = elementUtils.getAllMembers(element);
            definition = new BeanDescription(element);
            for (Element member: members) {
                if (member.getKind().equals(ElementKind.FIELD)) {
                    VariableElement field = (VariableElement) member;
                    fields.add(field);
                } else if (member.getKind().equals(ElementKind.METHOD)) {
                    methods.add((Symbol.MethodSymbol) member);
                }
            }
            for (VariableElement field : fields) {
                processField(definition, field);
            }

        } catch (Exception e) {
            Utils.warning(e, "Inspection of type element failed " + element.getSimpleName().toString()
                    + "\n" +e.getMessage());
        }
        beansCache.put(element.asType().toString(), definition);
        return definition;
    }

    private void checkAnnotationSupport(TypeElement element) throws GenerationException {
        List<? extends AnnotationMirror> annotations = elementUtils.getAllAnnotationMirrors(element);
        ArrayList<AnnotationMirror> wholeTreeAnnotations = new ArrayList<>();
        wholeTreeAnnotations.addAll(annotations);
        elementUtils.getAllMembers(element).stream()
                .forEach(m -> wholeTreeAnnotations.addAll(elementUtils.getAllAnnotationMirrors(m)));

        Optional<AnnotationMirror> unsupported = wholeTreeAnnotations.stream().filter(mirror ->
                mirror.getAnnotationType().toString().startsWith(JacksonProcessor.JACKSON_ANNOTATIONS_PACKAGE)
                        && !JacksonProcessor.getSupportedJacksonAnnotations()
                        .contains(mirror.getAnnotationType().toString())

        ).findAny();

        if (unsupported.isPresent() && FAIL.equals(JacksonProcessor.annotationsRule)) {
            throw new GenerationException("Annotation " + unsupported.get().getAnnotationType().toString()
                    + "is not supported.\n Generation skipped for " + element.getSimpleName());
        }
    }

    public static Optional<BeanDescription> getDescription(String typeName) {
        if (beansCache.containsKey(typeName)) {
            return Optional.ofNullable(beansCache.get(typeName));
        }

        TypeElement typeElement = elementUtils.getTypeElement(typeName);
        if (typeElement == null) return Optional.empty();
        return Optional.ofNullable(new Inspector().inspect(typeElement));
    }

    public void addForInspection(TypeElement element) {
        String typeName = element.asType().toString();
        if (!beansCache.containsKey(typeName)) {
            inspect(element);
        }
    }

    private void processField(BeanDescription definition, VariableElement member) {
        Symbol.MethodSymbol getter = findGetter(member);
        Symbol.MethodSymbol setter = findSetter(member);
        Property property;

        if (fieldFilter(member, getter, setter)) {
            property = fabric.construct(member);
            definition.getProps().add(property);
            if (getter != null && setter != null) {
                property.setGetter(getter.getSimpleName().toString());
                property.setSetter(setter.getSimpleName().toString());
            }
            processFieldAnnotations(member, property);
        }
    }

    private void processFieldAnnotations(VariableElement member, Property property) {
        JsonProperty jsonProperty = member.getAnnotation(JsonProperty.class);
        if (jsonProperty != null) {
            property.setName(jsonProperty.value());
        }

        AlternativeNames alternativeNames = member.getAnnotation(AlternativeNames.class);
        if (alternativeNames != null) {
            property.setAlternativeNames(Arrays.asList(alternativeNames.value()));
        }

        OldProperty oldProperty = member.getAnnotation(OldProperty.class);
        if (oldProperty != null) {
            property.setOldProperty(oldProperty.value());
        }
    }


    /**
     * @return false if field must be excluded
     */
    private boolean fieldFilter(VariableElement member, Symbol.MethodSymbol getter, Symbol.MethodSymbol setter) {
        return !(member.getModifiers().contains(Modifier.TRANSIENT) || member.getModifiers().contains(Modifier.STATIC))
                && (member.getAnnotation(JsonIgnore.class) == null || !member.getAnnotation(JsonIgnore.class).value())
                && (!member.getModifiers().contains(Modifier.PRIVATE) || getter != null && setter != null);
    }

    private Symbol.MethodSymbol findGetter(VariableElement field) {
        Symbol.MethodSymbol getter = null;
        String name = field.getSimpleName().toString().toLowerCase();
        for (Symbol.MethodSymbol method : methods) {

            String mName = method.getSimpleName().toString().toLowerCase();
            if ((mName.startsWith("get") && mName.length() > 3 && name.equals(mName.substring(3)))
                    || (field.asType().getKind().equals(BOOLEAN) && mName.startsWith("is")
                        && mName.length() > 2 &&  name.equals(mName.substring(2)))
                    || method.getAnnotation(JsonProperty.class) != null
                        && method.getAnnotation(JsonProperty.class)
                                .value().equals(field.getSimpleName().toString())
                    ) {
                getter = method;
                break;
            }
        }
        return getter;
    }

    private Symbol.MethodSymbol findSetter(VariableElement field) {
        Symbol.MethodSymbol setter = null;
        String name = field.getSimpleName().toString().toLowerCase();
        for (Symbol.MethodSymbol method : methods) {
            String mName = method.getSimpleName().toString().toLowerCase();
            if (mName.startsWith("set") && mName.length() > 3 && name.equals(mName.substring(3))
                    || method.getAnnotation(JsonProperty.class) != null
                        && method.getAnnotation(JsonProperty.class)
                                    .value().equals(field.getSimpleName().toString())
                        && method.getReturnType().getKind().equals(VOID)
                        && method.getParameters().size() == 1
                        && method.getParameters().get(0).asType().equals(field.asType())
                    ) {
                setter = method;
                break;
            }
        }
        return setter;
    }

}
