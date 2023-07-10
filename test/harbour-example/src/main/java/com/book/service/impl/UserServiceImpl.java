package com.book.service.impl;

import com.book.dao.UserDao;
import com.book.service.UserService;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.annotation.Inject;

/**
 * @author huanyv
 * @date 2022/11/17 17:50
 */
@Component
public class UserServiceImpl implements UserService {

    @Inject
    private UserDao userDao;

    @Override
    public boolean login(String username, String password) {
        return userDao.getUserByUsernameAndPassword(username, password) != null;
    }
}
