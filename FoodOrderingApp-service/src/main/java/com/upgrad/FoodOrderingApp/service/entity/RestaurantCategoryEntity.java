package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "restaurant_category", schema = "public", catalog = "restaurantdb")
@NamedQueries({
        @NamedQuery(name = "getCategoriesByRestaurant", query = "select rc from RestaurantCategoryEntity rc where rc.restaurantEntity.uuid =:restaurantUuid"),
        @NamedQuery(name = "getRestaurantByCategory", query = "select rc from RestaurantCategoryEntity rc where rc.categoryEntity.uuid =:categoryId")

})
public class RestaurantCategoryEntity implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToStringExclude
    private int id;

    @ToStringExclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurantEntity;

    @ToStringExclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RestaurantEntity getRestaurantEntity() {
        return restaurantEntity;
    }

    public void setRestaurantEntity(RestaurantEntity restaurantEntity) {
        this.restaurantEntity = restaurantEntity;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }
}