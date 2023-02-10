package top.huanyv.bean.ioc;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    private final AnnotationBeanDefinitionReader reader;

    public AnnotationConfigApplicationContext(String... scanPackages) {
        super();
        reader = new AnnotationBeanDefinitionReader(this.beanDefinitionRegistry);
        reader.read(scanPackages);
        refresh();
    }

}

