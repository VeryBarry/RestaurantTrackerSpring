package com.theironyard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by zach on 6/21/16.
 */
public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {
    public List<Restaurant> findByRating(int rating);
    public List<Restaurant> findByLocation(String location);
    public List<Restaurant> findByUser(User user);

    @Query("SELECT r FROM Restaurant r WHERE r.location LIKE ?1%")
    public List<Restaurant> searchLocation(String location);
}
