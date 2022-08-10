package com.example.ex02.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component // spring 선언
@Data // getter setter
public class WorkVO {
    private String name;
    private String infoTime;
}
