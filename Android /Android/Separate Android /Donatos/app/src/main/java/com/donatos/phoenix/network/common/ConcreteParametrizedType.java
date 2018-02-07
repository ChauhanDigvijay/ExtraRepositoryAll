package com.donatos.phoenix.network.common;

import com.bluelinelabs.logansquare.ParameterizedType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

public class ConcreteParametrizedType<T> extends ParameterizedType<T> {
    public ConcreteParametrizedType(Type type) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = getClass().getSuperclass().getDeclaredField("rawType");
        declaredField.setAccessible(true);
        declaredField.set(this, getRawType(type));
        addTypeParameters(type);
    }

    private void addTypeParameters(Type type) throws NoSuchFieldException, IllegalAccessException {
        if (type instanceof java.lang.reflect.ParameterizedType) {
            Type[] actualTypeArguments = ((java.lang.reflect.ParameterizedType) type).getActualTypeArguments();
            if (actualTypeArguments != null) {
                for (Type concreteParametrizedType : actualTypeArguments) {
                    this.typeParameters.add(new ConcreteParametrizedType(concreteParametrizedType));
                }
            }
        }
    }

    private Class getRawType(Type type) {
        Object obj = type;
        while (!(obj instanceof Class)) {
            if (obj instanceof java.lang.reflect.ParameterizedType) {
                return (Class) ((java.lang.reflect.ParameterizedType) obj).getRawType();
            }
            if (obj instanceof TypeVariable) {
                return Object.class;
            }
            if (obj instanceof WildcardType) {
                obj = ((WildcardType) obj).getUpperBounds()[0];
            } else if (obj instanceof GenericArrayType) {
                return Array.newInstance(getRawType(((GenericArrayType) obj).getGenericComponentType()), 0).getClass();
            } else {
                throw new RuntimeException("Invalid type passed: " + obj);
            }
        }
        return (Class) obj;
    }
}
