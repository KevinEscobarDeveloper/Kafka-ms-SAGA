package com.food.ordering.system.oder.service.domain.event;

import com.food.ordering.system.event.DomainEvent;
import com.food.ordering.system.oder.service.domain.entity.Order;

import java.time.ZonedDateTime;

public abstract class OrderEvent implements DomainEvent<Order> {
    private final Order order;
    private final ZonedDateTime createAt;

    public OrderEvent(Order order, ZonedDateTime createAt) {
        this.order = order;
        this.createAt = createAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }
}
