package com.example.ex00.dependency.qualifier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component // spring에 등록하기 위함
@Qualifier("laptop")
public class LapTop implements Computer{

    @Override
    public int getScreenWidth() {
        return 1280;
    }
}
