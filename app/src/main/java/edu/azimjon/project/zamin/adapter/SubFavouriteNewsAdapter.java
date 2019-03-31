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
import edu.azimjon.project.zamin.databinding.SubItemFavouriteNewsBinding;
import edu.azimjon.project.zamin.model.FavouriteNewsModel;

public class SubFavouriteNewsAdapter extends RecyclerView.Adapter<SubFavouriteNewsAdapter.MyHolder> {
    ArrayList<FavouriteNewsModel> items;
    Context context;

    int lastPosition = -1;

    public SubFavouriteNewsAdapter(Context context, ArrayList<FavouriteNewsModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubItemFavouriteNewsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.sub_item_favourite_news, parent, false);


        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        lastPosition = i;
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull MyHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        holder.card.clearAnimation();
    }

    public void init_items(List<FavouriteNewsModel> items) {
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
        SubItemFavouriteNewsBinding binding;

        int count = 0;


        public MyHolder(SubItemFavouriteNewsBinding binding) {
            super(binding.getRoot());

        }

        @Override
        public void onClick(View v) {

        }
    }

}
