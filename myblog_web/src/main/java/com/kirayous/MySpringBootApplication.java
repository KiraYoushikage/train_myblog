package com.kirayous;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous
 * @date 2021/7/16 2:54
 */
@SpringBootApplication(scanBasePackages = "com.kirayous")
public class MySpringBootApplication {

    public static void main(String[] args) {

        SpringApplication.run(MySpringBootApplication.class);
    }
}
