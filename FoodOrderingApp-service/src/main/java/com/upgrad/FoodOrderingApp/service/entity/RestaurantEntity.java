package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "restaurant", schema = "public", catalog = "restaurantdb")
@NamedQueries({
        @NamedQuery(
                name = "getAllRestaurantByRating",
                query = "select r from RestaurantEntity r order by r.customerRating desc"),
        @NamedQuery(
                name = "getRestaurantByName",
                query = "select r from RestaurantEntity r where lower(r.restaurantName) like lower(:restaurantName) order by r.restaurantName asc"),
        @NamedQuery(
                name = "getRestaurantByUuid",
                query = "select r from RestaurantEntity r where r.uuid= :restaurantUuid order by r.restaurantName asc")
})
public class RestaurantEntity {
    private int id;
    private String uuid;
    private String restaurantName;
    private String photoUrl;
    private Double customerRating;
    private int avgPrice;
    private int numberCustomersRated;
    private int addressId;
    private Collection<OrdersEntity> ordersById;
    @ManyToOne
    @JoinColumn(name = "address_id")
    @NotNull
    private AddressEntity address;
    private Collection<RestaurantCategoryEntity> restaurantCategoriesById;
    private Collection<RestaurantItemEntity> restaurantItemsById;

    @ManyToMany
    @JoinTable(name = "restaurant_category", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryEntity> categories = new ArrayList<>();

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "uuid", nullable = false, length = 200)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "restaurant_name", nullable = false, length = 50)
    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @Basic
    @Column(name = "photo_url", nullable = true, length = 255)
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Basic
    @Column(name = "customer_rating", nullable = false, precision = 0)
    @NotNull
    public Double getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Double customerRating) {
        this.customerRating = customerRating;
    }

    @Basic
    @Column(name = "average_price_for_two", nullable = false)
    public int getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(int avgPrice) {
        this.avgPrice = avgPrice;
    }

    @Basic
    @Column(name = "number_of_customers_rated", nullable = false)
    public int getNumberCustomersRated() {
        return numberCustomersRated;
    }

    public void setNumberCustomersRated(int numberCustomersRated) {
        this.numberCustomersRated = numberCustomersRated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return id == that.id &&
                avgPrice == that.avgPrice &&
                numberCustomersRated == that.numberCustomersRated &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(restaurantName, that.restaurantName) &&
                Objects.equals(photoUrl, that.photoUrl) &&
                Objects.equals(customerRating, that.customerRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, restaurantName, photoUrl, customerRating, avgPrice, numberCustomersRated);
    }

    @Basic
    @Column(name = "address_id", nullable = false)
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @OneToMany(mappedBy = "restaurantByRestaurantId")
    public Collection<OrdersEntity> getOrdersById() {
        return ordersById;
    }

    public void setOrdersById(Collection<OrdersEntity> ordersById) {
        this.ordersById = ordersById;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    @OneToMany(mappedBy = "restaurantByRestaurantId")
    public Collection<RestaurantCategoryEntity> getRestaurantCategoriesById() {
        return restaurantCategoriesById;
    }

    public void setRestaurantCategoriesById(Collection<RestaurantCategoryEntity> restaurantCategoriesById) {
        this.restaurantCategoriesById = restaurantCategoriesById;
    }

    @OneToMany(mappedBy = "restaurantByRestaurantId")
    public Collection<RestaurantItemEntity> getRestaurantItemsById() {
        return restaurantItemsById;
    }

    public void setRestaurantItemsById(Collection<RestaurantItemEntity> restaurantItemsById) {
        this.restaurantItemsById = restaurantItemsById;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    @ManyToMany
    @JoinTable(name = "restaurant_item", joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemEntity> items = new ArrayList<>();

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}
