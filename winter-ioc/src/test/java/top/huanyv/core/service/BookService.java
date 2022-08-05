package top.huanyv.core.service;

import top.huanyv.ioc.anno.Autowired;
import top.huanyv.ioc.anno.Component;

/**
 * @author admin
 * @date 2022/7/24 15:02
 */
//@Component
public class BookService {

    @Autowired
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
