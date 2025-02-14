package edu.illinois.cs.cs124.ay2021.mp.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// import java.util.prefs.Preferences;

public class RelatedRestaurants {
  private Map<String, Map<String, Integer>> restaurantRelationships = new HashMap<>();

  private List<String> validIDs = new ArrayList<>();
  private List<Restaurant> restaurantList = new ArrayList<>();
  private Map<String, Restaurant> restaurantToId = new HashMap<>();

  public RelatedRestaurants(
      final List<Restaurant> restaurants, final List<Preference> preferences) {
    for (Restaurant res : restaurants) {
      validIDs.add(res.getId());
      restaurantList.add(res);
      restaurantToId.put(res.getId(), res);
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

  public List<Restaurant> getRelatedInOrder(final String restaurantId) {
    if (restaurantId == null || !(validIDs.contains(restaurantId))) {
      throw new IllegalArgumentException();
    }
    Map<String, Integer> mapRelated = getRelated(restaurantId);
    Set keySet = mapRelated.keySet().stream().collect(Collectors.toSet());
    List<String> listRelated = new ArrayList<>();
    for (Object key : keySet) {
      String key2 = (String) key;
      listRelated.add(key2);
    }
    List<Restaurant> listNowRelated = new ArrayList<>();
    for (int i = 0; i < restaurantList.size(); i++) {
      if (listRelated.contains(restaurantList.get(i).getId())) {
        listNowRelated.add(restaurantList.get(i));
      }
    }
    Collections.sort(
        listNowRelated,
        (r1, r2) -> {
          return r1.getName().compareTo(r2.getName());
        });
    Collections.sort(
        listNowRelated,
        (r1, r2) -> {
          return mapRelated.get(r2.getId()) - mapRelated.get(r1.getId());
        });
    return listNowRelated;
  }

  public Set<Restaurant> getConnectedTo(final String restaurantId) {

    if (restaurantId == null || !(validIDs.contains(restaurantId))) {
      throw new IllegalArgumentException();
    }
    Set<String> mergedSet = new HashSet<>();
    Set<String> fixedSet = new HashSet<>();
    fixedSet.addAll(toHelp(restaurantId));
    mergedSet.addAll(toHelp(restaurantId));

    for (String restaurant : fixedSet) {
      mergedSet.addAll(toHelp(restaurant));
    }
    Set<Restaurant> toReturn = new HashSet<>();
    for (String id : mergedSet) {
      if (id.equals(restaurantId)) {
        continue;
      }
      toReturn.add(restaurantToId.get(id));
    }
    //toReturn.remove(restaurantToId.get(restaurantId));
    return toReturn;
  }

  private Set<String> toHelp(final String restaurantId) {
    // return set of one hop restaurant ids in which weight is > 1
    Map<String, Integer> mapRelated = getRelated(restaurantId);
    Set<String> keySet = mapRelated.keySet();
    if (restaurantId == null) {
      throw new IllegalArgumentException();
    }

    // create new set of keys of map
    Set<String> relRestaurants = new HashSet<>();
    for (String restaurant : keySet) {
      if (mapRelated.get(restaurant) > 1) {
        relRestaurants.add(restaurant);
      }
    }
    // for loop-  check for weight of each restaurant
    // add resulting restaurants to set
    return relRestaurants;
  }
}
