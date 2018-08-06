package com.github.denggeng.learning.word.service;

import com.github.denggeng.learning.word.domain.NewWord;
import com.github.denggeng.learning.word.domain.OldWord;
import com.github.denggeng.learning.word.domain.WordLevel;
import com.github.denggeng.learning.word.repository.NewWordRepository;
import com.github.denggeng.learning.word.repository.OldWordRepository;
import com.github.denggeng.learning.word.repository.WordLevelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class NewWordService {

    private final static Logger logger = LoggerFactory.getLogger(NewWordService.class);

    @Autowired
    private NewWordRepository newWordRepository;

    @Autowired
    private OldWordRepository oldWordRepository;

    @Autowired
    private WordLevelRepository wordLevelRepository;

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
                    .replaceAll("-", "");
        }
        return newEtyma;
    }

    public void genNewWord() {
        Iterable<OldWord> oldWords = oldWordRepository.findAll();
        List<WordLevel> wordLevelList = createWorldLevelForOldWord(oldWords);
        System.out.println("wordLevelList size:" + wordLevelList.size());
        wordLevelRepository.save(wordLevelList);
        Map<String, OldWord> oldWordMap = new HashMap<>();
        for (OldWord oldWord : oldWords) {
            oldWordMap.put(oldWord.getWord(), oldWord);
        }
        System.out.println("distinct old word size:" + oldWordMap.size());
        List<NewWord> newWords = new ArrayList<>();
        for (OldWord oldWord : oldWordMap.values()) {
            NewWord newWord = new NewWord();
            BeanUtils.copyProperties(oldWord, newWord);
            newWords.add(newWord);
        }
        System.out.println("distinct new word size:" + newWords.size());
        newWordRepository.save(newWords);
    }

    public void distictWord() {
        Iterable<NewWord> newWords = newWordRepository.findAll();
        Set<String> words = new HashSet<>();
        for (NewWord newWord : newWords) {
            words.add(newWord.getWord());
        }
        for (String word : words) {
            List<NewWord> wordList = newWordRepository.findByWord(word);
            List<WordLevel> levels = createWorldLevel(wordList);
            wordLevelRepository.save(levels);

            if (wordList.size() > 1) {
                wordList.remove(0);
                newWordRepository.delete(wordList);
                System.out.println("delete word [" + word + "] of size:" + wordList.size());
            }
        }
    }

    private List<WordLevel> createWorldLevel(List<NewWord> wordList) {
        List<WordLevel> wordLevels = new ArrayList<>();
        for (NewWord newWord : wordList) {
            WordLevel wordLevel = new WordLevel();
            wordLevel.setWord(newWord.getWord());
            wordLevel.setLevelId(newWord.getWordLevelId());
            wordLevels.add(wordLevel);
        }
        return wordLevels;
    }

    private List<WordLevel> createWorldLevelForOldWord(Iterable<OldWord> wordList) {
        List<WordLevel> wordLevels = new ArrayList<>();
        for (OldWord oldWord : wordList) {
            WordLevel wordLevel = new WordLevel();
            wordLevel.setWord(oldWord.getWord());
            wordLevel.setLevelId(oldWord.getWordLevelId());
            wordLevels.add(wordLevel);
        }
        return wordLevels;
    }
}
