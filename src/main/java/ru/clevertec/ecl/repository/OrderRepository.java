package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.ecl.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
