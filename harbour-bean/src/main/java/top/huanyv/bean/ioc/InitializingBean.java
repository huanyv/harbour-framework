package top.huanyv.bean.ioc;

public interface InitializingBean {
	void afterPropertiesSet() throws Exception;
}
