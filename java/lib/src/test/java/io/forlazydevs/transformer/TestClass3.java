package io.forlazydevs.transformer;

import io.forlazydevs.transformer.annotations.TransformComposed;
import io.forlazydevs.transformer.annotations.TransformIdentity;
import io.forlazydevs.transformer.annotations.Transformable;

@Transformable
public class TestClass3 {
    private String fieldOne = "MyValue";
    private int fieldTwo = 1;
    private Object fieldThree = null;
    private String notFieldFour = "Am I going to be transformed?";

    @TransformComposed
    private TestClass4 fieldFive = new TestClass4(0, true);

    @TransformIdentity
    private TestClass6 fieldSix = new TestClass6();

    @TransformIdentity("identity")
    private TestClass7 fieldSeven = new TestClass7();

    @TransformComposed
    private TestClass4 fieldEight = new TestClass4(1, false);

    public TestClass3() {};

    public String getFieldOne(){
        return this.fieldOne;
    }

    public int getFieldTwo(){
        return this.fieldTwo;
    }

    public Object getFieldThree(){
        return this.fieldThree;
    }

    public String getNotFieldFour(){
        return this.notFieldFour;
    }

    public TestClass4 getFieldFive(){
        return this.fieldFive;
    }

    public TestClass6 getFieldSix(){
        return this.fieldSix;
    }

    public TestClass7 getFieldSeven(){
        return this.fieldSeven;
    }

    public TestClass4 getFieldEight(){
        return this.fieldEight;
    }
    
}
