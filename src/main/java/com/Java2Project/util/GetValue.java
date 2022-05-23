package com.Java2Project.util;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GetValue {
    String[] name = new String[1000];
    String[] time = new String[1000];
    int[] watcher = new int[1000];
    String[] des = new String[1000];
    String[] urls = new String[1000];
    UArray pageArray = new UArray();
    String wordFrequency;

    public GetValue(){
        try {
            BufferedReader nameIn = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/name.txt"));
            BufferedReader timeIn = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/time.txt"));
            BufferedReader watcherIn = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/watcher.txt"));
            BufferedReader desIn = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/description.txt"));
            BufferedReader urlIn = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/url.txt"));
            BufferedReader wordIn = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/cloud.json"));
            for (int i = 0; i < 1000; i++) {
                name[i] = nameIn.readLine();
                time[i] = timeIn.readLine();
                watcher[i] = Integer.parseInt(watcherIn.readLine());
                des[i] = desIn.readLine();
                urls[i] = urlIn.readLine();
            }
            wordFrequency = wordIn.readLine();
            nameIn.close();
            timeIn.close();
            watcherIn.close();
            desIn.close();
            urlIn.close();
            wordIn.close();
            for (int i = 0; i < 1000; i++) {
                pageArray.addItem(new UItem(name[i], des[i], time[i], watcher[i], urls[i], i));
            }
            pageArray.sort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAll() {
        YArray yArray = new YArray();
        for (int i = 0; i < 1000; i++) {
            int itemYear = Integer.parseInt(time[i].split("-")[0]);
            yArray.addItem(new YItem(name[i], itemYear));
        }
        return new Gson().toJson(yArray);
    }

    public String getYear(int year) {
        GetHottest.ItemArray itemArray = new GetHottest.ItemArray();
        for (int i = 0; i < 1000; i++) {
            int itemYear = Integer.parseInt(time[i].split("-")[0]);
            int itemMonth = Integer.parseInt(time[i].substring(5, 7));
            int itemDay = Integer.parseInt(time[i].substring(8, 10));
            if (itemYear == year)
                itemArray.addItem(new GetHottest.Item(name[i], des[i], itemYear, itemMonth, itemDay, watcher[i], i));
        }
        itemArray.sort();
        return new Gson().toJson(itemArray);
    }

    public String getMonth(int year, int month) {
        GetHottest.ItemArray itemArray = new GetHottest.ItemArray();
        for (int i = 0; i < 1000; i++) {
            int itemYear = Integer.parseInt(time[i].split("-")[0]);
            int itemMonth = Integer.parseInt(time[i].substring(5, 7));
            int itemDay = Integer.parseInt(time[i].substring(8, 10));
            if (itemYear == year && itemMonth == month)
                itemArray.addItem(new GetHottest.Item(name[i], des[i], itemYear, itemMonth, itemDay, watcher[i], i));
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

    public String getWords(){ return wordFrequency;}

    public static class YArray{
        public ArrayList<YItem> items;
        public YArray(){items = new ArrayList<>();}
        public void addItem(YItem item){this.items.add(item);}
    }

    public static class YItem{
        public String name;
        public int year;

        public YItem(String name, int year){
            this.name = name;
            this.year = year;
        }
    }

    public static class UArray{
        public ArrayList<UItem> items;
        public UArray(){items = new ArrayList<>();}
        public void addItem(UItem item){this.items.add(item);}

        public void sort() {
            items.sort(Comparator.comparingInt(i -> -i.watchers));
        }
    }

    public static class UItem{
        public String name;
        public String des;
        public String time;
        public String url;
        public int watchers;
        public int id;

        public UItem(String name, String des, String time, int watchers, String url, int id){
            this.name = name;
            this.des = des;
            this.time = time;
            this.watchers = watchers;
            this.url = url;
            this.id = id;
        }
    }
}
