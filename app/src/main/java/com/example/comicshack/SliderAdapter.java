package com.example.comicshack;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager;
    private FragmentActivity fragmentActivity;

    public SliderAdapter(List<SliderItem> sliderItems, ViewPager2 viewPager, FragmentActivity activity) {
        this.sliderItems = sliderItems;
        this.viewPager = viewPager;
        this.fragmentActivity = activity;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));

        holder.itemView.setOnClickListener(view -> {
            if (fragmentActivity != null)
            {
                Intent intent = new Intent(fragmentActivity, ReadComicActivity.class);
                intent.putExtra("index", holder.getAdapterPosition());
                view.getContext().startActivity((intent));
            }
        });

        holder.itemView.setLongClickable(true);
        fragmentActivity.registerForContextMenu(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public List<SliderItem> getSliderItems() {
        return sliderItems;
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SliderItem sliderItem) {
            imageView.setImageBitmap(sliderItem.getImage());
        }

    }
}
