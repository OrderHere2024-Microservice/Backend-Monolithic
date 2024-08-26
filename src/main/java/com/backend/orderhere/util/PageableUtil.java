package com.backend.orderhere.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {
  public static Pageable determinePageable(int page, int size, Sort sort) {
    if (size <= 0) {
      return Pageable.unpaged();
    }
    return PageRequest.of(page - 1, size, sort);
  }
}
