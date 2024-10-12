package cn.xiangstudy.utils;

import cn.xiangstudy.config.annotation.NotNull;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangxiang
 * @date 2024-09-05 17:54
 */
public class InspectUtils {

    /**
     * @description: 判断是否为空
     * @author: zhangxiang
     * @date: 2024/9/5 17:59
     * @param: [obj]
     * @return: boolean
     */
    public static boolean isNull(Object obj){
        boolean isNull = false;
        if(obj == null){
            isNull = true;
            return isNull;
        }

        if(obj instanceof String){
            String str = (String) obj;
            if(str.isEmpty()){
                isNull = true;
            }
        }
        return isNull;
    }

    /**
     * @description: 判断是否不为空
     * @author: zhangxiang
     * @date: 2024/9/9 10:13
     * @param: [obj]
     * @return: boolean
     */
    public static boolean notNull(Object obj){
        return !isNull(obj);
    }


    /**
     * @description: 判断列表是否不为空
     * @author: zhangxiang
     * @date: 2024/9/9 10:30
     * @param: [list]
     * @return: boolean
     */
    public static <T> boolean notEmpty(List<T> list){
        if(list == null){
            return false;
        }
        return !list.isEmpty();
    }

    /**
     * @description: 判断对象中带有不能为空的注解的字段是否不为空
     * @author: zhangxiang
     * @date: 2024/9/9 14:02
     * @param: [obj, objClass]
     * @return: boolean
     */
    public static <T> boolean objNotNull(String obj, Class<T> objClass){
        boolean notNull = true;
        JSONObject jsonObject = JSON.parseObject(obj);
        Field[] declaredFields = objClass.getDeclaredFields();
        List<String> collect = Arrays.stream(declaredFields).filter(single -> {
            boolean isTrue = false;
            NotNull annotation = single.getAnnotation(NotNull.class);
            if (notNull(annotation)) {
                isTrue = true;
            }
            return isTrue;
        }).map(Field::getName).collect(Collectors.toList());
        for(String fieldName : collect){
            Object o = jsonObject.get(fieldName);
            if(!notNull(o)){
                notNull = false;
                break;
            }
        }

        return notNull;
    }

    /**
     * @description: 判断字符串中是否包含另一个字符串
     * @author: zhangxiang
     * @date: 2024/10/12 10:42
     * @param: [originStr, otherStr]
     * @return: boolean
     */
    public static boolean strIsHave(String originStr, String otherStr){
        if(isNull(originStr) && isNull(otherStr)){
            return false;
        }
        return originStr.contains(otherStr);
    }

}

