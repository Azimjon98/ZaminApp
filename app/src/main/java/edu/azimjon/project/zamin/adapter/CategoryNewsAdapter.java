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

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.bases.BaseRecyclerAdapter;
import edu.azimjon.project.zamin.bases.MyBaseHolder;
import edu.azimjon.project.zamin.databinding.ItemNewsCategoryBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;

import static edu.azimjon.project.zamin.addition.Constants.KEY_CATEGORY_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_TOOLBAR_NAME;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_WHERE;
import static edu.azimjon.project.zamin.addition.Constants.SEARCH_CATEGORY;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_FOOTER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER_NO_INTERNET;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_LOADING;

public class CategoryNewsAdapter extends BaseRecyclerAdapter<NewsCategoryModel> {

    public CategoryNewsAdapter(Context context, ArrayList<NewsCategoryModel> items) {
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
        else if (i == TYPE_HEADER_NO_INTERNET)
            return new MyBaseHolder(headerNoInternetView);
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
                            R.layout.item_news_category,
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

        }


    }


    public class MyHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemNewsCategoryBinding binding;

        int count = 0;


        public MyHolderItem(ItemNewsCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.cardNewsCategory.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_SEARCH_ID, binding.getModel().getCategoryId());
            bundle.putString(KEY_SEARCH_TOOLBAR_NAME, binding.getModel().getName());
            bundle.putInt(KEY_SEARCH_WHERE, SEARCH_CATEGORY);
            Navigation.findNavController(v).navigate(R.id.action_global_fragmentSearchResults, bundle);
        }
    }
}