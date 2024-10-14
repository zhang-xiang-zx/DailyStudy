package cn.xiangstudy.utils;

import java.math.BigDecimal;

/**
 * @author zhangxiang
 * @date 2024-10-14 16:51
 */
public class MathUtils {

    /**
     * @description: 将数字转为BigDecimal
     * @author: zhangxiang
     * @date: 2024/10/14 17:04
     * @param: [num]
     * @return: java.math.BigDecimal
     */
    public static BigDecimal getBigDecimal(Object num){
        String numStr = String.valueOf(num);
        return new BigDecimal(numStr);
    }

    /**
     * @description: 大于；第一个数是否大于第二个数
     * @author: zhangxiang
     * @date: 2024/10/14 17:00
     * @param: [numOne, numTwo]
     * @return: boolean
     */
    public static boolean daYu(BigDecimal numOne, BigDecimal numTwo){
        int i = numOne.compareTo(numTwo);
        return i > 0;
    }

    /**
     * @description: 等于；第一个数是否等于第二个数
     * @author: zhangxiang
     * @date: 2024/10/14 17:01
     * @param: [numOne, numTwo]
     * @return: boolean
     */
    public static boolean dengYu(BigDecimal numOne, BigDecimal numTwo){
        int i = numOne.compareTo(numTwo);
        return i == 0;
    }

    /**
     * @description: 小于；第一个数是否小于第二个数
     * @author: zhangxiang
     * @date: 2024/10/14 17:01
     * @param: [numOne, numTwo]
     * @return: boolean
     */
    public static boolean xiaoYu(BigDecimal numOne, BigDecimal numTwo){
        int i = numOne.compareTo(numTwo);
        return i < 0;
    }

}

