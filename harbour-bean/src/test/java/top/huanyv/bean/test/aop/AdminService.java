package top.huanyv.bean.test.aop;

import top.huanyv.bean.annotation.Bean;
import top.huanyv.bean.aop.Aop;
import top.huanyv.bean.test.entity.User;

/**
 * @author huanyv
 * @date 2022/11/18 14:39
 */
@Bean
@Aop({LogAspect.class, LogAspect2.class})
public class AdminService {

    @Aop(LogAspect.class)
    public User getUser() {
        System.out.println("方法执行");
        return new User(1, "admin", "123", "男", "111@qq.com");
    }

}
