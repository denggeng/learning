package com.github.denggeng.learning.core;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMatch {

    @Test
    public void matchCnWord() {
        String testTxt = "ambit(ion)雄，、,…＋心，啊，的";
        //String testTxt = "per始终是";
//  注意[\u4E00-\u9FA5]里面的斜杠字符，千万不可省略，不区分大小写
        Pattern pat = Pattern.compile("^(\\w+\\(*\\w*\\)*\\w*)([\u4E00-\u9FA5\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b…,＋]*)$");
        Matcher mat = pat.matcher(testTxt);
        if (mat.matches()) {
            System.out.println(mat.group(1));
            System.out.println(mat.group(2));
        }
    }

    @Test
    public void matchCnWord2() {
        String testTxt = "per..始终是";

        System.out.println(testTxt.replaceAll("\\.", ""));
        System.out.println("house 房子".replaceAll(" ", ""));
        System.out.println("house＋房子".replaceAll("＋", "+"));
        System.out.println("啊···啊".replaceAll("···", "…"));
        System.out.println("benefi(t)益处 ".trim());
        System.out.println("multi-多".replaceAll("-", ""));
    }

    @Test
    public void matchTwo() {
        Set<Character> alphabet = new HashSet<>();
        Collections.addAll(alphabet, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
        String testTxt = "per始终是";
        int idx = 0;
        for (int i = 0; i < testTxt.length(); i++) {
            if (alphabet.contains(testTxt.charAt(i))) {
                continue;
            } else {
                idx = i;
                break;
            }
        }
        System.out.println(testTxt.substring(0,idx));
        System.out.println(testTxt.substring(idx));

    }
}
