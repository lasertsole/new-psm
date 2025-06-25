package com.psm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DecimalFormat;

@SpringBootTest
public class test {
    @Test
    public void test1(){
        DecimalFormat df = new DecimalFormat("#,###.######");

        System.out.println(df.format(123456789.123456789));
    }
}
