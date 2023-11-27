package top.huanyv.bean.ioc;

import top.huanyv.bean.exception.BeansException;

public interface InitializingBean {
	void afterPropertiesSet() throws BeansException;
}
