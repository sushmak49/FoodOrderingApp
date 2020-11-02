package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

//removed unused named queries
@Entity
@Table(name = "restaurant_item", schema = "public", catalog = "restaurantdb")
public class RestaurantItemEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

//    private RestaurantEntity restaurantByRestaurantId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private ItemEntity itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private RestaurantEntity restaurantId;
//    private ItemEntity itemByItemId;

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

//    @ManyToOne
//    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
//    public RestaurantEntity getRestaurantByRestaurantId() {
//        return restaurantByRestaurantId;
//    }
//
//    public void setRestaurantByRestaurantId(RestaurantEntity restaurantByRestaurantId) {
//        this.restaurantByRestaurantId = restaurantByRestaurantId;
//    }

    public ItemEntity getItemId() {
        return itemId;
    }

    public void setItemId(ItemEntity itemId) {
        this.itemId = itemId;
    }

    public RestaurantEntity getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(RestaurantEntity restaurantId) {
        this.restaurantId = restaurantId;
    }


//    @ManyToOne
//    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
//    public ItemEntity getItemByItemId() {
//        return itemByItemId;
//    }
//
//    public void setItemByItemId(ItemEntity itemByItemId) {
//        this.itemByItemId = itemByItemId;
//    }
}
