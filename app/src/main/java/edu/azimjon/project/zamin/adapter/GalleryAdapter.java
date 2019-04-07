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
import edu.azimjon.project.zamin.addition.Converters;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.ItemAudioNewsBinding;
import edu.azimjon.project.zamin.databinding.ItemGalleryNewsBinding;
import edu.azimjon.project.zamin.databinding.ItemNewsCategoryBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<NewsSimpleModel> items;
    Context context;

    int lastPosition = -1;
    boolean isLoading = false;

    //header
    boolean hasHeader = false;
    View headerView;

    //Constants
    private final static int TYPE_HEADER = 1;
    private final static int TYPE_ITEM = 2;
    private final static int TYPE_LOADING = 3;

    public GalleryAdapter(Context context, ArrayList<NewsSimpleModel> items) {
        this.context = context;
        this.items = items;
    }

    public void withHeader(View headerView) {
        this.headerView = headerView;
        hasHeader = true;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0)
            return TYPE_HEADER;
        else if (isLoading && position == (items.size() - 1))
            return TYPE_LOADING;
        else
            return TYPE_ITEM;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);

        //header with bottom padding
        if (i == TYPE_HEADER)
            return new MyHolder1(headerView);
        else if (i == TYPE_ITEM)
            return new MyHolder2(DataBindingUtil
                    .inflate(inflater,
                            R.layout.item_gallery_news,
                            viewGroup,
                            false));
        else
            return new MyLoadingHolder(inflater.inflate(
                    R.layout.item_loading,
                    viewGroup,
                    false));


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        lastPosition = i;

        if (hasHeader && i == 0)
            return;
        //loading case skips
        if (isLoading && i == items.size() - 1) {
            return;
        }

        final int position = hasHeader ? i - 1 : i;

        MyHolder2 myHolder = (MyHolder2) viewHolder;
        myHolder.binding.setModel(items.get(position));

    }


    public void init_items(List<NewsSimpleModel> items) {
        clear_items();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public void add_all(List<NewsSimpleModel> items) {
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public void clear_items() {
        this.items.clear();
        this.notifyDataSetChanged();
    }

    //TODO: indicator item show/hide when loading data
    public void showLoading() {
        isLoading = true;
        items.add(new NewsSimpleModel());
        notifyDataSetChanged();
    }

    public void hideLoading() {
        isLoading = false;
        items.remove(items.size() - 1);
        notifyDataSetChanged();
    }

    //#######################################################


    @Override
    public int getItemCount() {
        return hasHeader ? items.size() + 1 : items.size();
    }

    //################################################################

    //TODO: Holders


    public class MyHolder1 extends RecyclerView.ViewHolder {
        public MyHolder1(@NonNull View itemView) {
            super(itemView);
        }
    }


    public class MyHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemGalleryNewsBinding binding;

        int count = 0;


        public MyHolder2(ItemGalleryNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.clicker.setOnClickListener(this);

            binding.favouriteIcon.setOnClickListener(v -> {
                        boolean isWished = binding.getModel().isWished();
                        binding.getModel().setWished(!binding.getModel().isWished());


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
        }


        @Override
        public void onClick(View v) {

        }
    }

    public class MyLoadingHolder extends RecyclerView.ViewHolder {


        public MyLoadingHolder(View v) {
            super(v);

        }

    }
}
