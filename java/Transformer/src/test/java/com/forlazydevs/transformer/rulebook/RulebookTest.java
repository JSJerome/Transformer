package com.forlazydevs.transformer.rulebook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.forlazydevs.transformer.exceptions.TransformerException;
import com.forlazydevs.transformer.rulebook.Rulebook;

import org.junit.Before;
import org.junit.Test;

public class RulebookTest {

    private Map<String, String> fieldNameRules;
    private Map<Object,Class<?>> composedTransformRulesStringKey;
    private Map<Object,Class<?>> composedTransformRulesClassKey;
    private Rulebook rules;

    @Before
    public void setUp() {
        fieldNameRules = new HashMap<String,String>();
        composedTransformRulesStringKey = new HashMap<Object,Class<?>>();
        composedTransformRulesClassKey = new HashMap<Object,Class<?>>();
        fieldNameRules.put("fieldOne", "fieldTwo");
        composedTransformRulesStringKey.put("fieldOne", String.class);
        composedTransformRulesClassKey.put(Object.class, String.class);
        rules = new Rulebook();
    }

    @Test
    public void testCanSetFieldNameRules() {
        rules.addFieldNameRules(fieldNameRules);
        assertEquals(rules.getFieldNameRules().keySet(), fieldNameRules.keySet());
        assertEquals(rules.getFieldNameRules().get("fieldOne"), fieldNameRules.get("fieldOne"));
    }

    @Test
    public void testCanSetComposedTransformWithStringKeyRules() throws Exception {
        rules.addComposedTransformRules(composedTransformRulesStringKey);
        assertEquals(rules.getComposedRules().keySet(), composedTransformRulesStringKey.keySet());
        assertEquals(rules.getComposedRules().get("fieldOne"), composedTransformRulesStringKey.get("fieldOne"));
    }

    @Test
    public void testCanSetComposedTransformWithClassKeyRules() throws Exception {
        rules.addComposedTransformRules(composedTransformRulesClassKey);
        assertEquals(rules.getComposedRules().keySet(), composedTransformRulesClassKey.keySet());
        assertEquals(rules.getComposedRules().get(Object.class), composedTransformRulesClassKey.get(Object.class));
    }

    @Test(expected = TransformerException.class)
    public void testCannotSetComposedTransformRulesWithKeyNotStringOrClass() throws Exception {
        rules.addComposedTransformRules(Map.of(List.of(1), String.class));
    }

    @Test 
    public void testHasFieldNameRulesRulesHaveNotBeenSet() {
        assertFalse(rules.hasFieldRules());
    }

    @Test 
    public void testHasComposedTransformRulesRulesHaveNotBeenSet() {
        assertFalse(rules.hasComposedRules());
    }

    @Test
    public void testHasFieldNameRulesRulesEmpty() {
        rules.addFieldNameRules(new HashMap<String,String>());
        assertFalse(rules.hasFieldRules());
    }

    @Test
    public void testHasComposedTransformRulesRulesEmpty() throws Exception {
        rules.addComposedTransformRules(new HashMap<Object,Class<?>>());
        assertFalse(rules.hasComposedRules());
    }

    @Test
    public void testHasFieldNameRules() {
        rules.addFieldNameRules(fieldNameRules);
        assertTrue(rules.hasFieldRules());
    }

    @Test
    public void testHasComposedTransformRules() throws Exception {
        rules.addComposedTransformRules(composedTransformRulesClassKey);
        assertTrue(rules.hasComposedRules());
    }

    @Test
    public void testClearFieldNameRules() {
        rules.addFieldNameRules(fieldNameRules);
        rules.clearFieldNameRules();
        assertFalse(rules.hasFieldRules());
    }

    @Test
    public void testClearComposedTransformRules() throws Exception {
        rules.addComposedTransformRules(composedTransformRulesStringKey);
        rules.clearComposedTransformRules();
        assertFalse(rules.hasComposedRules());
    }

    @Test
    public void testClearFieldNameRulesRulesNull () {
        rules.clearFieldNameRules();
        assertFalse(rules.hasFieldRules());
    }

    @Test
    public void testClearComposedTransformRulesRulesNull() {
        rules.clearComposedTransformRules();
        assertFalse(rules.hasComposedRules());
    }

    @Test
    public void testClearFieldNameRulesRulesEmpty() {
        rules.addFieldNameRules(new HashMap<String, String>());
        assertFalse(rules.hasFieldRules());
    }

    @Test
    public void testClearComposedTransformRulesRulesEmpty() throws Exception{
        rules.addComposedTransformRules(new HashMap<Object, Class<?>>());
        assertFalse(rules.hasComposedRules());
    }

    @Test
    public void testResetFieldNameRules() {
        Map<String,String> newRule = Map.of("NewKey", "NewValue");
        rules.addFieldNameRules(fieldNameRules);
        rules.resetFieldNameRules(newRule);
        assertEquals(rules.getFieldNameRules().keySet(), newRule.keySet());
        assertEquals(rules.getFieldNameRules().get("NewKey"), newRule.get("NewKey"));
    }

    @Test
    public void testResetComposedTransformRules() throws Exception {
        Map<Object, Class<?>> newRule = Map.of("NewKey", Exception.class);
        rules.addComposedTransformRules(composedTransformRulesStringKey);
        rules.resetComposedTransformRules(newRule);
        assertEquals(rules.getComposedRules().keySet(), newRule.keySet());
        assertEquals(rules.getComposedRules().get("NewKey"), newRule.get("NewKey"));
    }

    @Test
    public void testAddFieldNameRulesWhenRulesExist() {
        Map<String,String> newRule = Map.of("NewKey", "NewValue");
        rules.addFieldNameRules(fieldNameRules);
        rules.addFieldNameRules(newRule);
        assertEquals(2, rules.getFieldNameRules().size());
    }

    @Test
    public void testAddComposedTransformRulesWhenRulesExist() throws Exception {
        Map<Object, Class<?>> newRule = Map.of("NewKey", Exception.class);
        rules.addComposedTransformRules(composedTransformRulesStringKey);
        rules.addComposedTransformRules(newRule);
        assertEquals(2, rules.getComposedRules().size());
    }


    
}
