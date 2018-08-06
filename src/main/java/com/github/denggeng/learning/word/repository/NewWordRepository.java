package com.github.denggeng.learning.word.repository;

import com.github.denggeng.learning.word.domain.NewWord;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Old word
 * Created by dengg on 2017-07-15.
 */
public interface NewWordRepository extends PagingAndSortingRepository<NewWord, Integer> {
    List<NewWord> findByWord(String word);
}
