package ru.clevertec.ecl.utils;

import lombok.Getter;

@Getter
public class PageSize {

  private static final int PAGE_DEFAULT = 1;
  private static final int LIMIT_DEFAULT = 10;
  private static final String ZERO = "0";

  int page;
  int size;

  public PageSize(String page, String size) {
    this.page = (page == null)|| (page.equals(ZERO)) ? PAGE_DEFAULT : Integer.parseInt(page);
    this.size = size == null ? LIMIT_DEFAULT : Integer.parseInt(size);
  }

}
