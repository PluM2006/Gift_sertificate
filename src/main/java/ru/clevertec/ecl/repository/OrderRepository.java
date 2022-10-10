package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user, Pageable pageable);

}
