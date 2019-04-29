package edu.azimjon.project.zamin.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.addition.Converters;
import edu.azimjon.project.zamin.databinding.ItemNewsCategoryBinding;
import edu.azimjon.project.zamin.databinding.ItemNewsMainLargeBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_MODEL;

public class MainNewsPagerAdapter extends PagerAdapter {

    Context context;
    int count;
    public List<NewsSimpleModel> news;
    public ItemNewsMainLargeBinding[] bindings = new ItemNewsMainLargeBinding[10];

    public MainNewsPagerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        ItemNewsMainLargeBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_news_main_large, null, false);
        binding.setModel(news.get(position));
        updateItem(binding);
        bindings[position] = binding;


        binding.clicker.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_NEWS_ID, binding.getModel().getNewsId());
            bundle.putParcelable(KEY_NEWS_MODEL,
                    Converters.fromSimpleNewstoContentNews(binding.getModel()));
            Navigation.findNavController(v).navigate(R.id.action_global_fragmentNewsContent, bundle);
        });

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
                                    .insert(Converters
                                            .fromSimpleNewstoFavouriteNews(binding.getModel()));
                        }
                    }).start();
                }
        );


        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    public void updateItem(ItemNewsMainLargeBinding binding){
        if(binding == null )
            return;
        if (binding.getModel() == null)
            return;

        List<String> allFavouriteIds = NavigationActivity.getFavouritesIds();
        if (allFavouriteIds.contains(binding.getModel().getNewsId())) {
            binding.getModel().setWished(true);
        }else{
            binding.getModel().setWished(false);
        }

        //change Icon state when navigating
        binding.favouriteIcon.setImageResource(
                binding.getModel().isWished() ?
                        R.drawable.bookmark_active :
                        R.drawable.bookmark_inactive);

    }


    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public void init_items(List<NewsSimpleModel> news, int count) {
        this.count = 0;
        notifyDataSetChanged();

        this.count = count;
        this.news = news;
        notifyDataSetChanged();
    }

    //TODO: - Pager implementations


    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }


}
