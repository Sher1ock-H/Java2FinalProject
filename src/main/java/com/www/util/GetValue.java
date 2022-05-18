package com.www.util;

import java.io.*;

public class GetValue {
    String[] name = new String[1000];
    String[] time = new String[1000];
    String[] watcher = new String[1000];
    String[] des = new String[1000];
    GetHottest.ItemArray itemArray = new GetHottest.ItemArray();

    public GetValue() throws IOException {
        BufferedReader nameIn = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/www/dataSet/name.txt")));
        BufferedReader timeIn = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/www/dataSet/time.txt")));
        BufferedReader watcherIn = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/www/dataSet/watcher.txt")));
        BufferedReader desIn = new BufferedReader(new InputStreamReader(new FileInputStream("src/main/java/com/www/dataSet/description.txt")));
        for(int i=0;i<1000;i++){
            name[i] = nameIn.readLine();
            time[i] = timeIn.readLine();
            watcher[i] = watcherIn.readLine();
            des[i] = desIn.readLine();
        }
    }


}
