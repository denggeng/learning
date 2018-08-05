package com.github.denggeng.learning.word.service;

import com.github.denggeng.learning.word.domain.NewWord;
import com.github.denggeng.learning.word.repository.NewWordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NewWordService {

    private final static Logger logger = LoggerFactory.getLogger(NewWordService.class);

    @Autowired
    private NewWordRepository newWordRepository;

    public void wash() {
        Iterable<NewWord> newWords = newWordRepository.findAll();
        for (NewWord newWord : newWords) {
            if (StringUtils.hasText(newWord.getWordEtyma())) {
                newWord.setWordEtyma(washEtyma(newWord.getWordEtyma()));
            }
        }
        newWordRepository.save(newWords);

    }

    private String washEtyma(String etyma) {
        String newEtyma = null;
        if (StringUtils.hasText(etyma)) {
            newEtyma = etyma.replaceAll("\\＋", "+");
            newEtyma = newEtyma.replaceAll("···", "…")
                    .replaceAll(" ", "")
                    .replaceAll("＝", "=")
                    .replaceAll("\"", "")
                    .replaceAll(" ", "")
                    .replaceAll("-","");
        }
        return newEtyma;
    }

}
