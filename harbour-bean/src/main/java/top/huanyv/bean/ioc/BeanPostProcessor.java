package top.huanyv.bean.ioc;

import top.huanyv.bean.exception.BeansException;

public interface BeanPostProcessor {

	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
