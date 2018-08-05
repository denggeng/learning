package com.github.denggeng.learning.word.repository;

import com.github.denggeng.learning.word.domain.OldWord;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Old word
 * Created by dengg on 2017-07-15.
 */
public interface OldWordRepository extends PagingAndSortingRepository<OldWord,Integer>{
}
