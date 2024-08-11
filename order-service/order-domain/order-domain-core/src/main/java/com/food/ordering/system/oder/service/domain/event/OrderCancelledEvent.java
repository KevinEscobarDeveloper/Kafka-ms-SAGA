package com.food.ordering.system.oder.service.domain.event;

import com.food.ordering.system.event.DomainEvent;
import com.food.ordering.system.oder.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCancelledEvent extends OrderEvent {
    public OrderCancelledEvent(Order order, ZonedDateTime createAt) {
        super(order, createAt);
    }
}
