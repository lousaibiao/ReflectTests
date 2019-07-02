> [原文](https://www.baeldung.com/java-reflection)

## 概况

使用java的反射，可以让我们检查（或者修改）类，接口，字段，方法的特性。当你在编译期不知道他们的名字的时候非常有用。

除此之外，可以使用反射来创建实例，调用方法或者get/set 字段值。

## 设置项目

需要做的只有导个包。

```java
import java.lang.reflect.*; //根据使用的情况导特定的，比如reflect.constructor等
```

## 简单例子

先加一下junit的依赖，然后添加一个Person类，两个字段。

```java
public class Person {
    private String name;
    private int age;
}
```

写个测试方法来获取这个类的所有字段。

```java
 /**
  * 包含所有字段
  */
 @Test
 public void containAllFields() {
     Object p = new Person();
     Field[] declaredFields = p.getClass().getDeclaredFields();
     List<String> actualFields = Arrays.stream(declaredFields).map(field -> field.getName()).collect(Collectors.toList());
     List<String> exceptFields = new ArrayList<>();
     exceptFields.add("age");
     exceptFields.add("name");
     Assert.assertTrue(exceptFields.containsAll(actualFields));
 }
```

## 使用场景

最常用的使用场景为数据库表和实体类做字段映射。

## 检查java类

下面来做一些测试，用来获取前面提到过的比如类名，修饰符，字段，方法，实现的接口之类的东西。

### 准备工作

先创建一个Eating接口

```java
public interface Eating {
    String eats();
}
```

创建一个抽象Animal类来实现这个Eating接口

```java
package com.lou.reflect.test;

public abstract class Animal implements Eating {
    public static String CATEGORY = "domestic";
    private String name;

    protected abstract String getSound();

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```

创建一个`Locomotion`接口用来描述动物的行动方式。

```java
package com.lou.reflect.test;

public interface Locomotion {
    String getLocomotion();
}
```

创建一个具体的`Goat`类，继承自`Animal`同时实现`Locomotion`接口。

```java
package com.lou.reflect.test;

public class Goat extends Animal implements Locomotion {
    public Goat(String name) {
        super(name);
    }

    @Override
    protected String getSound() {
        return "山羊叫";
    }

    @Override
    public String eats() {
        return "吃草";
    }

    @Override
    public String getLocomotion() {
        return "行走";
    }
}
```

创建一个Bird类，继承自Animal

```java
public class Bird extends Animal {
    private boolean walks;


    public Bird() {
        super("鸟");
    }

    public Bird(String name) {
        super(name);
    }

    public Bird(String name, boolean walks) {
        super(name);
        this.walks = walks;
    }

    @Override
    protected String getSound() {
        return null;
    }

    @Override
    public String eats() {
        return null;
    }

    public boolean isWalks() {
        return walks;
    }

    public void setWalks(boolean walks) {
        this.walks = walks;
    }
}
```



做好准备工作之后开始下面的测试。

### 类名

获取类名。

```java
/**
 * simpleName为Goat，
 * name为com.lou.reflect.test.Goat
 * cannocalName为com.lou.reflect.test.Goat
 */
@Test
public void givenObjectThenGetNameTest() {
    Object goat = new Goat("山羊");
    Class<?> goatClazz = goat.getClass();

    Assert.assertEquals("Goat", goatClazz.getSimpleName());
    Assert.assertEquals("com.lou.reflect.test.Goat", goatClazz.getName());
    Assert.assertEquals("com.lou.reflect.test.Goat",goatClazz.getCanonicalName());
}
```

### 类修饰符

```java
/**
 * 通过获取类的modifier来判断类的特性
 * @throws Exception
 */
@Test
public void givenObjectThenGetModifiersTest() throws Exception {
    Class<?> animalClazz = Class.forName("com.lou.reflect.test.Animal");
    Class<?> goatClazz = Class.forName("com.lou.reflect.test.Goat");
    //获取两个类的修饰符
    int animalModifiers = animalClazz.getModifiers();
    int goatModifiers = goatClazz.getModifiers();
    //判断是否是公有
    Assert.assertTrue(Modifier.isPublic(goatModifiers));
    //判断是否是抽象
    Assert.assertTrue(Modifier.isAbstract(animalModifiers));
    //判断是否为公有
    Assert.assertTrue(Modifier.isPublic(animalModifiers));
}
```

### 包信息

```java
/**
 * 获取包信息
 */
@Test
public void givenClassThenGetPackageNameTest() {
    Goat goat = new Goat("山羊");
    Package goatPackage = goat.getClass().getPackage();
    Assert.assertEquals("com.lou.reflect.test", goatPackage.getName());
}
```

### 父类信息

```java
/**
 * 获取父类信息
 */
@Test
public void givenClassThenGetSupperClassTest() {
    Goat goat = new Goat("山羊");
    String str = "hello";

    Class<?> goatSupperClazz = goat.getClass().getSuperclass();
    Class<?> strSupperClazz = str.getClass().getSuperclass();

    Assert.assertEquals("com.lou.reflect.test.Animal",goatSupperClazz.getName());
    Assert.assertEquals("java.lang.Object",strSupperClazz.getName());
}
```

### 实现的接口

```java
/**
 * 获取实现的接口信息
 *
 * @throws Exception
 */
@Test
public void givenClassThenGetImpMethodsTest() throws Exception {

    Class<?> goatClazz = Class.forName("com.lou.reflect.test.Goat");
    Class<?> animalClazz = Class.forName("com.lou.reflect.test.Animal");
    Class<?>[] goatInterfaces = goatClazz.getInterfaces();
    Class<?>[] animalInterfaces = animalClazz.getInterfaces();

    //实现的接口，都是1个，虽然goat的父类实现了一个，goat自己也实现了一个。
    //只能获取用implements显式实现的接口。如果要全部就只能递归了。
    Assert.assertEquals(1, goatInterfaces.length);
    Assert.assertEquals(1, animalInterfaces.length);

    Assert.assertEquals("Locomotion", goatInterfaces[0].getSimpleName());
    Assert.assertEquals("Eating", animalInterfaces[0].getSimpleName());

}
```

### 构造函数，方法，字段

```java
/**
 * 构造函数，方法，字段
 */
@Test
public void givenClassThenGetConstructorMethodField() throws Exception {
    Class<?> goatClazz = Class.forName("com.lou.reflect.test.Goat");
    Class<?> animalClazz = Class.forName("com.lou.reflect.test.Animal");
    Constructor<?>[] goatCtors = goatClazz.getConstructors();
    Assert.assertEquals(1, goatCtors.length);
    Field[] animalFields = animalClazz.getDeclaredFields();
    //一个静态的CATEGORY，一个name，所以是2个。
    Assert.assertEquals(2, animalFields.length);
    //3个。这里获取到的是显式申明的方法，不包括那些从object继承下来的
    Method[] animalMethods = animalClazz.getDeclaredMethods();
    Assert.assertEquals(3, animalMethods.length);
    //getName,setName,getSound,
    List<String> methodNames = Arrays.stream(animalMethods).map(method -> method.getName()).collect(Collectors.toList());
    Assert.assertTrue(methodNames.containsAll(Arrays.asList("getName", "setName", "getSound")));
}
```

### 获取构造函数然后动态调用

```java
/**                                                                                   
 * 获取bird的3个构造函数，然后分别调用~。                                                             
 * @throws Exception                                                                  
 */                                                                                   
@Test                                                                                 
public void getConstructorThenCreateInstance() throws Exception {                     
    Class<?> birdClazz = Class.forName("com.lou.reflect.test.Bird");                  
    //获取无参的构造函数                                                                       
    Constructor<?> birdCtro1 = birdClazz.getConstructor();                            
    //获取有一个String类型的参数的构造函数                                                           
    Constructor<?> birdCtro2 = birdClazz.getConstructor(String.class);                
    //获取有一个String类型，一个boolean类型参数的构造函数                                                
    Constructor<?> birdCtro3 = birdClazz.getConstructor(String.class, boolean.class); 
    //调用无参的                                                                           
    Bird bird1 = (Bird) birdCtro1.newInstance();                                      
    Assert.assertEquals("鸟", bird1.getName());                                        
    //调用有一个参数的                                                                        
    Bird bird2 = (Bird) birdCtro2.newInstance("bird2");                               
    Assert.assertEquals("bird2", bird2.getName());                                    
    //调用两个参数的构造函数                                                                     
    Bird bird3 = (Bird) birdCtro3.newInstance("bird3", true);                         
    Assert.assertEquals("bird3", bird3.getName());                                    
    Assert.assertEquals(true, bird3.isWalks());                                       
}                                                                                     
```

### 运行期间修改field值，调用method

```java
/**
 * 通过获取name字段，然后动态修改。 
 * 通过动态调用setName方法，修改name的值 
 * @throws Exception
 */
@Test  
public void givenClassThenModifyFields() throws Exception {
    Class<?> birdClazz = Class.forName("com.lou.reflect.test.Bird");     
    Bird bird =(Bird) birdClazz.newInstance();   
    //定义在父类
    Field nameField = birdClazz.getSuperclass().getDeclaredField("name");                       
    //先设置为可访问                                                                              
    nameField.setAccessible(true);                                                             
    nameField.set(bird,"一只bird");                                                             
    Assert.assertEquals("一只bird",bird.getName());
    Method setNameMethod = birdClazz.getSuperclass().getDeclaredMethod("setName", String.class);       
    setNameMethod.invoke(bird,"又一只"); 
    Assert.assertEquals("又一只",bird.getName());                                                         
}                                                                                                      
```



