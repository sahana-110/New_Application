package com.example.new_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.ViewHolder> {
    private List<Facility> facilities;
    private Map<String, List<String>> exclusionMap;
    private Map<String, String> selectedOptions;

    public FacilityAdapter(List<Facility> facilities) {
        this.facilities = facilities;
        this.exclusionMap = createExclusionMap();
        this.selectedOptions = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facility, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Facility facility = facilities.get(position);
        holder.facilityNameTextView.setText(facility.getName());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.apartment);

        Glide.with(holder.itemView)
                .load(facility.getIconUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                .into(holder.facilityIconImageView);

        List<String> optionNames = new ArrayList<>();
        for (FacilityOption option : facility.getOptions()) {
            optionNames.add(option.getName());
        }

        ArrayAdapter<String> optionsAdapter = new ArrayAdapter<>(holder.itemView.getContext(),
                android.R.layout.simple_spinner_item, optionNames);
        holder.facilityOptionsSpinner.setAdapter(optionsAdapter);

        holder.facilityOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = optionNames.get(position);
                facility.setSelectedOption(selectedOption);
                selectedOptions.put(facility.getName(), selectedOption);

                if (!isCombinationAllowed()) {
                    // Reset the selection to a valid option
                    resetSpinnerSelection(holder.facilityOptionsSpinner, facility);
                    Toast.makeText(holder.itemView.getContext(), "Invalid combination selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    @Override
    public int getItemCount() {
        return facilities.size();
    }

    /*private boolean isCombinationAllowed() {
        for (Map.Entry<String, List<String>> entry : exclusionMap.entrySet()) {
            String facilityName = entry.getKey();
            List<String> exclusionOptions = entry.getValue();

            String selectedOption = selectedOptions.get(facilityName);
            if (selectedOption != null && exclusionOptions.contains(selectedOption)) {
                return false; // Invalid combination
            }
        }

        return true; // Combination is allowed
    }*/

    private boolean isCombinationAllowed() {
        boolean isValidCombination = true;

        // Check the selected options for exclusion combinations
        String propertyType = selectedOptions.get("Property Type");
        String numberOfRooms = selectedOptions.get("Number of Rooms");
        String otherFacilitiesOption = selectedOptions.get("Other Facilities");

        // Exclusion combination 1: Property Type: Land and Number of Rooms: 1 to 3 rooms
        if (propertyType != null && propertyType.equals("Land") && numberOfRooms != null && numberOfRooms.equals("1 to 3 Rooms")) {
            isValidCombination = false;
        }

        // Exclusion combination 2: Property Type: Boat House and Other Facilities: Garage
        if (propertyType != null && propertyType.equals("Boat House")) {
            if (otherFacilitiesOption != null && otherFacilitiesOption.equals("Garage")) {
                isValidCombination = false;
            }
        }

        return isValidCombination;
    }




    private void resetSpinnerSelection(Spinner spinner, Facility facility) {
        int position = getPositionOfValidOption(facility);
        if (position != -1) {
            spinner.setSelection(position);
        }
    }

    private int getPositionOfValidOption(Facility facility) {
        int position = -1;
        List<FacilityOption> options = facility.getOptions();
        for (int i = 0; i < options.size(); i++) {
            FacilityOption option = options.get(i);
            facility.setSelectedOption(option.getValue());
            selectedOptions.put(facility.getName(), option.getValue());

            if (isCombinationAllowed()) {
                position = i;
                break;
            }
        }
        return position;
    }

    private Map<String, List<String>> createExclusionMap() {
        Map<String, List<String>> exclusionMap = new HashMap<>();

        // Exclusion combination 1: Property Type: Land and Number of Rooms: 1 to 3 rooms
        List<String> exclusionOptions1 = new ArrayList<>();
        exclusionOptions1.add("Land");
        exclusionOptions1.add("1 to 3 Rooms");
        exclusionMap.put("Property Type", exclusionOptions1);
        exclusionMap.put("Number of Rooms", exclusionOptions1);

        // Exclusion combination 2: Property Type: Boat House and Other Facilities: Garage
        List<String> exclusionOptions2 = new ArrayList<>();
        exclusionOptions2.add("Boat House");
        exclusionOptions2.add("Garage");
        exclusionMap.put("Property Type", exclusionOptions2);
        exclusionMap.put("Other Facilities", exclusionOptions2);

        return exclusionMap;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView facilityNameTextView;
        public ImageView facilityIconImageView;
        public Spinner facilityOptionsSpinner;

        public ViewHolder(View itemView) {
            super(itemView);
            facilityNameTextView = itemView.findViewById(R.id.facilityNameTextView);
            facilityIconImageView = itemView.findViewById(R.id.facilityIconImageView);
            facilityOptionsSpinner = itemView.findViewById(R.id.facilityOptionsSpinner);
        }
    }
}
