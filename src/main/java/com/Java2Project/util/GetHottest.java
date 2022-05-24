package com.Java2Project.util;

import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class GetHottest { //获取最热门的开源项目
    public static void main(String[] args) throws IOException {
        ItemArray itemArray = new ItemArray();
        for (int i = 1; i <= 10; i++) {
            String website = "https://api.github.com/search/repositories?q=language:java+created:>2010+stars:>800&sort=watchers&per_page=100&page=" + i;
            URL url = new URL(website);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            String info = in.readLine();
            JSONObject jo1 = JSONObject.fromObject(info);
            JSONArray ja = jo1.getJSONArray("items");
            int len = ja.size();
            for (int j = 0; j < len; j++) {
                JSONObject jo2 = ja.getJSONObject(j);
                String itemName = jo2.get("name").toString();
                String itemDes = jo2.get("description").toString();
                String itemTime = jo2.get("created_at").toString();
                String itemUrl = jo2.get("html_url").toString();
                int itemWatcher = jo2.getInt("watchers");
                int itemYear = Integer.parseInt(itemTime.split("-")[0]);
                int itemMonth = Integer.parseInt(itemTime.substring(5,7));
                int itemDay = Integer.parseInt(itemTime.substring(8,10));
                itemArray.addItem(new Item(itemName, itemDes, itemYear, itemMonth, itemDay, itemWatcher, itemUrl, j));
            }
            in.close();
        }
        itemArray.sort();
        String JSON = new Gson().toJson(itemArray);
        BufferedWriter json = new BufferedWriter(new FileWriter("src/main/java/com/Java2Project/dataSet/info.json"));
        json.write(JSON);
        json.close();
        new GetValue();
    }

    public static class ItemArray {
        public ArrayList<Item> items;

        public ItemArray() {
            items = new ArrayList<>();
        }

        public void addItem(Item item) {
            this.items.add(item);
        }

        public void sort() {
            items.sort(Comparator.comparingInt(i -> -i.watchers));
            //items.sort((a, b) -> a.watchers - b.watchers);
        }
    }

    public static class Item {
        public String name;
        public String description;
        public int year;
        public int month;
        public int day;
        public int watchers;
        public String url;
        public int id;

        public Item(String name, String description, int year, int month, int day, int watchers, String url, int id) {
            this.name = name;
            this.description = description;
            this.year = year;
            this.month = month;
            this.day = day;
            this.watchers = watchers;
            this.url = url;
            this.id = id;
        }
    }
}
