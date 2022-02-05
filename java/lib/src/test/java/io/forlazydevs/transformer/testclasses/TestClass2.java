package io.forlazydevs.transformer.testclasses;

import io.forlazydevs.transformer.annotations.Transformable;

@Transformable
public class TestClass2 {
    private String fieldOne = "MyValue";
    private int fieldTwo = 1;
    private Object fieldThree = null;

    public TestClass2() {};

    public String getFieldOne(){
        return this.fieldOne;
    }

    public int getFieldTwo(){
        return this.fieldTwo;
    }

    public Object getFieldThree(){
        return this.fieldThree;
    }
}

