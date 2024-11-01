package cn.xiangstudy.service.impl;

import cn.xiangstudy.config.annotation.AopTest;
import cn.xiangstudy.mapper.UserMapper;
import cn.xiangstudy.pojo.User;
import cn.xiangstudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangxiang
 * @date 2024-10-31 16:39
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @AopTest
    @Override
    public User getUserName(User user) {
        System.out.println("即将进到："+user.getParams());
        return userMapper.gName(user);
    }
}

