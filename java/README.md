# Transformer (Java)

 Typically, when developing server side services with databases, you have an entity to describe your database object and a data transfer object (DTO) to return objects to requesting clients. This java library is designed to make transforming between entities and DTOs, or other Java objects, a quick and painless task. 
 
 Once you have a populated instance, call the `Transformer.transform()` method to transform your instance to an instance of another class. Example:

 ```java
@Transformable
 public class MyClass1 {
     private int one = 1;
     private String two = "2";
 }

 public class MyClass2 {
     private int one;
     private String two;
 }

 public class Application {
     public static void main(String[] args) {
         MyClass1 myClass1 = new MyClass1();
         MyClass2 myClass2 = Transformer.transform(myClass1, MyClass2.class);
     }
 }
 ```
 ```text
 Output: [MyClass2]
 [
     one = 1
     two = "2"
 ]
 ```

 ## How it works

 The transformer will populate the properties of the desired instance with the values of the properties **with the same name** of the instance you are transforming. It can copy any properties of any type including other Java classes. The transformer will skip any properties present in the current instance, that are not present in the new instance. The current instance must be marked with the `@Transformable` annotation or the transformer will throw a `TransformerException`.

 To use the transformer, your Java classes must have a public no-args constructor.

 ## Installation

 Add the following to the dependencies section of your build.gradle. 

```groovy
compile 'com.forlazydevs:transformer:1.0.0'
```

 ## Rulebook

 To have more fine-grain control over the transformation, the `transform()` method accepts a third parameter of the `Rulebook` type.

 The `Rulebook` has two types of rules:
 - Field Name Rules
 - Composed Transform Rules

 You can mix and match these rules however you desire.

 ### Field Name Rules

 Field Name Rules allow specification of which field in the new instance, a value of the current instance should be copied to. This bypasses the default expectation that properties in both classes have to have the same name. 
 
 Field name rules are provided to the rule book in the form of a `Map<String,String>`. The key is the field name in the current instance to retrieve the value from. The value is the field name in the new instance to populate. Example:

 ```java
@Transformable
 public class MyClass1 {
     private int one = 1;
     private String two = "2";
 }

 public class MyClass2 {
     private int one;
     private String myTwo;
 }

 public class Application {
     public static void main(String[] args) {
         MyClass1 myClass1 = new MyClass1();
         Map<String,String> fieldRules = new HashMap<String,String>();
         fieldRules.put("two", "myTwo");
         Rulebook rules = new RuleBook();
         rules.addFieldNameRules(fieldRules);
         MyClass2 myClass2 = Transformer.transform(myClass1, MyClass2.class, rules);
     }
 }
 ```
 ```text
 Output: [MyClass2]
 [
     one = 1
     myTwo = "2"
 ]
 ```

 ### Composed Transform Rules

 Composed Transform Rules specify how embedded objects should be transformed when the current instance is going through a transformation. Any embedded object that needs to be transformed should be annotated with `@TransformComposed`. If this annotation is omitted the transformer will attempt to copy the value of the embedded object as is to the new instance and may lead to a `TransformerException`. The class of the embedded object must be annotated with `@Transformable`. 

Field name rules are provided to the rule book in the form of a `Map<Object,Class<?>>`. The key is either a `String` representing the field name in the current instance to retrieve the value from or a `Class<?>` specifying the class of the embedded object to transform. The value is the `Class<?>` to transform the object to. Example:

 ```java
@Transformable
 public class MyClass1 {
     private int one = 1;
     private String two = "2";
 }

 public class MyClass2 {
     private int one;
     private String two;
 }

 @Transformable
 public class MyClass3 {
     private double one = 1.1;
     private boolean two = true;
     @TransformComposed
     private MyClass1 three = new MyClass1();

 }

 public class MyClass4 {
     private double one;
     private boolean two;
     private MyClass2 three;
 }

 public class Application {
     public static void main(String[] args) {
         MyClass3 myClass3 = new MyClass3();
         Map<Object,Class<?>> composedRules = new HashMap<Object,Class<?>>();
         composedRules.put(MyClass1.class, MyClass2.class);
         Rulebook rules = new RuleBook();
         rules.addComposedTransformRules(composedRules);
         MyClass4 myClass4 = Transformer.transform(myClass3, MyClass4.class, rules);
     }
 }
 ```
 ```text
 Output: [MyClass4]
 [
     one = 1.1
     two = true
     three = [MyClass2]
     [
        one = 1
        two = "2"
     ]
 ]
 ```
 Using a `String` of the field name as the key provides more fine-grain control of the transformation. For example, if you wished for all but one instance of MyClass1 in MyClass3 to be transformed into MyClass2. You could simply do the following:

