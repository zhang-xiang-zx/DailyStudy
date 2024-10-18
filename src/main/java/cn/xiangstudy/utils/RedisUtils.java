package cn.xiangstudy.utils;

import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangxiang
 * @date 2024-10-17 15:42
 */
@Component
public class RedisUtils {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * @description: redisson添加值
     * @author: zhangxiang
     * @date: 2024/10/17 17:56
     * @param: [key, value]
     * @return: boolean
     */
    public boolean setRedisValue(String key, Object value) {
        redissonClient.getBucket(key).set(value);
        return true;
    }

    /**
     * @description: redisson获取值
     * @author: zhangxiang
     * @date: 2024/10/18 10:15
     * @param: [key]
     * @return: java.lang.Object
     */
    public Object getRedisValue(String key) {
        return redissonClient.getBucket(key).get();
    }

    /**
     * @description: redisson添加列表
     * @author: zhangxiang
     * @date: 2024/10/18 10:16
     * @param: [list, key]
     * @return: java.lang.Boolean
     */
    public Boolean setRedisList(List<?> list, String key) {
        RList<Object> zxList = redissonClient.getList(key);
        zxList.addAll(list);
        return true;
    }

    /**
     * @description: redisson获取列表
     * @author: zhangxiang
     * @date: 2024/10/18 10:17
     * @param: [key]
     * @return: java.util.List<java.lang.Object>
     */
    public List<Object> getRedisList(String key) {
        RList<Object> list = redissonClient.getList(key);
        System.out.println(list);
        return list;
    }

}

