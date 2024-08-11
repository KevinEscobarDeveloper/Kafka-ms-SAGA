package com.food.ordering.system.oder.service.domain.entity;

import com.food.ordering.system.entity.AggregateRoot;
import com.food.ordering.system.valueobject.RestaurantId;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> products;
    private boolean active;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        setActive(builder.active);
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private final List<Product> products;
        private boolean active;

        public Builder(List<Product> products) {
            this.products = products;
        }

        public Builder(Restaurant copy) {
            this.restaurantId = copy.getId();
            this.products = copy.getProducts();
            this.active = copy.isActive();
        }

        public Builder withId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder withActive(boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }

}
