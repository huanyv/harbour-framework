package top.huanyv.core.test.service.impl;

import top.huanyv.core.test.service.BookService;
import top.huanyv.core.test.service.UserService;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;

/**
 * @author huanyv
 * @date 2022/11/3 10:02
 */
@Component("bookService")
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BookServiceImpl implements BookService {

    @Inject
    private UserService userService;

}
