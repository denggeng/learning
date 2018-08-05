package com.github.denggeng.learning.word.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 词根表
 */
@Entity
public class Etyma implements Comparable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 50)
    private String word;

    @Column(length = 2000)
    private String meanCn;

    @Column(length = 50)
    private String fullWord;

    @Column(length = 50)
    private String equalWord;

    public String getWord() {
        return word;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeanCn() {
        return meanCn;
    }

    public void setMeanCn(String meanCn) {
        this.meanCn = meanCn;
    }

    public String getFullWord() {
        return fullWord;
    }

    public void setFullWord(String fullWord) {
        this.fullWord = fullWord;
    }

    public String getEqualWord() {
        return equalWord;
    }

    public void setEqualWord(String equalWord) {
        this.equalWord = equalWord;
    }

    @Override
    public int compareTo(Object o) {
        Etyma otherEtyma = (Etyma) o;
        return this.getWord().compareTo(otherEtyma.getWord());
    }
}
