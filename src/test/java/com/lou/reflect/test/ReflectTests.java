package com.lou.reflect.test;

import com.lou.reflect.test.model.Person;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectTests {
    /**
     * 获取所有字段。
     * 然后设置字段的值
     */
    @Test
    public void containAllFields() throws Exception {
        Object p = new Person();
        Field[] declaredFields = p.getClass().getDeclaredFields();
        List<String> actualFields = Arrays.stream(declaredFields).map(field -> field.getName()).collect(Collectors.toList());
        List<String> exceptFields = new ArrayList<>();
        exceptFields.add("age");
        exceptFields.add("name");
        Assert.assertTrue(exceptFields.containsAll(actualFields));
    }

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
        Assert.assertEquals("com.lou.reflect.test.Goat", goatClazz.getCanonicalName());
    }

    /**
     * 通过获取类的modifier来判断类的特性
     *
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

    /**
     * 获取包信息
     */
    @Test
    public void givenClassThenGetPackageNameTest() {
        Goat goat = new Goat("山羊");
        Package goatPackage = goat.getClass().getPackage();
        Assert.assertEquals("com.lou.reflect.test", goatPackage.getName());
    }

    /**
     * 获取父类信息
     */
    @Test
    public void givenClassThenGetSupperClassTest() {
        Goat goat = new Goat("山羊");
        String str = "hello";

        Class<?> goatSupperClazz = goat.getClass().getSuperclass();
        Class<?> strSupperClazz = str.getClass().getSuperclass();

        Assert.assertEquals("com.lou.reflect.test.Animal", goatSupperClazz.getName());
        Assert.assertEquals("java.lang.Object", strSupperClazz.getName());
    }

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
        //getName,setName,getSound
        List<String> methodNames = Arrays.stream(animalMethods).map(method -> method.getName()).collect(Collectors.toList());
        Assert.assertTrue(methodNames.containsAll(Arrays.asList("getName", "setName", "getSound")));
    }

    /**
     * 获取bird的3个构造函数，然后分别调用~。
     *
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

    /**
     * 通过获取name字段，然后动态修改。
     * 通过动态调用setName方法，修改name的值
     *
     * @throws Exception
     */
    @Test
    public void givenClassThenModifyFields() throws Exception {
        Class<?> birdClazz = Class.forName("com.lou.reflect.test.Bird");
        Bird bird = (Bird) birdClazz.newInstance();
        Field nameField = birdClazz.getSuperclass().getDeclaredField("name");
        //先设置为可访问
        nameField.setAccessible(true);
        nameField.set(bird, "一只bird");
        Assert.assertEquals("一只bird", bird.getName());

        Method setNameMethod = birdClazz.getSuperclass().getDeclaredMethod("setName", String.class);
        setNameMethod.invoke(bird, "又一只");
        Assert.assertEquals("又一只", bird.getName());
    }
}
