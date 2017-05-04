package com.vshatrov.beans.properties;

import com.squareup.javapoet.TypeName;
import com.vshatrov.GenerationException;
import com.vshatrov.schema.JsonType;
import com.vshatrov.utils.Utils;
import lombok.*;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.vshatrov.utils.Utils.isEnum;
import static com.vshatrov.utils.Utils.isNumber;

/**
 * @author Viktor Shatrov.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Property {

    String name;    /** name of property which will be in json*/
    String propertyName; /** actual property name in class, used only for access */
    String typeName;
    Function<String, String> dynamicAccessor;
    String getter;
    String setter;
    TypeName tName;
    TypeKind typeKind;
    boolean isSimple;

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

    JsonType jsonType;


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
    public String genMethod(String varName) throws GenerationException {
        String accessor = modifyAccess(getAccessor(varName));

        switch (jsonType) {
            case NUMBER: return "writeNumber(" + accessor + ")";
            case STRING: return "writeString(" + accessor + ")";
            case BOOLEAN: return "writeBoolean(" + accessor + ")";
            default:
                throw new GenerationException("Couldn't find generator method for " + this);
        }
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
}
