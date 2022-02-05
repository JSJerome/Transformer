package io.forlazydevs.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import io.forlazydevs.transformer.exceptions.TransformerException;
import io.forlazydevs.transformer.rulebook.Rulebook;
import io.forlazydevs.transformer.testclasses.TestClass1;
import io.forlazydevs.transformer.testclasses.TestClass10;
import io.forlazydevs.transformer.testclasses.TestClass11;
import io.forlazydevs.transformer.testclasses.TestClass12;
import io.forlazydevs.transformer.testclasses.TestClass14;
import io.forlazydevs.transformer.testclasses.TestClass15;
import io.forlazydevs.transformer.testclasses.TestClass16;
import io.forlazydevs.transformer.testclasses.TestClass17;
import io.forlazydevs.transformer.testclasses.TestClass2;
import io.forlazydevs.transformer.testclasses.TestClass3;
import io.forlazydevs.transformer.testclasses.TestClass4;
import io.forlazydevs.transformer.testclasses.TestClass5;
import io.forlazydevs.transformer.testclasses.TestClass8;
import io.forlazydevs.transformer.testclasses.TestClass9;

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

    @Test(expected = TransformerException.class)
    public void testTransformFieldsCantBePutInIncompatibleTypes() throws Exception{
        TestClass2 classTwo = new TestClass2();
        Transformer.transform(classTwo, TestClass4.class);
    }

    @Test
    public void testTransformNullObjectReturnsNull() throws Exception {
        TestClass2 classTwo = null;
        assertNull(Transformer.transform(classTwo, TestClass1.class));
    }

    @Test(expected = TransformerException.class)
    public void testCantTransformClassNotTransformable() throws Exception {
        TestClass1 classOne = new TestClass1();
        Transformer.transform(classOne, TestClass2.class);
    }

    @Test(expected = TransformerException.class)
    public void testCantTransformNotTransformableTransformComposedField() throws Exception {
        TestClass8 classEight = new TestClass8();
        Rulebook rules = new Rulebook();
        rules.addComposedTransformRules(Map.of(TestClass1.class, TestClass2.class));
        Transformer.transform(classEight, TestClass10.class, rules);
    }

    @Test(expected = TransformerException.class)
    public void testCantIdentityTransformFieldWithoutIdField() throws Exception {
        TestClass11 classEleven = new TestClass11();
        Transformer.transform(classEleven, TestClass12.class);
    }

    @Test(expected = TransformerException.class)
    public void testCantIdentityTransformFieldWithOutSpecifiedField() throws Exception {
        TestClass11 classEleven = new TestClass11();
        Transformer.transform(classEleven, TestClass12.class);
    }

    @Test
    public void testIdentityTransformIsSkippedIfComposedTransformPerformed() throws Exception {
        TestClass14 classFourteen = new TestClass14();
        Rulebook rules = new Rulebook();
        rules.addComposedTransformRules(Map.of(TestClass15.class, TestClass16.class));
        TestClass17 classSeventeen = Transformer.transform(classFourteen, TestClass17.class, rules);
        assertEquals(classFourteen.getFieldOne().getFieldOne(), classSeventeen.getFieldOne().getFieldOne());
    }

    @Test
    public void testIdentityTransformIsNotSkippedIfComposedTransformDoesNotHappen() throws Exception {
        TestClass14 classFourteen = new TestClass14();
        TestClass16 classSixteen = Transformer.transform(classFourteen, TestClass16.class);
        assertEquals(classFourteen.getFieldOne().getFieldOne(), classSixteen.getFieldOne());
    }
    
}
