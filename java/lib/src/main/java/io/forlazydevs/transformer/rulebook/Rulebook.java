package io.forlazydevs.transformer.rulebook;

import java.util.Map;
import java.util.Map.Entry;

import io.forlazydevs.transformer.exceptions.TransformerException;

public class Rulebook {

    private Map<String, String> fieldNameRules;
    private Map<Object, Class<?>> composedTransformRules;
    
    /** 
     * Adds composed transform rules to the rule book for the transformation.
     * 
     * @param composedTransformRules - A map where the key is either a String representing the field name or a Class representing the type of the field in the object to transform. The Value is the Class to transform the field into or null if you wish to leave the value of the field alone.
     * @throws TransformerException - If the Key is not a String or Class.
     */
    public void addComposedTransformRules(Map<Object, Class<?>> composedTransformRules) throws TransformerException {
        validateComposedTransformRules(composedTransformRules);
        if (!hasComposedRules()) {
            this.composedTransformRules = composedTransformRules;
        }
        else {
            this.composedTransformRules.putAll(composedTransformRules);
        }
   }

    /** 
     * Adds field name rules to the rule book for the transformation.
     * 
     * @param fieldNameRules - A Map of strings where the key represents the field name in the object to transform and the value represents the field name in the class you want to transform the key into.
     */
    public void addFieldNameRules(Map<String, String> fieldNameRules) {
        if (!hasFieldRules()){
            this.fieldNameRules = fieldNameRules;
        }
        else {
            this.fieldNameRules.putAll(fieldNameRules);
        }
   }

   /**
    * Clears the composed transform rules from the rule book.
    */
   public void clearComposedTransformRules() {
       if (hasComposedRules()) {
           this.composedTransformRules.clear();
       }
   }

    /**
     * Clears the field name rules from the rule book.
     */
    public void clearFieldNameRules() {
        if (hasFieldRules()) {
            this.fieldNameRules.clear();
        }
    }

   
   /** 
    * Retrieves the composed transform rules from the rule book.
    *
    * @return Map<Object, Class<?>> - Returns the map of composed transform rules from the rule book.
    */
   public Map<Object, Class<?>> getComposedRules() {
       return Map.copyOf(this.composedTransformRules);
   }

   /** 
     * Retrieves the field name rules from the rule book.
     * @return Map<String, String> - Returns the map of field name rules from the rule book.
     */
    public Map<String, String> getFieldNameRules() {
        return Map.copyOf(this.fieldNameRules);
    }

   
   /** 
    * Determines if the rule book has composed transform rules.
    *
    * @return boolean - A flag representing if the rule book has composed transform rules.
    */
   public boolean hasComposedRules() {
        return this.composedTransformRules != null && !this.composedTransformRules.isEmpty();
    }

    /** 
     * Determines if the rule book has field name rules.
     * 
     * @return boolean - A flag representing if the rule book has field name rules.
     */
    public boolean hasFieldRules() {
        return this.fieldNameRules != null && !this.fieldNameRules.isEmpty();
    }

   /** 
    * Removes any existing composed transform rules in the rule book and replaces them with the specified rules.
    *
    * @param composedTransformRules - A map where the key is either a String representing the field name or a Class representing the type of the field in the object to transform. The Value is the Class to transform the field into or null if you wish to leave the value of the field alone.
    * @throws TransformerException -  If the Key is not a String or Class.
    */
   public void resetComposedTransformRules(Map<Object, Class<?>> composedTransformRules) throws TransformerException {
        validateComposedTransformRules(composedTransformRules);
       if (!hasComposedRules()) {
           this.composedTransformRules = composedTransformRules;
       }
       else {
           this.composedTransformRules.clear();
           this.composedTransformRules.putAll(composedTransformRules);
       }
   }
    
    /** 
     * Removes any existing field name rules in the rule book and replaces them with the specified rules.
     * 
     * @param fieldNameRules - A Map of strings where the key represents the field name in the object to transform and the value represents the field name in the class you want to transform the key into.
     */
    public void resetFieldNameRules(Map<String, String> fieldNameRules) {
        if (!hasFieldRules()) {
            this.fieldNameRules = fieldNameRules;
        }
        else {
            this.fieldNameRules.clear();
            this.fieldNameRules.putAll(fieldNameRules);
        }
    }

    private void validateComposedTransformRules(Map<Object, Class<?>> composedTransformRules) throws TransformerException {
        String errorMessage = "Composed Transform Rules must have keys of type Class or String.";
        long invalidKeys = composedTransformRules.keySet()
                            .stream()
                            .filter(key -> 
                                !key.getClass().equals(Class.class)
                                && !key.getClass().equals(String.class))
                            .count();
        if(invalidKeys > 0){
            throw new TransformerException(errorMessage);
        }
    }  
}
