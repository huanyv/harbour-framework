package top.huanyv.start.loader.web;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.ioc.ApplicationContext;
import top.huanyv.bean.utils.BeanFactoryUtil;
import top.huanyv.start.anntation.Conditional;
import top.huanyv.start.config.AppArguments;
import top.huanyv.start.loader.ApplicationLoader;
import top.huanyv.start.loader.Condition;
import top.huanyv.webmvc.security.SessionStorageStrategy;
import top.huanyv.webmvc.security.StorageStrategy;
import top.huanyv.webmvc.security.SubjectHolder;
import top.huanyv.webmvc.security.digest.BCryptPasswordDigester;
import top.huanyv.webmvc.security.digest.PasswordDigester;

/**
 * @author huanyv
 * @date 2023/4/9 17:44
 */
public class WebSecurityStartLoader implements ApplicationLoader {

    @Bean
    public SubjectHolder subjectHolder() {
        return new SubjectHolder();
    }

    @Bean
    @Conditional(ConditionOnMissingStorageStrategyBean.class)
    public StorageStrategy storageStrategy() {
        return new SessionStorageStrategy();
    }

    @Bean
    @Conditional(ConditionOnMissingPasswordDigesterBean.class)
    public PasswordDigester passwordDigester() {
        return new BCryptPasswordDigester();
    }

    public static class ConditionOnMissingStorageStrategyBean implements Condition {
        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, StorageStrategy.class);
        }
    }

    public static class ConditionOnMissingPasswordDigesterBean implements Condition {
        @Override
        public boolean matchers(ApplicationContext applicationContext, AppArguments appArguments) {
            return BeanFactoryUtil.isNotPresent(applicationContext, PasswordDigester.class);
        }
    }

}
