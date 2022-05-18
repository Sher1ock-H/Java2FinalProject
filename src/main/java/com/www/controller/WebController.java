package com.www.controller;

import com.www.util.GetValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class WebController {

    public static GetValue getValue = new GetValue();

    @RequestMapping("/")
    public String index() {
        return "bar-race-git";
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
    @RequestMapping(value = "/year/{year}/month/{month}", produces = "application/json; charset=utf-8")
    public String year_month(@PathVariable("year") String y, @PathVariable("month") String m) {
        return getValue.getMonth(Integer.parseInt(y), Integer.parseInt(m));
    }

    @ResponseBody
    @RequestMapping(value = "/page/{i}", produces = "application/json; charset=utf-8")
    public String page(@PathVariable("i") String i) {
        return getValue.getPage(Integer.parseInt(i));
    }
}
