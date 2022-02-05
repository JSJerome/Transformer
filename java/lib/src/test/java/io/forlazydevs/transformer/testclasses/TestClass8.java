package io.forlazydevs.transformer.testclasses;

import io.forlazydevs.transformer.annotations.TransformComposed;
import io.forlazydevs.transformer.annotations.Transformable;

@Transformable
public class TestClass8 {
    
    private int fieldOne = 0;
    private char fieldTwo = 'c';

    @TransformComposed
    private TestClass1 fieldThree = new TestClass1();
}
