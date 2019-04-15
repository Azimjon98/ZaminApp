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
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.bases.BaseRecyclerAdapter;
import edu.azimjon.project.zamin.bases.MyBaseHolder;
import edu.azimjon.project.zamin.databinding.ItemFavouriteNewsBinding;
import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_MODEL;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_FOOTER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_LOADING;

public class FavouriteNewsAdapter extends BaseRecyclerAdapter<FavouriteNewsModel> {

    public FavouriteNewsAdapter(Context context, ArrayList<FavouriteNewsModel> items) {
        super(context, items);

        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);

        //header with bottom padding
        if (i == TYPE_HEADER)
            return new MyBaseHolder(headerView);
        else if (i == TYPE_FOOTER)
            return new MyBaseHolder(footerView);
        else if (i == TYPE_LOADING)
            return new MyBaseHolder(inflater.inflate(
                    R.layout.item_loading,
                    viewGroup,
                    false));
        else
            return new MyHolderItem(DataBindingUtil
                    .inflate(inflater,
                            R.layout.item_favourite_news,
                            viewGroup,
                            false));


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MyHolderItem) {
            int position = i;
            if (hasHeader)
                position--;

            MyHolderItem myHolder = (MyHolderItem) viewHolder;
            myHolder.binding.setModel(items.get(position));
            myHolder.binding.newsNumber.setText((i + 1) > 9 ? String.valueOf(i + 1) : "0" + i);

            List<String> allFavouriteIds;
            allFavouriteIds = NavigationActivity.getFavouritesIds();
            if (allFavouriteIds.contains(myHolder.binding.getModel().getNewsId())) {
                myHolder.binding.getModel().setWished(true);
            }

            myHolder.binding.favouriteIcon.setImageResource(
                    myHolder.binding.getModel().isWished() ?
                            R.drawable.bookmark_active :
                            R.drawable.bookmark_inactive);
        }


    }

    //################################################################

    //TODO: Hoders




    public class MyHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemFavouriteNewsBinding binding;
        int count = 0;


        public MyHolderItem(ItemFavouriteNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.clicker.setOnClickListener(this);


            binding.favouriteIcon.setOnClickListener(v -> {
                        boolean isWished = binding.getModel().isWished();
                        binding.getModel().setWished(!binding.getModel().isWished());

                binding.favouriteIcon.setImageResource(
                        binding.getModel().isWished() ?
                                R.drawable.bookmark_active :
                                R.drawable.bookmark_inactive);
                        //delete or inser news to favourites in another thread
                        new Thread(() -> {
                            if (isWished) {
                                FavouriteNewsDatabase.getInstance(context)
                                        .getDao()
                                        .delete(binding.getModel().getNewsId());
                            } else {
                                FavouriteNewsDatabase.getInstance(context)
                                        .getDao()
                                        .insert(binding.getModel());
                            }
                        }).start();
                    }
            );

        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_NEWS_ID, binding.getModel().getNewsId());
            bundle.putParcelable(KEY_NEWS_MODEL, binding.getModel());
            Navigation.findNavController(v).navigate(R.id.action_global_fragmentNewsContent, bundle);
        }
    }


}
