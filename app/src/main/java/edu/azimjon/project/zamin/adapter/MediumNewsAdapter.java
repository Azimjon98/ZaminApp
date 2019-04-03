package edu.azimjon.project.zamin.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.databinding.ItemNewsMainMediumBinding;
import edu.azimjon.project.zamin.interfaces.IScrollStateChanged;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.MY_LOG;

public class MediumNewsAdapter extends RecyclerView.Adapter<MediumNewsAdapter.MyHolder> {
    ArrayList<NewsSimpleModel> items;
    Context context;
    IScrollStateChanged scrollStateChanged;

    int lastPosition = -1;

    public MediumNewsAdapter(Context context, ArrayList<NewsSimpleModel> items, IScrollStateChanged scrollStateChanged) {
        this.context = context;
        this.items = items;
        this.scrollStateChanged = scrollStateChanged;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsMainMediumBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_news_main_medium, parent, false);


        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        Log.d(MY_LOG, "onBindViewHolder: " + i);
        myHolder.binding.setModel(items.get(i));


        lastPosition = i;

        if (lastPosition == items.size() - 1) {
            scrollStateChanged.lastItemBinded();
        }
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull MyHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        holder.card.clearAnimation();
    }

    public void init_items(List<NewsSimpleModel> items) {
        clear_items();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public void add_items(List<NewsSimpleModel> items) {
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
        ItemNewsMainMediumBinding binding;

        int count = 0;


        public MyHolder(ItemNewsMainMediumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.clicker.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_NEWS_ID, items.get(getAdapterPosition()).getNewsId());
            Navigation.findNavController(v).navigate(R.id.action_global_fragmentNewsContent, bundle);
        }
    }

}
