package com.github.denggeng.learning.word.web;

import com.github.denggeng.learning.word.service.NewWordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("newWord")
public class NewWordController {

    private final static Logger logger = LoggerFactory.getLogger(NewWordController.class);

    @Autowired
    private NewWordService newWordService;

    @RequestMapping("wash")
    public String wash() {
        newWordService.wash();
        return "success";
    }

}
