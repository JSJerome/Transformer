package io.forlazydevs.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import io.forlazydevs.transformer.rulebook.Rulebook;

public class TransformerTest {

    @Test
    public void testTransform() throws Exception {
        TestClass2 classTwo = new TestClass2();
        TestClass1 classOne = Transformer.transform(classTwo, TestClass1.class);
        assertEquals(classOne.getFieldOne(), classTwo.getFieldOne());
        assertEquals(classOne.getFieldTwo(), classTwo.getFieldTwo());
        assertNull(classOne.getFieldThree());
    }

    @Test
    public void testTransformWithRules() throws Exception {
        TestClass3 classThree = new TestClass3();
        Rulebook rules = new Rulebook();
        rules.addFieldNameRules(Map.of("notFieldFour", "fieldFour"));
        rules.addComposedTransformRules(Map.of(TestClass4.class, TestClass5.class, "fieldEight", TestClass9.class));
        TestClass1 classOne = Transformer.transform(classThree, TestClass1.class, rules);
        assertEquals(classOne.getFieldOne(), classThree.getFieldOne());
        assertEquals(classOne.getFieldTwo(), classThree.getFieldTwo());
        assertNull(classOne.getFieldThree());
        assertEquals(classOne.getFieldFour(), classThree.getNotFieldFour());
        assertEquals(classOne.getFieldFive().getFieldOne(), classThree.getFieldFive().getFieldOne());
        assertEquals(classOne.getFieldFive().getFieldTwo(), classThree.getFieldFive().getFieldTwo());
        assertEquals(classOne.getFieldSix(), classThree.getFieldSix().getId());
        assertEquals(classOne.getFieldSeven(), classThree.getFieldSeven().getIdentity(),0);
        assertEquals(classOne.getFieldEight().getFieldOne(), classThree.getFieldEight().getFieldOne());
        assertEquals(classOne.getFieldEight().getFieldTwo(), classThree.getFieldEight().getFieldTwo());
        
    }
    
}
