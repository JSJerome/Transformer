package io.forlazydevs.transformer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.forlazydevs.transformer.annotations.TransformComposed;
import io.forlazydevs.transformer.annotations.TransformIdentity;
import io.forlazydevs.transformer.annotations.Transformable;
import io.forlazydevs.transformer.exceptions.TransformerException;
import io.forlazydevs.transformer.rulebook.Rulebook;

public class Transformer {

    public static <T> T transform(Object object, Class<T> clazz) throws TransformerException {
        return _transform(object, clazz, null);
    }

    public static <T> T transform(Object object, Class<T> clazz, Rulebook rules) throws TransformerException {
        return _transform(object, clazz, rules);
    }

    private static <T> T _transform(Object object, Class<T> clazz, Rulebook rules) throws TransformerException {
        if(Objects.isNull(object))
        {
            return null;
        }

        checkIfTransformable(object);

        Class<?> toBeTransformedClass = object.getClass();

        try {
            T transformedObject = clazz.getDeclaredConstructor().newInstance();

            List<String> toBeTransformedFields = Arrays.asList(toBeTransformedClass.getDeclaredFields())
                .stream()
                .map(field -> field.getName())
                .collect(Collectors.toList());
            
            // Transform object by iterating through common and field rule fields.
            for(String fieldName: toBeTransformedFields) {
                Field toBeTransformedField = toBeTransformedClass.getDeclaredField(fieldName);
                toBeTransformedField.setAccessible(true);
                Object fieldValue = toBeTransformedField.get(object);
                processRuleBook(toBeTransformedField, fieldValue, fieldName, transformedObject, clazz, rules);
            }

            return transformedObject;
        } catch(Exception ex) {
            throw new TransformerException("Could not transform " + toBeTransformedClass.getSimpleName(), ex);
        }
    }

    private static void checkIfTransformable(Object object) throws TransformerException {
        Class<?> clazz = object.getClass();

        if(!clazz.isAnnotationPresent(Transformable.class))
        {
            throw new TransformerException("Class " + clazz.getSimpleName() + " has not been marked with @Transformable and will not be transformed.");
        }
    }

    private static <T> void processRuleBook(Field fieldContainingValue, Object valueToSet, String transformableFieldName, Object transformedObject, Class<T> classToTransformTo, Rulebook rules) throws NoSuchFieldException, SecurityException, TransformerException, IllegalArgumentException, IllegalAccessException {
        Object finalValueToSet = null;
        boolean composedTransformedHappened = false;
        //Process field rules first
        String fieldNameToSet = transformableFieldName;
        if(!Objects.isNull(rules) && rules.hasFieldRules() && rules.getFieldNameRules().containsKey(transformableFieldName)){
            fieldNameToSet = rules.getFieldNameRules().get(transformableFieldName);
        }

        // If field is not in new class. skip it.
        if(!isFieldInTransformedClass(fieldNameToSet, classToTransformTo)){
            return;
        }

        Field transformedField = classToTransformTo.getDeclaredField(fieldNameToSet);
        transformedField.setAccessible(true);
        //Process Composite Transform Rules
        if(fieldContainingValue.isAnnotationPresent(TransformComposed.class)) {
            Class<?> fieldClass = valueToSet.getClass();
            
            if (!Objects.isNull(rules) && rules.hasComposedRules() && rules.getComposedRules().containsKey(transformableFieldName)) {
                finalValueToSet = transformComposedField(valueToSet, transformableFieldName, rules);
                composedTransformedHappened = true;
            } else if(!Objects.isNull(rules) && rules.hasComposedRules() && rules.getComposedRules().containsKey(fieldClass))
            {
                finalValueToSet = transformComposedField(valueToSet, fieldClass, rules);
                composedTransformedHappened = true;
            }
        }

        //Process Identity Transformation
        if(!composedTransformedHappened && fieldContainingValue.isAnnotationPresent(TransformIdentity.class)){
            String identityFieldName = fieldContainingValue.getAnnotation(TransformIdentity.class).value();
            Class<?> fieldClass = valueToSet.getClass();
            Field identityField = fieldClass.getDeclaredField(identityFieldName);
            identityField.setAccessible(true);
            finalValueToSet = identityField.get(valueToSet);
        }

        //SetValue
        transformedField.set(transformedObject, Objects.isNull(finalValueToSet) ? valueToSet : finalValueToSet);
    }

    private static Object transformComposedField(Object valueToSet, Object key, Rulebook rules) throws TransformerException {
        Object val = rules.getComposedRules().get(key);
        if(Objects.isNull(val)){
            return valueToSet;
        }
        else 
            return transform(valueToSet, (Class<?>)val, rules);
    }

    private static boolean isFieldInTransformedClass(String fieldName, Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredFields())
                    .stream()
                    .map(field -> field.getName())
                    .anyMatch(name -> name.equals(fieldName));
    }
}