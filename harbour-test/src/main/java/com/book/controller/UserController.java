package com.book.controller;

import com.book.pojo.User;
import com.book.service.UserService;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;
import top.huanyv.webmvc.annotation.Post;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.annotation.argument.Form;
import top.huanyv.webmvc.core.request.Model;

import javax.servlet.http.HttpSession;

/**
 * @author huanyv
 * @date 2022/11/17 17:27
 */
@Component
@Route("/admin/user")
public class UserController {

    @Inject
    private UserService userService;

    @Post("/login")
    public String login(@Form User user, HttpSession session, Model model) {
        boolean b = userService.login(user.getUsername(), user.getPassword());
        if (b) {
            session.setAttribute("username", user.getUsername());
            return "redirect:/";
        }
        model.add("msg", "登陆失败！");
        return "forward:/login";
    }

}
