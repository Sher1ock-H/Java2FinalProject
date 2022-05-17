package com.www.util;

import java.io.IOException;

public class getHottest { //获取最热门的开源项目
    public static void main(String[] args) throws IOException {
        getJava gj = new getJava();
        for (int i = 2010; i <= 2019; i++) {
            gj.writeJava(i);
        }
    }
}
