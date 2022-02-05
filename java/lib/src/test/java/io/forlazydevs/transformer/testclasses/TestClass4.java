package io.forlazydevs.transformer.testclasses;

import io.forlazydevs.transformer.annotations.Transformable;

@Transformable
public class TestClass4 {
    private int fieldOne;
    private boolean fieldTwo;

    public TestClass4() {}

    public TestClass4(int fieldOne, boolean fieldTwo) {
        this.fieldOne = fieldOne;
        this.fieldTwo = fieldTwo;
    }

    public int getFieldOne(){
        return this.fieldOne;
    }

    public boolean getFieldTwo(){
        return this.fieldTwo;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(!o.getClass().equals(this.getClass())){
            return false;
        }
        TestClass4 other = (TestClass4)o;
        return (this.fieldOne == other.getFieldOne()) && (this.fieldTwo == other.getFieldTwo());
    }
    
}
