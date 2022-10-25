package ru.clevertec.ecl.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class OffsetLimitPageable implements Pageable {

  private int limit;
  private int offset;
  private Sort sort = Sort.by(Direction.ASC, "id");

  public OffsetLimitPageable(int limit, int offset) {
    if (offset < 0) {
      throw new IllegalArgumentException("Offset index must not be less than zero!");
    }

    if (limit < 1) {
      throw new IllegalArgumentException("Limit must not be less than one!");
    }
    this.limit = limit;
    this.offset = offset;
  }

  @Override
  public int getPageNumber() {
    return offset / limit;
  }

  @Override
  public int getPageSize() {
    return limit;
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @Override
  public Sort getSort() {
    return sort;
  }

  @Override
  public Pageable next() {
    return new OffsetLimitPageable(getPageSize(), (int) (getOffset() + getPageSize()));
  }

  @Override
  public Pageable previousOrFirst() {
    return hasPrevious() ?
        new OffsetLimitPageable(getPageSize(), (int) (getOffset() + getPageSize())) : this;
  }

  @Override
  public Pageable first() {
    return new OffsetLimitPageable(getPageSize(), 0);
  }

  @Override
  public Pageable withPage(int pageNumber) {
    return new OffsetLimitPageable(pageNumber, getPageSize());
  }

  @Override
  public boolean hasPrevious() {
    return offset > limit;
  }
}
