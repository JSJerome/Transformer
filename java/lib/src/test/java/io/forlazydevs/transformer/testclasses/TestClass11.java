package io.forlazydevs.transformer.testclasses;

import io.forlazydevs.transformer.annotations.TransformIdentity;
import io.forlazydevs.transformer.annotations.Transformable;

@Transformable
public class TestClass11 {

    @TransformIdentity
    private TestClass5 fieldOne = new TestClass5();

    @TransformIdentity("other")
    private TestClass13 fieldTwo = new TestClass13();
}
