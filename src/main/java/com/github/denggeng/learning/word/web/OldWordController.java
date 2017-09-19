package com.github.denggeng.learning.word.web;

import com.github.denggeng.learning.word.domain.OldWord;
import com.github.denggeng.learning.word.domain.OriOldWord;
import com.github.denggeng.learning.word.service.OldWordRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * old word c
 * Created by dengg on 2017-07-15.
 */
@RestController
@RequestMapping("/oldWord")
public class OldWordController {

    private final static Logger logger = LoggerFactory.getLogger(OldWordController.class);

    private Gson gson = new Gson();

    private List<OldWord> oldWords = new ArrayList<>(1000);
    private Set<String> fieldSet = new HashSet<>();

    @Autowired
    private OldWordRepository oldWordRepository;

    @RequestMapping("")
    public Object getOldWords() {
        return oldWordRepository.findAll(new PageRequest(0, 10));
    }

    @RequestMapping("initData")
    public Object initData(String path) {
        treeRead(new File("D:\\study\\words\\extract"));
        oldWordRepository.save(oldWords);
        oldWords.clear();
        return "success!";
    }

    @RequestMapping("showFields")
    public Object showFields(String path) {
        treeRead(new File("D:\\study\\words\\extract"));
        oldWords.clear();
        return fieldSet;
    }

    private void treeRead(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File innerFile : files) {
                    treeRead(innerFile);
                }
            }

        } else {
            if (file.getName().equals("meta.json")) {
                String jsonString = readOne(file);
                //logger.info("word:{}", jsonString);
                addOne(jsonString);
/*                if (oldWords.size() % 5000 == 0) {
                    oldWordRepository.save(oldWords);
                    oldWords.clear();
                }*/

            }
        }
    }

    private void addOne(String jsonString) {
        try {
            Assert.notNull(jsonString);
            OriOldWord oriOldWord = createOriOldWord(jsonString);
            OldWord oldWord = new OldWord();
            BeanUtils.copyProperties(oriOldWord, oldWord);
            oldWord.setCloseData(gson.toJson(oriOldWord.getClozeData()));
            logger.info("word:{}", oldWord.getWord());
            oldWords.add(oldWord);
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    private OriOldWord createOriOldWord(String jsonString) {
        OriOldWord oriOldWord = new OriOldWord();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            fieldSet.add(entry.getKey());
        }
        oriOldWord.setTopicId(jsonObject.get("topic_id") != null ? jsonObject.get("topic_id").getAsInt() : null);
        oriOldWord.setWordLevelId(jsonObject.get("word_level_id") != null ? jsonObject.get("word_level_id").getAsInt() : null);
        oriOldWord.setTagId(jsonObject.get("tag_id") != null ? jsonObject.get("tag_id").getAsInt() : null);
        oriOldWord.setWord(jsonObject.get("word") != null ? jsonObject.get("word").getAsString() : null);
        oriOldWord.setWordAudio(jsonObject.get("word_audio") != null ? jsonObject.get("word_audio").getAsString() : null);
        oriOldWord.setWordVariants(jsonObject.get("word_variants") != null ? jsonObject.get("word_variants").getAsString() : null);
        oriOldWord.setImageFile(jsonObject.get("image_file") != null ? jsonObject.get("image_file").getAsString() : null);
        oriOldWord.setAccent(jsonObject.get("accent") != null ? jsonObject.get("accent").getAsString() : null);
        oriOldWord.setMeanCn(jsonObject.get("mean_cn") != null ? jsonObject.get("mean_cn").getAsString() : null);
        oriOldWord.setMeanEn(jsonObject.get("mean_en") != null ? jsonObject.get("mean_en").getAsString() : null);
        oriOldWord.setShortPhrase(jsonObject.get("short_phrase") != null ? jsonObject.get("short_phrase").getAsString() : null);
        oriOldWord.setDeformationImg(jsonObject.get("deformation_img") != null ? jsonObject.get("deformation_img").getAsString() : null);
        oriOldWord.setSentence(jsonObject.get("sentence") != null ? jsonObject.get("sentence").getAsString() : null);
        oriOldWord.setSentenceTrans(jsonObject.get("sentence_trans") != null ? jsonObject.get("sentence_trans").getAsString() : null);
        oriOldWord.setSentencePhrase(jsonObject.get("sentence_phrase") != null ? jsonObject.get("sentence_phrase").getAsString() : null);
        oriOldWord.setSentenceAudio(jsonObject.get("sentence_audio") != null ? jsonObject.get("sentence_audio").getAsString() : null);
        oriOldWord.setWordEtyma(jsonObject.get("word_etyma") != null ? jsonObject.get("word_etyma").getAsString() : null);
        oriOldWord.setClozeData(gson.fromJson(jsonObject.get("cloze_data") != null ? jsonObject.get("cloze_data") : new JsonObject(), Map.class));

        oriOldWord.setBpgFile(jsonObject.get("bpg_file") != null ? jsonObject.get("bpg_file").getAsString() : null);
        oriOldWord.setDeformationDesc(jsonObject.get("deformation_desc") != null ? jsonObject.get("deformation_desc").getAsString() : null);
        oriOldWord.setDeformationSentence(jsonObject.get("deformation_sentence") != null ? jsonObject.get("deformation_sentence").getAsString() : null);
        oriOldWord.setDeformationSentenceTrans(jsonObject.get("deformation_sentence_trans") != null ? jsonObject.get("deformation_sentence_trans").getAsString() : null);

        return oriOldWord;
    }


    private String readOne(File jsonFile) {
        String json = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(jsonFile), "UTF8");
            List<String> strings = IOUtils.readLines(inputStreamReader);
            if (strings != null) {
                for (String string : strings) {
                    stringBuilder.append(string);
                }
            }
            json = stringBuilder.toString();
        } catch (Exception e) {
            logger.warn("", e);
        }
        return json;
    }
}
