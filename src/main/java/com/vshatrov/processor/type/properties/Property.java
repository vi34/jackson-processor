package com.vshatrov.processor.type.properties;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import com.sun.tools.javac.code.Type;
import com.vshatrov.processor.GenerationException;
import com.vshatrov.processor.schema.JsonType;
import com.vshatrov.processor.utils.Utils;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.vshatrov.processor.utils.Utils.isEnum;
import static com.vshatrov.processor.utils.Utils.isNumber;

/**
 * @author Viktor Shatrov.
 */
public class Property {

    private String name;    /** name of property which will be in json*/
    private String propertyName; /** actual property name in class, used only for access */
    String typeName;
    Function<String, String> dynamicAccessor;
    String getter;
    String setter;
    TypeKind typeKind;
    boolean isSimple;
    JsonType jsonType;
    TypeName tName;
    boolean isInterface;

    List<String> alternativeNames = new ArrayList<>();
    String oldProperty;


    /**
     * Type constructor used only when property is a type parameter of another property
     * e.g. Array element type, generic type parameter
     * */
    Property(TypeMirror type) {
        typeName = type.toString();
        name = Utils.qualifiedToSimple(typeName);
        tName = TypeName.get(type);
        jsonType = JsonType.OBJECT;
        try {
            typeKind = Utils.typeUtils.unboxedType(type).getKind();
        } catch (IllegalArgumentException e) {
            typeKind = type.getKind();
        }
        fillWithType(type);
    }


    Property(VariableElement element) {
        propertyName = element.getSimpleName().toString();
        name = propertyName;
        TypeMirror type = element.asType();
        typeName = type.toString();
        tName = TypeName.get(type);
        jsonType = JsonType.OBJECT;
        try {
            typeKind = Utils.typeUtils.unboxedType(type).getKind();
        } catch (IllegalArgumentException e) {
            typeKind = type.getKind();
        }
        fillWithType(type);
    }

    void fillWithType(TypeMirror type) {
        isSimple = computeSimple(type);
        isInterface = ((Type)type).isInterface();
        switch (getTypeName()) {
            case "boolean":case "java.lang.Boolean":
                jsonType = JsonType.BOOLEAN; break;
            case "char":case "java.lang.Character":case "java.lang.String":
                jsonType = JsonType.STRING; break;
            default:
                if (isNumber(type)) {
                    jsonType = JsonType.NUMBER;
                }
        }
    }

    private static boolean computeSimple(TypeMirror type) {
        return type.getKind().isPrimitive() || isNumber(type) || isEnum(type)
                || type.toString().equals("java.lang.String")
                || type.toString().equals("java.lang.Character")
                || type.toString().equals("java.lang.Boolean");
    }

    public String getAccessor(String var) {
        String res = var + "." + getter();
        if (dynamicAccessor != null) res = dynamicAccessor.apply(var);
        return res;
    }

    protected String modifyAccess(String accessor) {
        if (getTypeName().equals("char") || getTypeName().equals("java.lang.Character"))
            accessor += " + \"\"";
        return accessor;
    }

    public String getter() {
        if (getter == null) return getPropertyName();
        return getter + "()";
    }

    //TODO handle more types. Watch JsonGenerator._writeSimpleObject
    public String writeMethod(String varName) throws GenerationException {
        String accessor = modifyAccess(getAccessor(varName));

        switch (jsonType) {
            case NUMBER: return "writeNumber(" + accessor + ")";
            case STRING: return "writeString(" + accessor + ")";
            case BOOLEAN: return "writeBoolean(" + accessor + ")";
            default:
                throw new GenerationException("Couldn't find generator method for " + this);
        }
    }

    public CodeBlock defaultInstance() {
        return CodeBlock.builder()
                .add("new $T();\n$]", tName)
                .build();
    }

