package ru.clevertec.ecl.services;

import ru.clevertec.ecl.entity.Order;

public interface OrderService {

    Order addOrder(Order order);

    Order getOrderByUsername(String username);

}
