package com.fortyk.studentapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fortyk.studentapp.R;
import com.fortyk.studentapp.models.ImageOption;

import java.util.List;

public class SingleImageOptionAdapter extends RecyclerView.Adapter<SingleImageOptionAdapter.ViewHolder> {

    public List<ImageOption> optionList;
    Context context;
    private ImageSelectListener listener;
    private int lastCheckedPosition = -1;

    public interface ImageSelectListener {
        void onSelectImage(ImageOption imageOption);
    }

    public SingleImageOptionAdapter(Context context, List<ImageOption> lessons,
                                    ImageSelectListener listener) {
        this.optionList = lessons;
        this.context = context;
        this.listener = listener;

    }

    @NonNull
    @Override
    public SingleImageOptionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SingleImageOptionAdapter.ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.list_single_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SingleImageOptionAdapter.ViewHolder holder, final int position) {

        ImageOption option = optionList.get(position);
        if (option.getDescription() != null && !option.getDescription().isEmpty()) {
            holder.option_description.setText(option.getDescription());
            holder.option_description.setVisibility(View.VISIBLE);
        } else {
            holder.option_description.setVisibility(View.GONE);
        }

        if (lastCheckedPosition == position) {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_bg));
        } else {
            holder.option_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.image_selector_white_bg));
        }

        Glide.with(context)
                .load(option.getImage())
                .into(holder.option_image);


        //  holder.option_description.setImageResource(R.drawable.unlock);

       /* if(locked){
            viewHolder.lessonImage.setImageResource(R.drawable.lock);
        }else{
            viewHolder.lessonImage.setImageResource(R.drawable.unlock);
        }*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedPosition != position) {
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = position;
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);

                    if (listener != null) {
                        listener.onSelectImage(optionList.get(position));
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView option_image;
        public TextView option_description;
        public LinearLayout option_layout;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            option_image = (ImageView) itemLayoutView.findViewById(R.id.option_image);
            option_description = (TextView) itemLayoutView.findViewById(R.id.option_description);
            option_layout = (LinearLayout) itemLayoutView.findViewById(R.id.option_layout);
        }
    }

    public void clearSelected() {
        lastCheckedPosition = -1;
        notifyDataSetChanged();
    }
}