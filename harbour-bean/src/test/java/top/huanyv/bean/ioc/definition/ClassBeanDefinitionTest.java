package top.huanyv.bean.ioc.definition;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassBeanDefinitionTest {

    @Test
    public void newInstance() {
        ClassBeanDefinition beanDefinition = new ClassBeanDefinition(A.class, "123", new ArrayList<String>());
        Object o = beanDefinition.newInstance();
        System.out.println("o = " + o);
    }
}

class A {
    public A() {
    }

}