package com.example.demo;

import com.example.demo.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    @RequestMapping(value = "/another", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        System.out.println("Home Page Requested, locale = " + locale);
        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        return "another";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String user(@Validated User user, Model model) {
        System.out.println("User Page Requested");
        model.addAttribute("userName", user.getUserName());
        return "user";
    }

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/homepage")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }


}