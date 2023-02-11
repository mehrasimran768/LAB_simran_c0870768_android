package com.example.lab_simran_c0870768_android;

import static java.security.AccessController.getContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<favouritePlace> favoritePlaces = new ArrayList<>();
    private static final int ADD_PLACE_REQUEST_CODE = 1;
    private FavoritePlacesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView favoritePlacesList = findViewById(R.id.places_list);
        FavoritePlacesAdapter adapter = new FavoritePlacesAdapter(this, favoritePlaces);
        favoritePlacesList.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPlaceIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivityForResult(addPlaceIntent, ADD_PLACE_REQUEST_CODE);

            }

        });

        favoritePlaces.add(new favouritePlace("Central Park", "New York, NY", new LatLng(40.7829, -73.9654)));
        favoritePlaces.add(new favouritePlace("Golden Gate Bridge", "San Francisco, CA", new LatLng(37.8199, -122.4783)));
        favoritePlaces.add(new favouritePlace("Disneyland", "Anaheim, CA", new LatLng(33.8121, -117.9190)));


        favoritePlacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected favorite place
                favouritePlace selectedPlace = favoritePlaces.get(i);
                // Create an intent to open the MapsActivity
                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                // Pass the selected place's location to the MapsActivity
                mapsIntent.putExtra("location", selectedPlace.getLocation());
                // Start the MapsActivity
                startActivity(mapsIntent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PLACE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            LatLng latLng = data.getParcelableExtra("latLng"); // Get the selected location from the result
            String address = getAddressFromLatLng(latLng); // Convert the location to an address
            if (address != null) {
                String name = "Favorite Place " + (favoritePlaces.size() + 1); // Generate a default name for the new place
                favoritePlaces.add(new favouritePlace(name, address, latLng)); // Add the new place to the list
                adapter.notifyDataSetChanged(); // Update the list view to show the new place
            } else {
                Toast.makeText(this, "Unable to get address for selected location", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private static class FavoritePlacesAdapter extends ArrayAdapter<favouritePlace> {

        public FavoritePlacesAdapter(Context context, ArrayList<favouritePlace> places) {
            super(context, 0, places);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            favouritePlace place = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_favourite_place, parent, false);
            }
            TextView nameTextView = convertView.findViewById(R.id.place_name);
            TextView addressTextView = convertView.findViewById(R.id.place_address);
            nameTextView.setText(place.getName());
            addressTextView.setText(place.getAddress());
            return convertView;
        }
    }
    private String getAddressFromLatLng (LatLng latLng){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
