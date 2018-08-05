package com.github.denggeng.learning.word.repository;

import com.github.denggeng.learning.word.domain.Etyma;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EtymaRepository extends PagingAndSortingRepository<Etyma,Integer> {
}
