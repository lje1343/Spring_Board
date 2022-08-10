package com.example.ex00.dependency;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component // 해당 객체를 Spring에서 관리하도록 설정
@Data
@RequiredArgsConstructor // 생성자 주입
public class Restaurant {
    private final Chef chef;
}
