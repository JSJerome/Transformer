package com.forlazydevs.transformer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.forlazydevs.transformer.annotations.TransformComposed;
import com.forlazydevs.transformer.annotations.TransformIdentity;
import com.forlazydevs.transformer.annotations.Transformable;
import com.forlazydevs.transformer.exceptions.TransformerException;
import com.forlazydevs.transformer.rulebook.Rulebook;

public class Transformer {
    
    /** 
     * Transforms an object into the specified class. 
     *
     * @param toTransform - The object to transform.
     * @param transformationClass - The class to transform the toTransform object into.
     * @return T - A new instance of the transformationClass.
     * @throws TransformerException - If the transformer cannot transform the object. 
     */
    public static <T> T transform(Object toTransform, Class<T> transformationClass) throws TransformerException {
        return _transform(toTransform, transformationClass, null);
    }

    
    /** 
     * Transforms an object into the specified class. 
     * 
     * @param toTransform - The object to transform.
     * @param transformationClass - The class to transform the toTransform object into.
     * @param rules - Rules to apply to the transformation.
     * @return T - A new instance of the transformationClass.
     * @throws TransformerException - If the transformer cannot transform the object.
     */
    public static <T> T transform(Object toTransform, Class<T> transformationClass, Rulebook rules) throws TransformerException {
        return _transform(toTransform, transformationClass, rules);
    }

    private static void checkIfTransformable(Object object) throws TransformerException {
        Class<?> clazz = object.getClass();

        if(!clazz.isAnnotationPresent(Transformable.class))
        {
            throw new TransformerException("Class " + clazz.getSimpleName() + " has not been marked with @Transformable and will not be transformed.");
        }
    }

    private static boolean isFieldInTransformedClass(String fieldName, Class<?> clazz) {
        return Arrays.asList(clazz.getDeclaredFields())
                    .stream()
                    .map(field -> field.getName())
                    .anyMatch(name -> name.equals(fieldName));
    }

    private static <T> void processRuleBook(Field fieldContainingValue, Object valueToSet, String transformableFieldName, Object transformedObject, Class<T> classToTransformTo, Rulebook rules) throws NoSuchFieldException, SecurityException, TransformerException, IllegalArgumentException, IllegalAccessException {
        Object finalValueToSet = null;
        boolean composedTransformedHappened = false;
        // Process field rules first
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
        // Process Composed Transform Rules
        if(fieldContainingValue.isAnnotationPresent(TransformComposed.class)) {
            Class<?> fieldClass = valueToSet.getClass();
            // Process the composed rules for specified fields first.
            if (!Objects.isNull(rules) && rules.hasComposedRules() && rules.getComposedRules().containsKey(transformableFieldName)) {
                finalValueToSet = transformComposedField(valueToSet, transformableFieldName, rules);
                composedTransformedHappened = true;
            } else if(!Objects.isNull(rules) && rules.hasComposedRules() && rules.getComposedRules().containsKey(fieldClass))
            {
                finalValueToSet = transformComposedField(valueToSet, fieldClass, rules);
                composedTransformedHappened = true;
            }
        }

        // Process Identity Transformation
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

    private static <T> T _transform(Object object, Class<T> clazz, Rulebook rules) throws TransformerException {
        
        //Returns null if the object is null.
        if(Objects.isNull(object))
        {
            return null;
        }

        checkIfTransformable(object);
        Class<?> toBeTransformedClass = object.getClass();

        try {
            T transformedObject = clazz.getDeclaredConstructor().newInstance();

            // Retrieves the list of fields from the object to transform.
            List<String> toBeTransformedFields = Arrays.asList(toBeTransformedClass.getDeclaredFields())
                .stream()
                .map(field -> field.getName())
                .collect(Collectors.toList());
            
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

    private static Object transformComposedField(Object valueToSet, Object key, Rulebook rules) throws TransformerException {
        Object val = rules.getComposedRules().get(key);
        if(Objects.isNull(val)){
            return valueToSet;
        }
        else 
            return transform(valueToSet, (Class<?>)val, rules);
    }
}
