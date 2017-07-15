package com.github.denggeng.learning.word.domain;

import java.util.Map;

/**
 * o o w
 * Created by dengg on 2017-07-15.
 */
public class OriOldWord {

    private Integer topicId;

    private Integer wordLevelId;

    private Integer tagId;

    private String word;

    private String wordVariants;

    private String wordAudio;

    private String imageFile;

    private String accent;

    private String meanCn;

    private String meanEn;

    private String shortPhrase;

    private String deformationImg;

    private String sentence;

    private String sentenceTrans;

    private String sentencePhrase;

    private String sentenceAudio;

    private Map<String, Object> clozeData;

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getWordLevelId() {
        return wordLevelId;
    }

    public void setWordLevelId(Integer wordLevelId) {
        this.wordLevelId = wordLevelId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordVariants() {
        return wordVariants;
    }

    public void setWordVariants(String wordVariants) {
        this.wordVariants = wordVariants;
    }

    public String getWordAudio() {
        return wordAudio;
    }

    public void setWordAudio(String wordAudio) {
        this.wordAudio = wordAudio;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getAccent() {
        return accent;
    }

    public void setAccent(String accent) {
        this.accent = accent;
    }

    public String getMeanCn() {
        return meanCn;
    }

    public void setMeanCn(String meanCn) {
        this.meanCn = meanCn;
    }

    public String getMeanEn() {
        return meanEn;
    }

    public void setMeanEn(String meanEn) {
        this.meanEn = meanEn;
    }

    public String getShortPhrase() {
        return shortPhrase;
    }

    public void setShortPhrase(String shortPhrase) {
        this.shortPhrase = shortPhrase;
    }

    public String getDeformationImg() {
        return deformationImg;
    }

    public void setDeformationImg(String deformationImg) {
        this.deformationImg = deformationImg;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentenceTrans() {
        return sentenceTrans;
    }

    public void setSentenceTrans(String sentenceTrans) {
        this.sentenceTrans = sentenceTrans;
    }

    public String getSentencePhrase() {
        return sentencePhrase;
    }

    public void setSentencePhrase(String sentencePhrase) {
        this.sentencePhrase = sentencePhrase;
    }

    public String getSentenceAudio() {
        return sentenceAudio;
    }

    public void setSentenceAudio(String sentenceAudio) {
        this.sentenceAudio = sentenceAudio;
    }

    public Map<String, Object> getClozeData() {
        return clozeData;
    }

    public void setClozeData(Map<String, Object> clozeData) {
        this.clozeData = clozeData;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "OriOldWord{" +
                "topicId=" + topicId +
                ", wordLevelId=" + wordLevelId +
                ", tagId=" + tagId +
                ", word='" + word + '\'' +
                ", wordVariants='" + wordVariants + '\'' +
                ", wordAudio='" + wordAudio + '\'' +
                ", imageFile='" + imageFile + '\'' +
                ", accent='" + accent + '\'' +
                ", meanCn='" + meanCn + '\'' +
                ", meanEn='" + meanEn + '\'' +
                ", shortPhrase='" + shortPhrase + '\'' +
                ", deformationImg='" + deformationImg + '\'' +
                ", sentence='" + sentence + '\'' +
                ", sentenceTrans='" + sentenceTrans + '\'' +
                ", sentencePhrase='" + sentencePhrase + '\'' +
                ", sentenceAudio='" + sentenceAudio + '\'' +
                ", clozeData=" + clozeData +
                '}';
    }
}
