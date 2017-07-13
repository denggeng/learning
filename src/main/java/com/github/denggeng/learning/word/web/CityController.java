package com.github.denggeng.learning.word.web;

import com.github.denggeng.learning.word.service.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * city c
 * Created by dengg on 2017-07-13.
 */
@RestController
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @RequestMapping("cities")
    public Object getCity() {
        return cityRepository.findAll(new PageRequest(0, 10));
    }
}
