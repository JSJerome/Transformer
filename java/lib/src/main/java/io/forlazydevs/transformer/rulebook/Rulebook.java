package io.forlazydevs.transformer.rulebook;

import java.util.Map;

import io.forlazydevs.transformer.exceptions.TransformerException;

public class Rulebook {

    private Map<String, String> fieldNameRules;
    private Map<Object, Class<?>> composedTransformRules;

    public void addComposedTransformRules(Map<Object, Class<?>> composedTransformRules) throws TransformerException {
        validateComposedTransformRules(composedTransformRules);
        if (this.composedTransformRules == null || this.composedTransformRules.isEmpty()) {
            this.composedTransformRules = composedTransformRules;
        }
        else {
            this.composedTransformRules.putAll(composedTransformRules);
        }
   }

   public void clearComposedTransformRules() {
       if (this.composedTransformRules != null && !this.composedTransformRules.isEmpty()) {
           this.composedTransformRules.clear();
       }
   }

   public Map<Object, Object> getComposedRules() {
       return Map.copyOf(this.composedTransformRules);
   }

   public void resetComposedTransformRules(Map<Object, Class<?>> composedTransformRules) throws TransformerException {
        validateComposedTransformRules(composedTransformRules);
       if (this.composedTransformRules == null || this.composedTransformRules.isEmpty()) {
           this.composedTransformRules = composedTransformRules;
       }
       else {
           this.composedTransformRules.clear();
           this.composedTransformRules.putAll(composedTransformRules);
       }
   }
    
    public void addFieldNameRules(Map<String, String> fieldNameRules) {
         if (this.fieldNameRules == null || this.fieldNameRules.isEmpty()) {
             this.fieldNameRules = fieldNameRules;
         }
         else {
             this.fieldNameRules.putAll(fieldNameRules);
         }
    }

    public void clearFieldNameRules() {
        if (this.fieldNameRules != null && !this.fieldNameRules.isEmpty()) {
            this.fieldNameRules.clear();
        }
    }

    public Map<String, String> getFieldNameRules() {
        return Map.copyOf(this.fieldNameRules);
    }

    public void resetFieldNameRules(Map<String, String> fieldNameRules) {
        if (this.fieldNameRules == null || this.fieldNameRules.isEmpty()) {
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

    public boolean hasFieldRules() {
        return this.fieldNameRules != null && !this.fieldNameRules.isEmpty();
    }

    public boolean hasComposedRules() {
        return this.composedTransformRules != null && !this.composedTransformRules.isEmpty();
    }
    
}
