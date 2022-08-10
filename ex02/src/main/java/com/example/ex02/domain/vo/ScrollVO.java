package com.example.ex02.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
public class ScrollVO {
    private int scroll100;
    private int scroll60;
    private int scroll10;

    public ScrollVO() {
        this(10, 40, 80);
    } /* 메서드 오버로딩 -> 기본생성자를 만들어서 */
}