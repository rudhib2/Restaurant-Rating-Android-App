package edu.illinois.cs.cs124.ay2021.mp.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import java.util.prefs.Preferences;

public class RelatedRestaurants {
  private Map<String, Map<String, Integer>> restaurantRelationships = new HashMap<>();

  private List<String> validIDs = new ArrayList<>();

  public RelatedRestaurants(
      final List<Restaurant> restaurants, final List<Preference> preferences) {
    for (Restaurant res : restaurants) {
      validIDs.add(res.getId());
    }
    for (Restaurant restaurant : restaurants) {
      Map<String, Integer> restaurantPreferenceMap = new HashMap<>();
      for (Preference preference : preferences) {
        List<String> prefIDList = new ArrayList<>();
        Collections.addAll(prefIDList, preference.getRestaurantIDs());
        if (prefIDList.contains(restaurant.getId())) {
          for (String restaurantId : preference.getRestaurantIDs()) {
            if ((!(validIDs.contains(restaurantId))) || restaurantId.equals(restaurant.getId())) {
              continue;
            } else {
              if (restaurantPreferenceMap.get(restaurantId) == null) {
                restaurantPreferenceMap.put(restaurantId, 1);
              } else {
                restaurantPreferenceMap.put(
                    restaurantId, restaurantPreferenceMap.get(restaurantId) + 1);
              }
            }
          }
        }
      }
      restaurantRelationships.put(restaurant.getId(), restaurantPreferenceMap);
    }
  }

  public Map<String, Integer> getRelated(final String restaurantId) {
    Map<String, Integer> mapReturn = restaurantRelationships.get(restaurantId);
    if (mapReturn != null) {
      return mapReturn;
    }
    return new HashMap<>();
  }
}
