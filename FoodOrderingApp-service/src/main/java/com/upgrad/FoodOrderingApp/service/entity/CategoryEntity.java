package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category", schema = "public", catalog = "restaurantdb")
@NamedQueries({
        @NamedQuery(
                name = "getAllCategory",
                query = "select c from CategoryEntity c order by c.categoryName asc"),
        @NamedQuery(
                name = "getCategoryByUuid",
                query = "select c from CategoryEntity c where c.uuid = :categoryUuid")
})
public class CategoryEntity implements Serializable, Comparable<CategoryEntity> {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "categoryIdGenerator")
    @SequenceGenerator(
            name = "categoryIdGenerator",
            sequenceName = "category_id_seq",
            initialValue = 1,
            allocationSize = 1)
    @ToStringExclude
    @HashCodeExclude
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "category_name")
    @Size(max = 30)
    private String categoryName;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
    private List<ItemEntity> items;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, Boolean.FALSE);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, Boolean.FALSE);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    @Override
    public int compareTo(CategoryEntity categoryEntity) {
        return 0;
    }
}