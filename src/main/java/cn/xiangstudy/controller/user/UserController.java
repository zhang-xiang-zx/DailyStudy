package cn.xiangstudy.controller.user;

import cn.xiangstudy.pojo.User;
import cn.xiangstudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zhangxiang
 * @date 2024-10-31 16:33
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("getName")
    public User getUserName(@RequestBody User user){
        Map<String, Object> params = user.getParams();
        System.out.println("查看第一次：" +params);
        return userService.getUserName(user);
    }
}

