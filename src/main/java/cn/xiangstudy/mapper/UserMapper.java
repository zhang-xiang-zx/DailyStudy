package cn.xiangstudy.mapper;

import cn.xiangstudy.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhangxiang
 * @date 2024-10-30 15:00
 */
@Mapper
public interface UserMapper {

    User gName(User user);
}
