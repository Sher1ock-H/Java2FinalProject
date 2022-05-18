package com.www.util;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class GetValue {
    String[] name = new String[1000];
    String[] time = new String[1000];
    int[] watcher = new int[1000];
    String[] des = new String[1000];

    public GetValue() throws IOException {
        BufferedReader nameIn = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/www/dataSet/name.txt")));
        BufferedReader timeIn = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/www/dataSet/time.txt")));
        BufferedReader watcherIn = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/www/dataSet/watcher.txt")));
        BufferedReader desIn = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/www/dataSet/description.txt")));
        for (int i = 0; i < 1000; i++) {
            name[i] = nameIn.readLine();
            time[i] = timeIn.readLine();
            watcher[i] = Integer.parseInt(watcherIn.readLine());
            des[i] = desIn.readLine();
        }
    }

    public String getAll() {
        GetHottest.ItemArray itemArray = new GetHottest.ItemArray();
        for (int i = 0; i < 1000; i++) {
            int itemYear = Integer.parseInt(time[i].split("-")[0]);
            int itemMonth = Integer.parseInt(time[i].substring(5, 7));
            int itemDay = Integer.parseInt(time[i].substring(8, 10));
            itemArray.addItem(new GetHottest.Item(name[i], des[i], itemYear, itemMonth, itemDay, watcher[i], i));
        }
        return new Gson().toJson(itemArray);
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
        return new Gson().toJson(itemArray);
    }

    public String getPage(int pageNum) {
        GetHottest.ItemArray itemArray = new GetHottest.ItemArray();
        for (int i = (pageNum - 1) * 100; i <= (pageNum) * 100; i++) {
            int itemYear = Integer.parseInt(time[i].split("-")[0]);
            int itemMonth = Integer.parseInt(time[i].substring(5, 7));
            int itemDay = Integer.parseInt(time[i].substring(8, 10));
            itemArray.addItem(new GetHottest.Item(name[i], des[i], itemYear, itemMonth, itemDay, watcher[i], i));
        }
        return new Gson().toJson(itemArray);
    }
}
