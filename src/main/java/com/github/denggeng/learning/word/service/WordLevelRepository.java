package com.github.denggeng.learning.word.service;

import com.github.denggeng.learning.word.domain.WordLevel;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Old word
 * Created by dengg on 2017-07-15.
 */
public interface WordLevelRepository extends PagingAndSortingRepository<WordLevel,Integer>{
}
