package com.example.lab_simran_c0870768_android;

import com.google.android.gms.maps.model.LatLng;

public class favouritePlace {
        private String name;
        private String address;
        private LatLng location;

        public favouritePlace(String name, String address, LatLng location) {
            this.name = name;
            this.address = address;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public LatLng getLocation() {
            return location;
        }
    }


