package top.huanyv.core.test.service.impl;

import top.huanyv.core.test.service.BookService;
import top.huanyv.core.test.service.UserService;
import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.anno.Configuration;
import top.huanyv.ioc.anno.Inject;
import top.huanyv.ioc.anno.Scope;
import top.huanyv.ioc.core.definition.BeanDefinition;

/**
 * @author huanyv
 * @date 2022/11/3 10:02
 */
@Component
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BookServiceImpl implements BookService {

    @Inject
    private UserService userService;

}
