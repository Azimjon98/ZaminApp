package edu.azimjon.project.zamin.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.databinding.ItemNewsCategoryBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;

import static edu.azimjon.project.zamin.addition.Constants.KEY_CATEGORY_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_CATEGORY_NAME;

public class CategoryNewsAdapter extends RecyclerView.Adapter<CategoryNewsAdapter.MyHolder> {

    ArrayList<NewsCategoryModel> items;
    Context context;

    private int lastPosition = -1;

    public CategoryNewsAdapter(Context context, ArrayList<NewsCategoryModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_news_category, parent, false);


        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        lastPosition = position;

        holder.binding.setModel(items.get(position));
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        holder.card.clearAnimation();
    }

    public void init_items(List<NewsCategoryModel> items) {
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
        ItemNewsCategoryBinding binding;

        int count = 0;


        public MyHolder(ItemNewsCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.cardNewsCategory.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_CATEGORY_ID, items.get(getAdapterPosition()).getCategoryId());
            bundle.putString(KEY_CATEGORY_NAME, items.get(getAdapterPosition()).getName());
            Navigation.findNavController(v).navigate(R.id.action_global_fragmentSearchResults, bundle);
        }
    }
}