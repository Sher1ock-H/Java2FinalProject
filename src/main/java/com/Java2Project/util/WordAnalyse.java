package com.Java2Project.util;

import com.google.gson.Gson;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class WordAnalyse {
    public void analyse() throws IOException {
        //筛掉中英文stop words
        HashSet<String> stopWord = new HashSet<>();
        BufferedReader stopWordsIn = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/stopwords.txt"));
        String s;
        while ((s = stopWordsIn.readLine()) != null) {
            stopWord.add(s);
        }

        BufferedReader infoIn = new BufferedReader(new FileReader("src/main/java/com/Java2Project/dataSet/info.json"));
        JSONObject jo1 = JSONObject.fromObject(infoIn.readLine());
        JSONArray ja = jo1.getJSONArray("items");
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            stringList.add(ja.getJSONObject(i).getString("description"));
        }

        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600); //最大词频
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        List<WordFrequency> wordFrequencyList = frequencyAnalyzer.load(stringList);
        wordFrequencyList.sort(Comparator.comparingInt(WordFrequency::getFrequency));

        IArray iArray = new IArray();
        for (WordFrequency wordFrequency : wordFrequencyList) {
            if (stopWord.contains(wordFrequency.getWord())) continue;
            iArray.add(new Item(wordFrequency.getWord(), wordFrequency.getFrequency()));
        }
        String cloud = new Gson().toJson(iArray);
        BufferedWriter out = new BufferedWriter(new FileWriter("src/main/java/com/Java2Project/dataSet/cloud.json"));
        out.write(cloud);
        out.close();
        System.out.println("饼图数据传输成功");
    }

    public static class IArray {
        ArrayList<Item> items;

        public IArray() {
            items = new ArrayList<>();
        }

        public void add(Item item) {
            items.add(item);
        }
    }

    public static class Item {
        String word;
        int frequency;

        public Item(String word, int frequency) {
            this.word = word;
            this.frequency = frequency;
        }
    }
}
