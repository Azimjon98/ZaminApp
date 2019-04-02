package edu.azimjon.project.zamin.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.databinding.ItemAudioNewsBinding;
import edu.azimjon.project.zamin.databinding.ItemGalleryNewsBinding;
import edu.azimjon.project.zamin.databinding.ItemNewsCategoryBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.util.MyUtil;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyHolder> {

    ArrayList<NewsSimpleModel> items;
    Context context;

    int lastPosition = -1;

    public GalleryAdapter(Context context, ArrayList<NewsSimpleModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGalleryNewsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_audio_news, parent, false);


        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        lastPosition = i;

        if (i == items.size() - 1){
            myHolder.binding.getRoot().setPadding(0,0,0, MyUtil.dpToPx(64));
        }
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull MyHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        holder.card.clearAnimation();
    }

    public void init_items(List<NewsSimpleModel> items){
        clear_items();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public void clear_items() {
        this.items.clear();
        this.notifyDataSetChanged();
    }





    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemGalleryNewsBinding binding;

        int count = 0;


        public MyHolder(ItemGalleryNewsBinding binding) {
            super(binding.getRoot());


        }


        @Override
        public void onClick(View v) {

        }
    }
}
