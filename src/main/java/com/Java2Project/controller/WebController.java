package com.Java2Project.controller;

import com.Java2Project.util.GetValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    public static GetValue getValue = new GetValue();

    @RequestMapping("/")
    public String index() {
        return "bar-race-graph";
    }

    @RequestMapping("/pie-chart")
    public String pieChart() {
        return "pie-chart";
    }

    @RequestMapping("/rank-list")
    public String rankList() {
        return "rankList";
    }
    @RequestMapping(value = "/rank-list/search-result/{param}")
    public String rankListSearch(@PathVariable String param) {
        return "search-result";
    }
    @ResponseBody
    @RequestMapping(value = "/rank-list/search-result/{param}/data", produces = "application/json; charset=utf-8")
    public String searchResult(@PathVariable String param) {
        return getValue.search(param);
    }

    @ResponseBody
    @RequestMapping(value = "/pie-chart/data", produces = "application/json; charset=utf-8")
    public String pieChartData() {
        return getValue.getWords();
    }

    @ResponseBody
    @RequestMapping(value = "/data", produces = "application/json; charset=utf-8")
    public String jsonString() {
        return getValue.getAll();
    }

    @ResponseBody
    @RequestMapping(value = "/year/{year}", produces = "application/json; charset=utf-8")
    public String year(@PathVariable("year") String y) {
        return getValue.getYear(Integer.parseInt(y));
    }


    @ResponseBody
    @RequestMapping(value = "/page/{i}", produces = "application/json; charset=utf-8")
    public String page(@PathVariable("i") String i) {
        return getValue.getPage(Integer.parseInt(i));
    }
}
