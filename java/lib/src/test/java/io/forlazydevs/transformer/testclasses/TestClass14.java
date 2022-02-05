package io.forlazydevs.transformer.testclasses;

import io.forlazydevs.transformer.annotations.TransformComposed;
import io.forlazydevs.transformer.annotations.TransformIdentity;
import io.forlazydevs.transformer.annotations.Transformable;

@Transformable
public class TestClass14 {
    
    @TransformComposed
    @TransformIdentity("fieldOne")
    private TestClass15 fieldOne = new TestClass15();

    public TestClass15 getFieldOne(){
        return this.fieldOne;
    }
}
