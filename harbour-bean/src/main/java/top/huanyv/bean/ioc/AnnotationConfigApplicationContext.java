package top.huanyv.bean.ioc;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext {

    private final AnnotationBeanDefinitionReader reader;

    public AnnotationConfigApplicationContext(String... scanPackages) {
        super();
        reader = new AnnotationBeanDefinitionReader(getBeanDefinitionRegistry());
        reader.read(scanPackages);
        initialization();
    }

}

