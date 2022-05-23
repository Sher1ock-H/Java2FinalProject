package com.Java2Project.util;

import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class GetValue {
    String[] name = new String[1000];
    int[] year = new int[1000];
    int[] month = new int[1000];
    int[] day = new int[1000];
    int[] watcher = new int[1000];
    String[] des = new String[1000];
    String[] urls = new String[1000];
    UArray pageArray = new UArray();

    public GetValue() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/info.json"));
            JSONObject jo1 = JSONObject.fromObject(in.readLine());
            JSONArray ja = jo1.getJSONArray("items");
            for (int i = 0; i < 1000; i++) {
                JSONObject jo2 = ja.getJSONObject(i);
                name[i] = jo2.getString("name");
                watcher[i] = jo2.getInt("watchers");
                des[i] = jo2.getString("description");
                urls[i] = jo2.getString("url");
                year[i] = jo2.getInt("year");
                month[i] = jo2.getInt("month");
                day[i] = jo2.getInt("day");
            }
            in.close();
            for (int i = 0; i < 1000; i++) {
                String time = String.valueOf(year[i]) + '-' + month[i] + '-' + day[i];
                pageArray.addItem(new UItem(name[i], des[i], time, watcher[i], urls[i], i));
            }
            pageArray.sort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAll() {
        YArray yArray = new YArray();
        for (int i = 0; i < 1000; i++) {
            yArray.addItem(new YItem(name[i], year[i]));
        }
        return new Gson().toJson(yArray);
    }

    public String getYear(int y) {
        GetHottest.ItemArray itemArray = new GetHottest.ItemArray();
        for (int i = 0; i < 1000; i++) {
            if (year[i] == y)
                itemArray.addItem(new GetHottest.Item(name[i], des[i], year[i], month[i], day[i], watcher[i], urls[i], i));
        }
        itemArray.sort();
        return new Gson().toJson(itemArray);
    }

    public String getMonth(int y, int m) {
        GetHottest.ItemArray itemArray = new GetHottest.ItemArray();
        for (int i = 0; i < 1000; i++) {
            if (year[i] == y && month[i] == m)
                itemArray.addItem(new GetHottest.Item(name[i], des[i], year[i], month[i], day[i], watcher[i], urls[i], i));
        }
        itemArray.sort();
        return new Gson().toJson(itemArray);
    }

    public String getPage(int pageNum) {
        UArray itemArray = new UArray();
        for (int i = (pageNum - 1) * 100; i < pageNum * 100; i++) {
            itemArray.addItem(pageArray.items.get(i));
        }
        return new Gson().toJson(itemArray);
    }

    public String getWords(){
        String wordFrequency = null;
        try{
            WordAnalyse wa = new WordAnalyse();
            wa.analyse();
            BufferedReader wordIn = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/cloud.json"));
            wordFrequency = wordIn.readLine();
            wordIn.close();
            System.out.println(wordFrequency);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordFrequency;
    }

    public static class YArray {
        public ArrayList<YItem> items;

        public YArray() {
            items = new ArrayList<>();
        }

        public void addItem(YItem item) {
            this.items.add(item);
        }
    }

    public String search(String s) {
        UArray itemArray = new UArray();
        String lowers = s.toLowerCase(Locale.ROOT);
        for (int i = 0; i < 1000; i++) {
            if (name[i].toLowerCase(Locale.ROOT).contains(lowers)) {
                itemArray.addItem(pageArray.items.get(i));
            }
        }
        return new Gson().toJson(itemArray);
    }

    public static class YItem {
        public String name;
        public int year;

        public YItem(String name, int year) {
            this.name = name;
            this.year = year;
        }
    }

    public static class UArray {
        public ArrayList<UItem> items;

        public UArray() {
            items = new ArrayList<>();
        }

        public void addItem(UItem item) {
            this.items.add(item);
        }

        public void sort() {
            items.sort(Comparator.comparingInt(i -> -i.watchers));
        }
    }

    public static class UItem {
        public String name;
        public String des;
        public String time;
        public String url;
        public int watchers;
        public int id;

        public UItem(String name, String des, String time, int watchers, String url, int id) {
            this.name = name;
            this.des = des;
            this.time = time;
            this.watchers = watchers;
            this.url = url;
            this.id = id;
        }
    }
}