    public String parseMethod(String parser) throws GenerationException {
        switch (typeKind) {
            case BOOLEAN:
                return parser + ".getBooleanValue()";
            case INT:
                return parser + ".getIntValue()";
            case CHAR:
                return parser + ".getText().charAt(0)";
            case LONG:
                return parser + ".getLongValue()";
            case SHORT:
                return "_parseShort(" + parser + ", ctxt)";
            case DOUBLE:
                return "_parseDouble(" + parser + ", ctxt)";
            case FLOAT:
                return "_parseFloat(" + parser + ", ctxt)";
            case BYTE:
                return "_parseByte(" + parser + ", ctxt)";
            default:
                if (typeName.equals("java.lang.String")) {
                    return parser + ".getText()";
                }
                throw new GenerationException("Couldn't find generator method for " + this);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public Function<String, String> getDynamicAccessor() {
        return this.dynamicAccessor;
    }

    public String getGetter() {
        return this.getter;
    }

    public String getSetter() {
        return this.setter;
    }

    public TypeName getTName() {
        return this.tName;
    }

    public TypeKind getTypeKind() {
        return this.typeKind;
    }

    public boolean isSimple() {
        return this.isSimple;
    }

    public List<String> getAlternativeNames() {
        return this.alternativeNames;
    }

    public String getOldProperty() {
        return this.oldProperty;
    }

    public JsonType getJsonType() {
        return this.jsonType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setDynamicAccessor(Function<String, String> dynamicAccessor) {
        this.dynamicAccessor = dynamicAccessor;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public void setTName(TypeName tName) {
        this.tName = tName;
    }

    public void setTypeKind(TypeKind typeKind) {
        this.typeKind = typeKind;
    }

    public void setSimple(boolean isSimple) {
        this.isSimple = isSimple;
    }

    public void setAlternativeNames(List<String> alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public void setOldProperty(String oldProperty) {
        this.oldProperty = oldProperty;
    }

    public void setJsonType(JsonType jsonType) {
        this.jsonType = jsonType;
    }

    public boolean isInterface() {
        return isInterface;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        if (isSimple != property.isSimple) return false;
        if (isInterface != property.isInterface) return false;
        if (name != null ? !name.equals(property.name) : property.name != null) return false;
        if (propertyName != null ? !propertyName.equals(property.propertyName) : property.propertyName != null)
            return false;
        if (typeName != null ? !typeName.equals(property.typeName) : property.typeName != null) return false;
        if (dynamicAccessor != null ? !dynamicAccessor.equals(property.dynamicAccessor) : property.dynamicAccessor != null)
            return false;
        if (getter != null ? !getter.equals(property.getter) : property.getter != null) return false;
        if (setter != null ? !setter.equals(property.setter) : property.setter != null) return false;
        if (typeKind != property.typeKind) return false;
        if (jsonType != property.jsonType) return false;
        if (tName != null ? !tName.equals(property.tName) : property.tName != null) return false;
        if (alternativeNames != null ? !alternativeNames.equals(property.alternativeNames) : property.alternativeNames != null)
            return false;
        return oldProperty != null ? oldProperty.equals(property.oldProperty) : property.oldProperty == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (propertyName != null ? propertyName.hashCode() : 0);
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (dynamicAccessor != null ? dynamicAccessor.hashCode() : 0);
        result = 31 * result + (getter != null ? getter.hashCode() : 0);
        result = 31 * result + (setter != null ? setter.hashCode() : 0);
        result = 31 * result + (typeKind != null ? typeKind.hashCode() : 0);
        result = 31 * result + (isSimple ? 1 : 0);
        result = 31 * result + (jsonType != null ? jsonType.hashCode() : 0);
        result = 31 * result + (tName != null ? tName.hashCode() : 0);
        result = 31 * result + (isInterface ? 1 : 0);
        result = 31 * result + (alternativeNames != null ? alternativeNames.hashCode() : 0);
        result = 31 * result + (oldProperty != null ? oldProperty.hashCode() : 0);
        return result;
    }
}
