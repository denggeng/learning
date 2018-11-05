package com.github.denggeng.learning.word.service;

import com.github.denggeng.learning.word.domain.Etyma;
import com.github.denggeng.learning.word.domain.NewWord;
import com.github.denggeng.learning.word.repository.EtymaRepository;
import com.github.denggeng.learning.word.repository.NewWordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EtymaService {
    private final static Logger logger = LoggerFactory.getLogger(EtymaService.class);

    @Autowired
    private EtymaRepository etymaRepository;

    @Autowired
    private NewWordRepository newWordRepository;

    /**
     * @return numbers of entyma
     */
    public int genEtyma() {
        int count = 0;
        Iterable<NewWord> newWords = newWordRepository.findAll();
        List<String> oldEtymas = queryOldEtyma(newWords);
        List<String> mixEtymas = genMixEtymas(oldEtymas);
        List<List<String>> seprateEtymas = seperateEtyma(mixEtymas);
        List<Etyma> etymas = createEtyma(seprateEtymas);
        List<Etyma> distictEtymas = distictEtyma(etymas);
        Collections.sort(distictEtymas);
        etymaRepository.save(distictEtymas);
        return distictEtymas.size();
    }

    private List<Etyma> distictEtyma(List<Etyma> etymas) {
        List<Etyma> newEtymas = new ArrayList<>();
        Map<String, Etyma> distictEtymaMap = new HashMap<>();
        Map<String, Set<String>> distictEtymaCnMap = new HashMap<>();
        for (Etyma etyma : etymas) {
            if (distictEtymaMap.containsKey(etyma.getWord())) {
                Etyma existedEtyma = distictEtymaMap.get(etyma.getWord());
                Set<String> meanSet = distictEtymaCnMap.get(etyma.getWord());
                meanSet.add(etyma.getEqualWord() != null ? etyma.getEqualWord() : ""
                        + etyma.getMeanCn());
                String cnMean = StringUtils.collectionToDelimitedString(meanSet, "|");
                existedEtyma.setMeanCn(cnMean);
            } else {
                distictEtymaMap.put(etyma.getWord(), etyma);
                Set<String> meanSet = new HashSet<>();
                meanSet.add(etyma.getMeanCn());
                distictEtymaCnMap.put(etyma.getWord(), meanSet);
            }
        }
        newEtymas.addAll(distictEtymaMap.values());
        logger.info("newEtymas size :{}", newEtymas.size());
        return newEtymas;
    }

    private List<Etyma> createEtyma(List<List<String>> seprateEtymas) {
        List<Etyma> etymas = new ArrayList<>();
        for (List<String> seprateEtyma : seprateEtymas) {
            Etyma etyma = createSingleEtyma(seprateEtyma);
            etymas.add(etyma);
        }
        logger.info("etymas size :{}", etymas.size());
        return etymas;
    }

    private Etyma createSingleEtyma(List<String> seprateEtyma) {
        Etyma etyma = new Etyma();
        if (seprateEtyma.size() == 3) {
            etyma.setWord(seprateEtyma.get(0));
            etyma.setEqualWord(seprateEtyma.get(1));
            etyma.setMeanCn(seprateEtyma.get(2));
        } else if (seprateEtyma.size() == 2) {
            etyma.setWord(seprateEtyma.get(0));
            etyma.setMeanCn(seprateEtyma.get(1));
        }
        return etyma;
    }

    private List<List<String>> seperateEtyma(List<String> mixEtymas) {
        List<List<String>> etymas = new ArrayList<>();
        for (String mixEtyma : mixEtymas) {
            List<String> oldSperatedEtyma = seperateEtyma(mixEtyma);
            if (oldSperatedEtyma.size() < 2) {
                System.out.println("oldSperatedEtyma.size is lower than 2 :" + mixEtyma);
            } else {
                etymas.add(oldSperatedEtyma);
            }
        }
        logger.info("seperateEtymas size :{}", etymas.size());
        return etymas;
    }

    /**
     * 分割中英文，并处理有=的情况
     *
     * @param minEtyms 返回值如果是3个，则说明是有=的情况
     * @return seperated etyma
     */
    private List<String> seperateEtyma(String minEtyms) {
        List<String> etyma = new ArrayList<>();
        String[] equlsEtyma = minEtyms.split("=");
        String pureEtyma = minEtyms;
        //处理有等于号的情况
        if (equlsEtyma.length > 1) {
            pureEtyma = equlsEtyma[1];
            etyma.add(equlsEtyma[0]);
        }
        Pattern pat = Pattern.compile("^(\\w+\\(*\\w*\\)*\\w*)" +
                "([\u4E00-\u9FA5\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b…,<>\\[\\]/“”]*)$");
        Matcher mat = pat.matcher(pureEtyma);
        if (mat.matches() && mat.groupCount() == 2) {
            etyma.add(mat.group(1));
            etyma.add(mat.group(2));
        } else {
            int idx = firstNoneChar(pureEtyma);
            if (idx > 0 && idx < pureEtyma.length()) {
                etyma.add(pureEtyma.substring(0,idx));
                etyma.add(pureEtyma.substring(idx));
            }else {
                System.out.println("No matches for :" + minEtyms);
            }
        }

        return etyma;
    }

    /**
     * 得到英文和中文混合的词根
     *
     * @param oldEtymas old etymas
     * @return mix etymas
     */
    private List<String> genMixEtymas(List<String> oldEtymas) {
        List<String> mixEtymas = new ArrayList<>();
        for (String oldEtyma : oldEtymas) {
            String[] strings = oldEtyma.split("→");
            if (strings.length > 1) {
                String minEtymas = strings[0];
                String[] minEtymaArray = minEtymas.split("\\+");
                if (minEtymaArray.length < 1) {
                    System.out.println("genMixEtyma error for:" + minEtymas + ", no + found.");
                }
                Collections.addAll(mixEtymas, minEtymaArray);
            } else {
                System.out.println("genMixEtyma error for:" + oldEtyma + ", no → found.");
            }
        }
        logger.info("mixEtymas size :{}", mixEtymas.size());
        return mixEtymas;
    }

    /**
     * 获取所有word_etyma字段的内容
     *
     * @param newWords old words
     * @return old eryma
     */
    private List<String> queryOldEtyma(Iterable<NewWord> newWords) {
        List<String> etymas = new ArrayList<>();
        for (NewWord newWord : newWords) {
            if (StringUtils.hasText(newWord.getWordEtyma())) {
                //去除空格
                String oldEtyma = newWord.getWordEtyma().replaceAll(" ", "");
                //去除...
                oldEtyma = oldEtyma.replaceAll("\\.", "");
                etymas.add(oldEtyma);
            }
        }
        logger.info("old etymas size :{}", etymas.size());
        return etymas;
    }

    private int firstNoneChar(String text) {
        int idx = 0;
        Set<Character> alphabet = new HashSet<>();
        Collections.addAll(alphabet, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
        for (int i = 0; i < text.length(); i++) {
            if (alphabet.contains(text.charAt(i))) {
                continue;
            } else {
                idx = i;
                break;
            }
        }
        return idx;
    }

}
