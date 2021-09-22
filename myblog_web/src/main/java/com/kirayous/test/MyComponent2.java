package com.kirayous.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.test
 * @date 2021/7/20 0:48
 */
public class MyComponent2{

    public MyComponent2(MyComponent myComponent)
    {
        myComponent.test();
    }
    public MyComponent2(){

        System.out.println("无参数构造方法");
    }
}
