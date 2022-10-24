package ru.clevertec.ecl.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findAllByUser(User user, Pageable pageable);

  @Query(value = "SELECT setval('orders_id', :seq) ", nativeQuery = true)
  long getNextSequence(Long seq);

  @Query(value = "SELECT last_value FROM orders_id", nativeQuery = true)
  long getLastValueSequence();

}
