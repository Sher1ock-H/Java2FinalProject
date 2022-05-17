package com.www.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class getJava {
    public void writeJava(int year) throws IOException {
        String website = "https://api.github.com/search/repositories?q=language:java+created:"
                + year + ".." + (year+1)
                + "&sort=watchers&per_page=100";
        URL url = new URL(website);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(huc.getInputStream()));
        String infoString = in.readLine();
        in.close();
        JSONObject jo1 = JSONObject.fromObject(infoString);
        JSONArray ja = jo1.getJSONArray("items");
        int len = ja.size();
        BufferedWriter nameWriter = new BufferedWriter(new FileWriter("src/main/java/com/www/dataSet/nameSet" + year + ".txt"));
        BufferedWriter desWriter = new BufferedWriter(new FileWriter("src/main/java/com/www/dataSet/descriptionSet" + year + ".txt"));
        for (int i = 0; i < len; i++) {
            JSONObject jo2 = ja.getJSONObject(i);
            nameWriter.write(jo2.get("name").toString() + '\n');
            desWriter.write(jo2.get("description").toString() + '\n');
        }
        nameWriter.close();
        desWriter.close();
    }
}