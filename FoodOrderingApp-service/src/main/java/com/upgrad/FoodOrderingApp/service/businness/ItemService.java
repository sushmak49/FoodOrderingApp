package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemService {
    @Autowired
    private ItemDao itemDao;

    @Autowired
    private OrdersDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private CategoryDao categoryDao;

    /**
     * Get item entity by item UUID
     *
     * @param uuid
     * @return
     * @throws ItemNotFoundException
     */
    public ItemEntity getItemByUUID(String uuid) throws ItemNotFoundException {
        ItemEntity itemEntity = itemDao.getItemByUUID(uuid);
        if (itemEntity == null) {
            throw new ItemNotFoundException("INF-003", "No item by this id exist");
        }
        return itemEntity;
    }

    /**
     * List top 5 items in a restaurant by desc number of orders
     *
     * @param restaurantEntity
     * @return
     */
    public List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {
        List<ItemEntity> itemEntityList = new ArrayList<>();
        for (OrdersEntity orderEntity : orderDao.getOrdersByRestaurant(restaurantEntity)) {
            for (OrderItemEntity ordersItemEntity : orderItemDao.getItemsByOrder(orderEntity)) {
                itemEntityList.add(ordersItemEntity.getItem());
            }
        }
        //Get all items ordered from a particular restaurant
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (ItemEntity itemEntity : itemEntityList) {
            Integer count = map.get(itemEntity.getUuid());
            map.put(itemEntity.getUuid(), (count == null) ? 1 : count + 1);
        }
        //Store the entire map data in tree-map
        Map<String, Integer> treeMap = new TreeMap<String, Integer>(map);
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(treeMap.entrySet());
        //Sort the map entries based on asc number of orders
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        //Transfer list data to  List<ItemEntity>
        List<ItemEntity> sortedItemEntityList = new ArrayList<ItemEntity>();
        for (int i = 0; i < list.size(); i++) {
            sortedItemEntityList.add(itemDao.getItemByUUID(list.get(i).getKey()));
        }
        // Reverse the List<ItemEntity> to desc sort by number of orders
        Collections.reverse(sortedItemEntityList);
        return sortedItemEntityList;
    }

    /**
     * Get items from a restaurant grouped by category
     * @param restaurantUUID
     * @param categoryUUID
     * @return
     */
    public List<ItemEntity> getItemsByCategoryAndRestaurant(String restaurantUUID, String categoryUUID) {
        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantUUID);
        CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryUUID);
        List<ItemEntity> restaurantItemEntityList = new ArrayList<ItemEntity>();

        for (ItemEntity restaurantItemEntity : restaurantEntity.getItems()) {
            for (ItemEntity categoryItemEntity : categoryEntity.getItems()) {
                if (restaurantItemEntity.getUuid().equals(categoryItemEntity.getUuid())) {
                    restaurantItemEntityList.add(restaurantItemEntity);
                }
            }
        }
        restaurantItemEntityList.sort(Comparator.comparing(ItemEntity::getItemName));
        return restaurantItemEntityList;
    }
}