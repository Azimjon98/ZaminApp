package edu.azimjon.project.zamin.adapter;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.Converters;
import edu.azimjon.project.zamin.databinding.ItemNewsCategoryBinding;
import edu.azimjon.project.zamin.databinding.ItemNewsMainLargeBinding;
import edu.azimjon.project.zamin.databinding.ItemNewsMainSmallBinding;
import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;

public class LargeNewsAdapter extends RecyclerView.Adapter<LargeNewsAdapter.MyHolder> {
    ArrayList<NewsSimpleModel> items;
    Context context;

    //list of ids which are favourite
    List<Integer> allFavouriteIds;

    int lastPosition = -1;

    public LargeNewsAdapter(Context context, ArrayList<NewsSimpleModel> items) {
        this.context = context;
        this.items = items;

//        //intit favourite ids
//        allFavouriteIds = new ArrayList<>();
//        FavouriteNewsDatabase
//                .getInstance(context)
//                .getDao()
//                .getAllIds()
//                .observe((LifecycleOwner) this, new Observer<List<Integer>>() {
//                    @Override
//                    public void onChanged(@Nullable List<Integer> integers) {
//                        Log.d(Constants.MY_LOG, "in favourite get all ids");
//                        allFavouriteIds.clear();
//                        allFavouriteIds.addAll(integers);
//                    }
//                });

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsMainLargeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_news_main_large, parent, false);


        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.binding.setModel(items.get(i));

        lastPosition = i;
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

    public void clear_items() {
        this.items.clear();
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemNewsMainLargeBinding binding;

        int count = 0;


        public MyHolder(ItemNewsMainLargeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.clicker.setOnClickListener(this);

//            binding.favouriteIcon.setOnClickListener(v -> {
//                        boolean isWished = binding.getModel().isWished();
//                        binding.getModel().setWished(!binding.getModel().isWished());
//
//
//                        //delete or inser news to favourites in another thread
//                        new Thread(() -> {
//                            if (isWished) {
//                                FavouriteNewsDatabase.getInstance(context)
//                                        .getDao()
//                                        .delete(binding.getModel().getNewsId());
//                            } else {
////                                FavouriteNewsDatabase.getInstance(context)
////                                        .getDao()
////                                        .insert(Converters
////                                                .fromSimpleNewstoFavouriteNews(binding.getModel()));
//                            }
//                        }).start();
//                    }
//            );

        }


        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_NEWS_ID, items.get(getAdapterPosition()).getNewsId());
            Navigation.findNavController(v).navigate(R.id.action_global_fragmentNewsContent, bundle);
        }
    }
}