## Design Patterns
---
 ### Singleton
 > - only allows **one** object from class

```java
public class Singleton {
  // needs to be priv
  private static Singleton theUniqueInstance = new Singleton();
  public static Singleton getInstance() {
    return theUniqueInstance;
  }
  private Singleton() {....}
  // ^ private so cant be accessed and make another instance of obj.
}
```

### Factory
>- a collection of classes that implements an **interface** or extends and **Abstract** class
>- centralizes object creation that share a common type of

this class implements a **Factory** Design Pattern called **GeoShapeFactory**
```java
public class GeoShapeFactory {
  public GeoShape createShape(String shapeType) {
    if (shapeType.equalsIgnoreCase("CIRCLE")) {
      return new Circle(0,0,10);
    }
    .
    .
    .
    return null;
  }
}
```
this class uses the **Factory** Design Class from above ^^^

``` java
public class FactoryDemo {
  public static void main (String[] args) {
    GeoShapeFactory factory = new GeoShapeFactory();

    LinkedList<GeoShape> list = new LinkedList<GeoShape>;
    .
    .
    .
    // this creates the shape using the shapeType parameter from GeoShapeFactory Factory class
    // and adds to LinkedList Data Structure
    list.add(factory.createShape("circle"));
  }
}
```
