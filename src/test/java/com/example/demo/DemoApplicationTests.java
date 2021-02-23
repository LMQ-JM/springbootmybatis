package com.example.demo;

import com.example.common.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(System.currentTimeMillis()/1000-1607577128);
        String time = DateUtils.getTime("1645985452");
        System.out.println(time);

    }

}
