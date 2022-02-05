package com.forlazydevs.transformer.testclasses;

import com.forlazydevs.transformer.annotations.TransformComposed;
import com.forlazydevs.transformer.annotations.TransformIdentity;
import com.forlazydevs.transformer.annotations.Transformable;;

@Transformable
public class TestClass14 {
    
    @TransformComposed
    @TransformIdentity("fieldOne")
    private TestClass15 fieldOne = new TestClass15();

    public TestClass15 getFieldOne(){
        return this.fieldOne;
    }
}
