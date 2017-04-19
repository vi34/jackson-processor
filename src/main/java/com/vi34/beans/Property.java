package com.vi34.beans;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.vi34.GenerationException;
import com.vi34.utils.Utils;
import lombok.*;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.function.Function;

/**
 * Created by vi34 on 03/04/2017.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Property {

    boolean isField;
    boolean isSimple; //Todo handle arrays and collections
    boolean isNumber;
    String name;
    String typeName;
    Function<String, String> accessor;
    String getter;
    TypeName tName;

    Property(VariableElement element) {
        name = element.getSimpleName().toString();
        typeName = element.asType().toString();
        tName = TypeName.get(element.asType());
    }

    Property(TypeMirror type) {
        typeName = type.toString();
        name = Utils.qualifiedToSimple(typeName);
        tName = TypeName.get(type);
    }

    public String getAccessor(String var) {
        String res = var + "." + getter();
        if (accessor != null) res = accessor.apply(var); //fixme: awful design
        return modifyAccess(res);
    }

    protected String modifyAccess(String accessor) {
        if (getTypeName().equals("char") || getTypeName().equals("java.lang.Character"))
            accessor += " + \"\"";
        return accessor;
    }

    public String getter() {
        if (isField) return getName();
        return getter + "()";
    }

    //TODO handle more types. Watch JsonGenerator._writeSimpleObject
    public String genMethod() throws GenerationException {
        if (isNumber()) {
            return "writeNumber";
        }

        switch (getTypeName()) {
            case "boolean":case "java.lang.Boolean": return "writeBoolean";
            case "char":case "java.lang.Character":case "java.lang.String": return "writeString";
        }

        throw new GenerationException("Couldn't find generator method for " + this);
    }
}
