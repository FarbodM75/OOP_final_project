package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
// Adapter for the recycler view (used in Quarters, Simulator and MissionControl)
public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.ViewHolder> {
    private ArrayList<CrewMember> crewList;
    private ArrayList<CrewMember> selectedCrew = new ArrayList<>();
    private SelectionListener selectionListener;

    // Getter for selected crew
    public ArrayList<CrewMember> getSelectedCrew() {
        return selectedCrew;
    }

    // Interface for selection listener.
    public interface SelectionListener {
        void onSelectionChanged(ArrayList<CrewMember> newSelection);
    }

    // Setters
    public void setSelectionListener(SelectionListener listener) {
        this.selectionListener = listener;
    }
    public CrewAdapter(ArrayList<CrewMember> crewList) {
        this.crewList = crewList;
    }
    public void setCrewList(ArrayList<CrewMember> newList) {
        this.crewList = newList;
        clearSelection(); // reset any selection
    }

    /**
     Clears the selected crew and notifies the adapter
     */
    public void clearSelection() {
        selectedCrew.clear();
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(new ArrayList<>(selectedCrew));
        }
        notifyDataSetChanged(); // refresh the checkboxes
    }
    // ViewHolder class for the recycler view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView poster;
        public TextView crwnamee;
        public TextView crwtypee;
        public TextView crwstatss;
        public CheckBox checkbox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            crwnamee = itemView.findViewById(R.id.crwnamee);
            crwtypee = itemView.findViewById(R.id.crwtypee);
            crwstatss = itemView.findViewById(R.id.crwstatss);
            checkbox = itemView.findViewById(R.id.checkbox);

        }


    }
    @NonNull
    @Override
    public CrewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crew_list_item, parent, false);
        return new ViewHolder(view);
    }

    // Fills the card with the crew info.
    @Override
    public void onBindViewHolder(@NonNull CrewAdapter.ViewHolder holder, int position) {
        CrewMember crew = crewList.get(position);

        holder.crwnamee.setText(crew.getName());
        holder.crwtypee.setText(crew.getType());
        String stats = "Skill: " + crew.getSkill() +
                " | Resilience: " + crew.getResilience() +
                " | Energy: " + crew.getEnergy() + "/" + crew.getMaxEnergy() +
                " | XP: " + crew.getExperience() + "/100";
        holder.crwstatss.setText(stats);
        holder.poster.setImageResource(crew.getPosterResId(holder.itemView.getContext()));
        // Checkbox logic
        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(selectedCrew.contains(crew));
        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // If the checkbox is checked, add the crew to the selected list
            if (isChecked) {
                if (!selectedCrew.contains(crew)) selectedCrew.add(crew);
            }
            else {
                selectedCrew.remove(crew);
            }
            // if the selection listener is set, notify it, because the selection has changed.
            if (selectionListener != null) {
                selectionListener.onSelectionChanged(new ArrayList<>(selectedCrew));
            }

        });
    }


    // Returns the number of items in the list
    @Override
    public int getItemCount() {

        return crewList.size();
    }
}

