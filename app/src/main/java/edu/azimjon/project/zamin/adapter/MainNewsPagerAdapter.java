package edu.azimjon.project.zamin.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.databinding.ItemNewsCategoryBinding;
import edu.azimjon.project.zamin.databinding.ItemNewsMainLargeBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_MODEL;

public class MainNewsPagerAdapter extends PagerAdapter implements View.OnClickListener {

    Context context;
    int count;
    List<NewsSimpleModel> news;
    static int newsPosition;

    public MainNewsPagerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        ItemNewsMainLargeBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_news_main_large, container, false);
        binding.setModel(news.get(position));

        binding.clicker.setOnClickListener(v -> {
             newsPosition =  position;
            this.onClick(v);
        });



        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public void init_items(List<NewsSimpleModel> news, int count) {
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


    //TODO: - OnclickListener implementations
    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NEWS_ID, news.get(newsPosition).getNewsId());
        bundle.putParcelable(KEY_NEWS_MODEL, news.get(newsPosition));
        Navigation.findNavController(v).navigate(R.id.action_global_fragmentNewsContent, bundle);
    }
}