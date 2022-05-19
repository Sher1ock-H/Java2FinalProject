package com.Java2Project.util;

import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GetHottest { //获取最热门的开源项目
    public static void main(String[] args) throws IOException {
        StringBuilder name = new StringBuilder();
        StringBuilder describe = new StringBuilder();
        StringBuilder time = new StringBuilder();
        StringBuilder watch = new StringBuilder();
        StringBuilder url = new StringBuilder();
        GetHot gh = new GetHot();
        ItemArray itemArray = new ItemArray();
        for (int i = 1; i <= 10; i++) {
            BufferedReader in = new BufferedReader(new InputStreamReader(gh.get(i)));
            String info = in.readLine();
            JSONObject jo1 = JSONObject.fromObject(info);
            System.out.println(jo1.get("incomplete_results"));
            JSONArray ja = jo1.getJSONArray("items");
            int len = ja.size();
            for (int j = 0; j < len; j++) {
                JSONObject jo2 = ja.getJSONObject(j);
                String itemName = jo2.get("name").toString();
                String itemDes = jo2.get("description").toString();
                String itemTime = jo2.get("created_at").toString();
                String itemUrl = jo2.get("html_url").toString();
                int itemWatcher = jo2.getInt("watchers");
                name.append(itemName).append('\n');
                describe.append(itemDes).append('\n');
                time.append(itemTime).append('\n');
                watch.append(itemWatcher).append('\n');
                url.append(itemUrl).append('\n');
                int itemYear = Integer.parseInt(itemTime.split("-")[0]);
                int itemMonth = Integer.parseInt(itemTime.substring(5,7));
                int itemDay = Integer.parseInt(itemTime.substring(8,10));
                itemArray.addItem(new Item(itemName, itemDes, itemYear, itemMonth, itemDay, itemWatcher, j));
            }
            in.close();
        }
        BufferedWriter nameOut = new BufferedWriter(new FileWriter("src/main/java/com/Java2Project/dataSet/name.txt"));
        BufferedWriter desOut = new BufferedWriter(new FileWriter("src/main/java/com/Java2Project/dataSet/description.txt"));
        BufferedWriter timeOut = new BufferedWriter(new FileWriter("src/main/java/com/Java2Project/dataSet/time.txt"));
        BufferedWriter watchOut = new BufferedWriter(new FileWriter("src/main/java/com/Java2Project/dataSet/watcher.txt"));
        BufferedWriter urlOut = new BufferedWriter(new FileWriter("src/main/java/com/Java2Project/dataSet/url.txt"));
        nameOut.write(name.toString());
        desOut.write(describe.toString());
        timeOut.write(time.toString());
        watchOut.write(watch.toString());
        urlOut.write(url.toString());
        nameOut.close();
        desOut.close();
        timeOut.close();
        watchOut.close();
        urlOut.close();
        itemArray.sort();
        String JSON = new Gson().toJson(itemArray);
        BufferedWriter json = new BufferedWriter(new FileWriter("src/main/java/com/Java2Project/dataSet/info.json"));
        json.write(JSON);
        json.close();
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
        public int id;

        public Item(String name, String description, int year, int month, int day, int watchers, int id) {
            this.name = name;
            this.description = description;
            this.year = year;
            this.month = month;
            this.day = day;
            this.watchers = watchers;
            this.id = id;
        }
    }
}
