## Design Patterns
 ### Singleton
 > only allows **one** object from class

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
> a collection of classes that implements an **interface** or extends and **Abstract** class
> centralizes object creation that share a common type of
class using factory

```java
public class GeoShapeFactory {
  public GeoShape createShape(String shapeType) {
    
  }
}
```
