package top.huanyv.core.service;

import top.huanyv.ioc.anno.Component;
import top.huanyv.ioc.anno.Inject;

/**
 * @author admin
 * @date 2022/7/24 15:02
 */
@Component
public class BookService {

    @Inject
    private UserService userService;


    public UserService getUserService() {
        System.out.println("userService.toString() = " + userService.toString());
        return userService;
    }


    @Override
    public String toString() {
        return "BookService";
    }
}
