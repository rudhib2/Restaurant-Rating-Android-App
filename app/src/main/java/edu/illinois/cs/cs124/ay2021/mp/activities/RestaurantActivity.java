package edu.illinois.cs.cs124.ay2021.mp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import edu.illinois.cs.cs124.ay2021.mp.R;
import edu.illinois.cs.cs124.ay2021.mp.application.EatableApplication;
import edu.illinois.cs.cs124.ay2021.mp.databinding.ActivityRestaurantBinding;
import edu.illinois.cs.cs124.ay2021.mp.models.Restaurant;

public class RestaurantActivity extends AppCompatActivity {

  private ActivityRestaurantBinding binding;

  private EatableApplication application;

  private Restaurant restaurant;

  @Override
  protected void onCreate(@Nullable final Bundle unused) {
    super.onCreate(unused);
    application = (EatableApplication) getApplication();

    Intent startedIntent = getIntent();
    String restaurantId = startedIntent.getStringExtra("id");

    Restaurant r = application.getClient().convertIdToRestaurant(restaurantId);

    binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant);
    binding.name.setText(r.getName());
    binding.cuisine.setText(r.getCuisine());
  }
}
