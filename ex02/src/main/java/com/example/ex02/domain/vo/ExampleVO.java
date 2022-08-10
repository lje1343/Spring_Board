package com.example.ex02.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component // spring 선언
@Data // getter setter
public class ExampleVO {
    private String name;
    private int age;
}
