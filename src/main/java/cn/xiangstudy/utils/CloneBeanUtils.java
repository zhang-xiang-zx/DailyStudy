package cn.xiangstudy.utils;

import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhangxiang
 * @date 2024-10-16 9:26
 */
public class CloneBeanUtils {

    private static Map<String, BeanCopier> beanCopierMap = new HashMap<>();

    /**
     * @description: 复制相同类字段数据
     * @author: zhangxiang
     * @date: 2024/10/16 10:30
     * @param: [source, aClass]
     * @return: T
     */
    public static <T> T cloneBean(Object source, Class<T> aClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = aClass.newInstance();
            copyProperties(source, target);
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @description: 复制相同列表数据
     * @author: zhangxiang
     * @date: 2024/10/16 10:37
     * @param: [sourceList, aClass]
     * @return: java.util.List<T>
     */
    public static <T> List<T> cloneList(List<?> sourceList, Class<T> aClass){
        if(InspectUtils.isNull(sourceList)){
            return null;
        }
        List<T> resultList = new ArrayList<>();
        for(Object obj : sourceList){
            resultList.add(cloneBean(obj,aClass));
        }
        return resultList;
    }

    private static void copyProperties(Object source, Object target) {
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier;
        if (!beanCopierMap.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.put(beanKey, copier);
        } else {
            copier = beanCopierMap.get(beanKey);
        }
        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> one, Class<?> two) {
        return one.toString() + two.toString();
    }

}

