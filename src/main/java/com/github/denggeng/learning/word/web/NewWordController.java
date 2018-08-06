package com.github.denggeng.learning.word.web;

import com.github.denggeng.learning.word.service.EtymaService;
import com.github.denggeng.learning.word.service.NewWordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("newWord")
public class NewWordController {

    private final static Logger logger = LoggerFactory.getLogger(NewWordController.class);

    @Autowired
    private NewWordService newWordService;

    @Autowired
    private EtymaService etymaService;


    @RequestMapping("wash")
    @ResponseBody
    public String wash() {
        newWordService.wash();
        return "success";
    }

    @RequestMapping("distictWord")
    @ResponseBody
    public String distictWord() {
        newWordService.distictWord();
        return "success";
    }

    @RequestMapping("genNewWord")
    @ResponseBody
    public String genNewWord() {
        newWordService.genNewWord();
        return "success";
    }

    @RequestMapping("initEtyma")
    @ResponseBody
    public String initEtyma() {
        System.out.println("start init etyma,thread:" + Thread.currentThread().getId());
        new Thread(() -> {
            int count = etymaService.genEtyma();
            System.out.println("generate " + count + " etyma(s)");
        }).start();
        return "submit create etymas";
    }

}
