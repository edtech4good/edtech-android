package com.fortyk.studentapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortyk.studentapp.R;
import com.fortyk.studentapp.models.School;

import java.util.ArrayList;
import java.util.List;

public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.ViewHolder> implements Filterable {

    private List<School> schoolList;
    private List<School> schoolListFiltered;
    private Context context;
    private SelectedSchoolItemListener selectedSchoolItemListener;

    public interface SelectedSchoolItemListener {
        void onClickItem(School school);
    }

    public SchoolListAdapter(Context context, List<School> schoolList, SelectedSchoolItemListener selectedSchoolItemListener) {
        this.schoolList = schoolList;
        this.schoolListFiltered = schoolList;
        this.context = context;
        this.selectedSchoolItemListener = selectedSchoolItemListener;
    }

    @NonNull
    @Override
    public SchoolListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SchoolListAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.school_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolListAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.title.setText(schoolListFiltered.get(position).getSchoolname());

        viewHolder.itemView.setOnClickListener(v -> {
            if (selectedSchoolItemListener != null) {
                selectedSchoolItemListener.onClickItem(schoolListFiltered.get(viewHolder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return schoolListFiltered.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            title = (TextView) itemLayoutView.findViewById(R.id.title);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    schoolListFiltered = schoolList;
                } else {
                    List<School> filteredList = new ArrayList<>();
                    for (School row : schoolList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSchoolname().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    schoolListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = schoolListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                schoolListFiltered = (ArrayList<School>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}