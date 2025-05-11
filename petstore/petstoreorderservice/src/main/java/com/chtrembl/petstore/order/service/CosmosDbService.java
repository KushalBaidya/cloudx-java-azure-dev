package com.chtrembl.petstore.order.service;

import com.chtrembl.petstore.order.model.Order;
import com.chtrembl.petstore.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CosmosDbService {

    @Autowired
    private OrderRepository orderRepository;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}