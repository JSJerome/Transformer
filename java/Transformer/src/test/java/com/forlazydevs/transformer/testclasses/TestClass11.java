package com.forlazydevs.transformer.testclasses;

import com.forlazydevs.transformer.annotations.TransformIdentity;
import com.forlazydevs.transformer.annotations.Transformable;;

@Transformable
public class TestClass11 {

    @TransformIdentity
    private TestClass5 fieldOne = new TestClass5();

    @TransformIdentity("other")
    private TestClass13 fieldTwo = new TestClass13();
}
