package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.util.Objects;

//removed named unused queries
@Entity
@Table(name = "category_item", schema = "public", catalog = "restaurantdb")
public class CategoryItemEntity {
    private int id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id", nullable = false)
    private ItemEntity itemId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryEntity categoryId;
//    private  itemByItemId;
//    private CategoryEntity categoryByCategoryId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemEntity getItemId() {
        return itemId;
    }

    public void setItemId(ItemEntity itemId) {
        this.itemId = itemId;
    }

    public CategoryEntity getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(CategoryEntity categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryItemEntity that = (CategoryItemEntity) o;
        return id == that.id &&
                itemId == that.itemId &&
                categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, categoryId);
    }

//    public ItemEntity getItemByItemId() {
//        return itemByItemId;
//    }
//
//    public void setItemByItemId(ItemEntity itemByItemId) {
//        this.itemByItemId = itemByItemId;
//    }
//
//
//    public CategoryEntity getCategoryByCategoryId() {
//        return categoryByCategoryId;
//    }
//
//    public void setCategoryByCategoryId(CategoryEntity categoryByCategoryId) {
//        this.categoryByCategoryId = categoryByCategoryId;
//    }
}
