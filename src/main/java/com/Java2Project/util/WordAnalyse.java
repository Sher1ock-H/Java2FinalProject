package com.Java2Project.util;

import com.google.gson.Gson;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WordAnalyse {
    public void analyse() throws IOException {
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(600); //最大词频
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        List<WordFrequency> wordFrequencyList = frequencyAnalyzer.load("src/main/java/com/Java2Project/dataSet/description.txt");
        wordFrequencyList.sort(Comparator.comparingInt(WordFrequency::getFrequency));
        IArray iArray = new IArray();
        for(WordFrequency wordFrequency : wordFrequencyList)
            iArray.add(new Item(wordFrequency.getWord(), wordFrequency.getFrequency()));
        String cloud = new Gson().toJson(iArray);
        BufferedWriter out = new BufferedWriter(new FileWriter("src/main/java/com/Java2Project/dataSet/cloud.json"));
        out.write(cloud);
        out.close();
        System.out.println("饼图数据传输成功");
    }

    public static class IArray{
        ArrayList<Item> itemArrayList;

        public IArray(){
            itemArrayList = new ArrayList<>();
        }

        public void add(Item item){
            itemArrayList.add(item);
        }
    }

    public static class Item{
        String word;
        int frequency;

        public Item(String word, int frequency){
            this.word = word;
            this.frequency = frequency;
        }
    }
}