```java
@Transformable
 public class MyClass1 {
     private int one = 1;
     private String two = "2";
 }

 public class MyClass2 {
     private int one;
     private String two;
 }

 @Transformable
 public class MyClass3 {
     private double one = 1.1;
     private boolean two = true;
     @TransformComposed
     private MyClass1 three = new MyClass1();
     @TransformComposed
     private MyClass1 four = new MyClass1();
     @TransformComposed
     private MyClass1 five = new MyClass1();

 }

 public class MyClass4 {
     private double one;
     private boolean two;
     private MyClass2 three;
     private MyClass2 four;
     private MyClass5 five;
 }

 public class MyClass5 {
     private int one;
     private String two;
 }

 public class Application {
     public static void main(String[] args) {
         MyClass3 myClass3 = new MyClass3();
         Map<Object,Class<?>> composedRules = new HashMap<Object,Class<?>>();
         composedRules.put(MyClass1.class, MyClass2.class);
         composedRules.put("five", MyClass5.class); // Alternatively you could put 'null' as the value if you wanted to copy MyClass1 as is to the new instance or leave off the @TransformComposed annotation on the property.
         Rulebook rules = new RuleBook();
         rules.addComposedTransformRules(composedRules);
         MyClass4 myClass4 = Transformer.transform(myClass3, MyClass4.class, rules);
     }
 }
 ```
 ```text
 Output: [MyClass4]
 [
     one = 1.1
     two = true
     three = [MyClass2]
     [
        one = 1
        two = "2"
     ]
     four = [MyClass2]
     [
        one = 1
        two = "2"
     ]
     five = [MyClass5]
     [
        one = 1
        two = "2"
     ]
 ]
 ```

 ## Other Advanced Transforms

 ### @TransformIdentity

 The `@TransformIdentity` annotation will copy the value of the specified identity property of an embedded object in the current instance into the new instance while doing a transformation. If no property is specified, the transformer will attempt to use a property named `id` by default. If the specified identity or `id` property does not exist in the embedded class a `TransformerException` will be thrown. The classes of the properties annotated with `@TransformIdentity` do **NOT** *need* to be annotated with `@Transformable`. Example: 

 ```java
 public class MyClass1 {
     private int id = 1;
     private String two = "2";
 }

 public class MyClass2 {
     private int one = 2;
     private String two = "abcde";
 }

 @Transformable
 public class MyClass3 {
     private double one = 1.1;
     private boolean two = true;
     @TransformIdentity
     private MyClass1 three = new MyClass1();
     @TransformIdentity("two")
     private MyClass2 four = new MyClass2();
 }

 public class MyClass4 {
     private double one;
     private boolean two;
     private int three;
     private String four;
 }

 public class Application {
     public static void main(String[] args) {
         MyClass3 myClass3 = new MyClass3();
         MyClass4 myClass4 = Transformer.transform(myClass3, MyClass4.class);
     }
 }
 ```
 ```text
 Output: [MyClass4]
 [
     one = 1.1
     two = true
     three = 1
     four = "abcde"
 ]
 ```

