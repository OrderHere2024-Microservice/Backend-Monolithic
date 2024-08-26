package com.backend.orderhere.service.enums;

public enum DishSort {
  RATING("rating"),
  CATEGORY("category");

  private final String name;

  DishSort(String name) {
    this.name = name;
  }

  public static DishSort getEnumByString(String type) {
    for (DishSort e : DishSort.values()) {
      if (e.name.equals(type)) return e;
    }
    return DishSort.CATEGORY;
  }

  public String getName() {
    return this.name;
  }
}
