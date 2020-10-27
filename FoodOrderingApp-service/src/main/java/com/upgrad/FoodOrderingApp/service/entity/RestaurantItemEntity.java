package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "restaurant_item", schema = "public", catalog = "restaurantdb")
public class RestaurantItemEntity {
    private int id;
    private RestaurantEntity restaurantByRestaurantId;
    private int itemId;
    private int restaurantId;
    private ItemEntity itemByItemId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantItemEntity that = (RestaurantItemEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    public RestaurantEntity getRestaurantByRestaurantId() {
        return restaurantByRestaurantId;
    }

    public void setRestaurantByRestaurantId(RestaurantEntity restaurantByRestaurantId) {
        this.restaurantByRestaurantId = restaurantByRestaurantId;
    }

    @Basic
    @Column(name = "item_id", nullable = false)
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "restaurant_id", nullable = false)
    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    public ItemEntity getItemByItemId() {
        return itemByItemId;
    }

    public void setItemByItemId(ItemEntity itemByItemId) {
        this.itemByItemId = itemByItemId;
    }
}
