package com.example.ex02.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component // spring 선언
@Data // getter setter
public class LoginVO {
    private String id;
    private String pw;
}
