package com.www.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class getHottest { //获取最热门的开源项目
    public static void main(String[] args) throws IOException {
        StringBuilder name = new StringBuilder();
        StringBuilder describe = new StringBuilder();
        StringBuilder time = new StringBuilder();
        StringBuilder watch = new StringBuilder();
        getHot gh = new getHot();
        for(int i=1;i<=10;i++){
            BufferedReader in = new BufferedReader(new InputStreamReader(gh.get(i)));
            String info = in.readLine();
            JSONObject jo1 = JSONObject.fromObject(info);
            JSONArray ja = jo1.getJSONArray("items");
            int len = ja.size();
            for(int j=0;j<len;j++){
                JSONObject jo2 = ja.getJSONObject(j);
                name.append(jo2.get("name").toString()).append('\n');
                describe.append(jo2.get("description").toString()).append('\n');
                time.append(jo2.get("created_at").toString()).append('\n');
                watch.append(jo2.get("watchers").toString()).append('\n');
            }
            in.close();
        }
        BufferedWriter nameOut = new BufferedWriter(new FileWriter("src/main/java/com/www/dataSet/name.txt"));
        BufferedWriter desOut = new BufferedWriter(new FileWriter("src/main/java/com/www/dataSet/description.txt"));
        BufferedWriter timeOut = new BufferedWriter(new FileWriter("src/main/java/com/www/dataSet/time.txt"));
        BufferedWriter watchOut = new BufferedWriter(new FileWriter("src/main/java/com/www/dataSet/watcher.txt"));
        nameOut.write(name.toString());
        desOut.write(describe.toString());
        timeOut.write(time.toString());
        watchOut.write(watch.toString());
        nameOut.close();
        desOut.close();
        timeOut.close();
        watchOut.close();
    }

    public static class Array {
        public ArrayList<Item> items;

        public Array() {
            items = new ArrayList<>();
        }

        public void addItem (Item item) {
            this.items.add(item);
        }
    }

    public static class Item {
        public String xxx;

        public Item(String xxx) {
            this.xxx = xxx;
        }
    }
}
