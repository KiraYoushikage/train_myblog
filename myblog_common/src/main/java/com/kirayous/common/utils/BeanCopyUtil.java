package com.kirayous.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.common.utils
 * @date 2021/9/9 19:58
 */
public class BeanCopyUtil {

    /**
     * 根据现有对象的属性创建目标对象，并赋值
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     * @throws Exception
     *
     * source把共同属性赋值给target生成的对象
     */
    public static <T> T copyObject(Object source, Class<T> target) {


        T temp = null;
        try {
            temp = target.newInstance();
            if (null != source) {
                org.springframework.beans.BeanUtils.copyProperties(source, temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 拷贝集合
     *
     * @param source
     * @param target
     * @param <T>
     * @param <S>
     * @return
     */
    public static <T, S> List<T> copyList(List<S> source, Class<T> target) {
        List<T> list = new ArrayList<>();
        if (null != source && source.size() > 0) {
            for (Object obj : source) {
                list.add(BeanCopyUtil.copyObject(obj, target));
            }
        }
        return list;
    }
}
