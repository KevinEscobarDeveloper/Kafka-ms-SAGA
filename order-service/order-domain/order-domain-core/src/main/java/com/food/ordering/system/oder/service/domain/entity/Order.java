package com.food.ordering.system.oder.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.oder.service.domain.exception.OrderDomainException;
import com.food.ordering.system.oder.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.oder.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.oder.service.domain.valueobject.TrackingId;
import com.food.ordering.system.valueobject.*;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;  // Corrección en el nombre del campo
    private final Money price;
    private final List<OrderItem> items;

    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems(){
        long itemId =  1;
        for(OrderItem orderItem: items){
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = items.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return  orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if(!price.equals(orderItemsTotal)){
            throw new OrderDomainException("Total price: "+ price.getAmount() + "is not equal to Order items total: "+ orderItemsTotal.getAmount());
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid()){
            throw new OrderDomainException("Order item price : "+ orderItem.getPrice().getAmount()+ "is not valid for product "+ orderItem.getProduct());
        }
    }

    private void validateTotalPrice() {
        if(price == null || !price.isGreaterThanZero()){
            throw new OrderDomainException("Total price must be grater than zero");
        }
    }

    private void validateInitialOrder() {
        if( orderStatus!= null || getId() != null){
            throw new OrderDomainException("Order is not in corret state for initialization");
        }
    }

    private Order(Builder builder) {
        super.setId(builder.orderId);
        this.customerId = builder.customerId;
        this.restaurantId = builder.restaurantId;
        this.deliveryAddress = builder.deliveryAddress;  // Corrección en el nombre del campo
        this.price = builder.price;
        this.items = builder.items;
        this.trackingId = builder.trackingId;
        this.orderStatus = builder.orderStatus;
        this.failureMessages = builder.failureMessages;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {  // Corrección en el nombre del método
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(TrackingId trackingId) {
        this.trackingId = trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public void setFailureMessages(List<String> failureMessages) {
        this.failureMessages = failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private final CustomerId customerId;
        private final RestaurantId restaurantId;
        private final StreetAddress deliveryAddress;  // Corrección en el nombre del campo
        private final Money price;
        private final List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        public Builder(CustomerId customerId, RestaurantId restaurantId, StreetAddress deliveryAddress, Money price, List<OrderItem> items) {
            this.customerId = customerId;
            this.restaurantId = restaurantId;
            this.deliveryAddress = deliveryAddress;  // Corrección en el nombre del campo
            this.price = price;
            this.items = items;
        }

        public Builder orderId(OrderId val) {
            this.orderId = val;
            return this;
        }

        public Builder withTrackingId(TrackingId val) {
            this.trackingId = val;
            return this;
        }

        public Builder withOrderStatus(OrderStatus val) {
            this.orderStatus = val;
            return this;
        }

        public Builder withFailureMessages(List<String> val) {
            this.failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
