package com.www.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetHot {
    public InputStream get(int page) throws IOException { //获取最热门的inputStream
        String website = "https://api.github.com/search/repositories?q=language:java+created:%3E2010&sort=watchers&per_page=100&page=" + page;
        URL url = new URL(website);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getInputStream();
    }
}