**NOTE:** You can use *both* `@TransformIdentity` and `@TransformComposed` annotations on a field. If composed transform rules have been defined in the transformation, the identity transformation. This was designed for situations where you may need to transform an embedded object to another instance when transforming from an entity to a model but only need the identity transforming from an entity to a DTO. Example:

 ```java
 @Transformable
 public class MyClass1 {
     private int id = 1;
     private String two = "2";
 }

 public class MyClass2 {
     private int one = 2;
     private String two = "abcde";
 }

 @Transformable
 public class MyClass3 {
     private double one = 1.1;
     private boolean two = true;
     @TransformIdentity
     @TransformComposed
     private MyClass1 three = new MyClass1();
     @TransformIdentity("two")
     private MyClass2 four = new MyClass2();
 }

 public class MyClass4 {
     private double one;
     private boolean two;
     private int three;
     private String four;
 }

 public class MyClass5 {
     private double one;
     private boolean two;
     private MyClass2 three;
     private String four;
 }

 public class Application {
     public static void main(String[] args) {
         MyClass3 myClass3 = new MyClass3();
         Map<Object,Class<?>> composedRules = new HashMap<Object,Class<?>>();
         composedRules.put(MyClass1.class, MyClass2.class);
         Rulebook rules = new RuleBook();
         rules.addComposedTransformRules(composedRules);
         MyClass5 myClass5 = Transformer.transform(myClass3, MyClass5.class, rules);
         MyClass4 myClass4 = Transformer.transform(myClass3, MyClass4.class);
     }
 }
 ```
 ```text
 Output: [MyClass4]
 [
     one = 1.1
     two = true
     three = 1
     four = "abcde"
 ]

 [MyClass5]
 [
     one = 1.1
     two = true
     three = [MyClass2]
     [
        one = 1
        two = "2"
     ]
     four = "abcde"
 ]
 ```
## Classes

### Transformer

A class with a static `transform` method to transform.

#### transform()

```text
Transforms an object into the specified class. 

Params
Object toTransform - The object to transform.
Class<T> transformationClass - The class to transform the toTransform object into.
rules - Rules to apply to the transformation. (Optional)

Returns
T - A new instance of the transformationClass.

Throws
TransformerException - If the transformer cannot transform the object. 
```

### Rulebook

A class containing rules to apply to transformations.

#### addComposedTransformRules()

```text
Adds composed transform rules to the rule book for the transformation.

Params
Map<Object,Class<?>> composedTransformRules - A map where the key is either a String representing the field name or a Class representing the type of the field in the object to transform. The Value is the Class to transform the field into or null if you wish to leave the value of the field alone.

Throws
TransformerException - If the Key is not a String or Class.
```

#### addFieldNameRules()
```text
Adds field name rules to the rule book for the transformation.

Params
Map<String, String> fieldNameRules - A Map of strings where the key represents the field name in the object to transform and the value represents the field name in the class you want to transform the key into.
```

#### clearComposedTransformRules()

```text
Clears the composed transform rules from the rule book.
```

#### clearFieldNameRules()

```text
Clears the field name rules from the rule book.
```

#### getComposedRules()

```text
Retrieves the composed transform rules from the rule book.

Returns
Map<Object, Class<?>> - Returns the map of composed transform rules from the rule book.
```

#### getFieldNameRules()

```text
Retrieves the field name rules from the rule book.

Returns
Map<String, String> - Returns the map of field name rules from the rule book.
```

#### hasComposedRules()

```text
Determines if the rule book has composed transform rules.

Returns
boolean - A flag representing if the rule book has composed transform rules.
```

#### hasFieldRules()

```text
Determines if the rule book has field name rules.

Returns
boolean - A flag representing if the rule book has field name rules.
```

#### resetComposedTransformRules()

```text
Removes any existing composed transform rules in the rule book and replaces them with the specified rules.

Params
Map<Object,Class<?>> composedTransformRules - A map where the key is either a String representing the field name or a Class representing the type of the field in the object to transform. The Value is the Class to transform the field into or null if you wish to leave the value of the field alone.

Throws
TransformerException -  If the Key is not a String or Class.
```

#### resetFieldNameRules()

```text
Removes any existing field name rules in the rule book and replaces them with the specified rules.

Params
Map<String,String> fieldNameRules - A Map of strings where the key represents the field name in the object to transform and the value represents the field name in the class you want to transform the key into.
```


