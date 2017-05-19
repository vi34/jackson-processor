package com.vshatrov.beans.properties;

import com.squareup.javapoet.TypeName;
import com.vshatrov.GenerationException;
import com.vshatrov.schema.JsonType;
import com.vshatrov.utils.Utils;

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
public class Property {

    private String name;    /** name of property which will be in json*/
    private String propertyName; /** actual property name in class, used only for access */
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Property)) return false;
        final Property other = (Property) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$propertyName = this.getPropertyName();
        final Object other$propertyName = other.getPropertyName();
        if (this$propertyName == null ? other$propertyName != null : !this$propertyName.equals(other$propertyName))
            return false;
        final Object this$typeName = this.getTypeName();
        final Object other$typeName = other.getTypeName();
        if (this$typeName == null ? other$typeName != null : !this$typeName.equals(other$typeName)) return false;
        final Object this$dynamicAccessor = this.getDynamicAccessor();
        final Object other$dynamicAccessor = other.getDynamicAccessor();
        if (this$dynamicAccessor == null ? other$dynamicAccessor != null : !this$dynamicAccessor.equals(other$dynamicAccessor))
            return false;
        final Object this$getter = this.getGetter();
        final Object other$getter = other.getGetter();
        if (this$getter == null ? other$getter != null : !this$getter.equals(other$getter)) return false;
        final Object this$setter = this.getSetter();
        final Object other$setter = other.getSetter();
        if (this$setter == null ? other$setter != null : !this$setter.equals(other$setter)) return false;
        final Object this$tName = this.getTName();
        final Object other$tName = other.getTName();
        if (this$tName == null ? other$tName != null : !this$tName.equals(other$tName)) return false;
        final Object this$typeKind = this.getTypeKind();
        final Object other$typeKind = other.getTypeKind();
        if (this$typeKind == null ? other$typeKind != null : !this$typeKind.equals(other$typeKind)) return false;
        if (this.isSimple() != other.isSimple()) return false;
        final Object this$alternativeNames = this.getAlternativeNames();
        final Object other$alternativeNames = other.getAlternativeNames();
        if (this$alternativeNames == null ? other$alternativeNames != null : !this$alternativeNames.equals(other$alternativeNames))
            return false;
        final Object this$oldProperty = this.getOldProperty();
        final Object other$oldProperty = other.getOldProperty();
        if (this$oldProperty == null ? other$oldProperty != null : !this$oldProperty.equals(other$oldProperty))
            return false;
        final Object this$jsonType = this.getJsonType();
        final Object other$jsonType = other.getJsonType();
        if (this$jsonType == null ? other$jsonType != null : !this$jsonType.equals(other$jsonType)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $propertyName = this.getPropertyName();
        result = result * PRIME + ($propertyName == null ? 43 : $propertyName.hashCode());
        final Object $typeName = this.getTypeName();
        result = result * PRIME + ($typeName == null ? 43 : $typeName.hashCode());
        final Object $dynamicAccessor = this.getDynamicAccessor();
        result = result * PRIME + ($dynamicAccessor == null ? 43 : $dynamicAccessor.hashCode());
        final Object $getter = this.getGetter();
        result = result * PRIME + ($getter == null ? 43 : $getter.hashCode());
        final Object $setter = this.getSetter();
        result = result * PRIME + ($setter == null ? 43 : $setter.hashCode());
        final Object $tName = this.getTName();
        result = result * PRIME + ($tName == null ? 43 : $tName.hashCode());
        final Object $typeKind = this.getTypeKind();
        result = result * PRIME + ($typeKind == null ? 43 : $typeKind.hashCode());
        result = result * PRIME + (this.isSimple() ? 79 : 97);
        final Object $alternativeNames = this.getAlternativeNames();
        result = result * PRIME + ($alternativeNames == null ? 43 : $alternativeNames.hashCode());
        final Object $oldProperty = this.getOldProperty();
        result = result * PRIME + ($oldProperty == null ? 43 : $oldProperty.hashCode());
        final Object $jsonType = this.getJsonType();
        result = result * PRIME + ($jsonType == null ? 43 : $jsonType.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Property;
    }

    public String toString() {
        return "com.vshatrov.beans.properties.Property(name=" + this.getName() + ", propertyName=" + this.getPropertyName() + ", typeName=" + this.getTypeName() + ", dynamicAccessor=" + this.getDynamicAccessor() + ", getter=" + this.getGetter() + ", setter=" + this.getSetter() + ", tName=" + this.getTName() + ", typeKind=" + this.getTypeKind() + ", isSimple=" + this.isSimple() + ", alternativeNames=" + this.getAlternativeNames() + ", oldProperty=" + this.getOldProperty() + ", jsonType=" + this.getJsonType() + ")";
    }
}
