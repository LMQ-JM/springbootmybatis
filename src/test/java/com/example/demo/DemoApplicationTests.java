package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        File file = new File("e://file/video/161130576161917.mp4");
        //判断文件是否存在
        if (file.exists() == true){
            System.out.println("图片存在，可执行删除操作");
            Boolean flag = false;
            flag = file.delete();
            if (flag){
                System.out.println("成功删除图片"+file.getName());
            }else {
                System.out.println("删除失败");
            }
        }else {
            System.out.println("图片不存在，终止操作");
        }
    }



}
