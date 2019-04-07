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
import edu.azimjon.project.zamin.addition.Converters;
import edu.azimjon.project.zamin.databinding.HeaderWindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.ItemNewsMainMediumBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;

public class NewsContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<NewsSimpleModel> items;
    Context context;


    int lastPosition = -1;
    boolean isLoading = false;

    //header
    boolean hasHeader = false;
    View headerView;

    private final static int TYPE_HEADER = 1;
    private final static int TYPE_ITEM = 2;
    private final static int TYPE_LOADING = 3;

    public NewsContentAdapter(Context context, ArrayList<NewsSimpleModel> items) {
        this.context = context;
        this.items = items;
    }

    public void withHeader(View headerView) {
        this.headerView = headerView;
        hasHeader = true;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0) {
            return TYPE_HEADER;
        } else if (isLoading && position == (items.size() - 1))
            return TYPE_LOADING;
        else
            return TYPE_ITEM;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);


        if (i == TYPE_HEADER)
            return new MyHeaderHolder(headerView);
        else if (i == TYPE_ITEM)
            return new MyHolderItem(DataBindingUtil.inflate(
                    inflater,
                    R.layout.item_news_main_medium,
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
        if (hasHeader && i == 0)
            return;
        //loading case skips
        if (isLoading && i == items.size() - 1) {
            return;
        }

        final int position = hasHeader ? i - 1 : i;


        MyHolderItem holder = ((MyHolderItem) viewHolder);
        holder.binding.setModel(items.get(position));

        lastPosition = i;
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


    public class MyHeaderHolder extends RecyclerView.ViewHolder {

        public MyHeaderHolder(View v) {
            super(v);
        }

    }

    public class MyHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemNewsMainMediumBinding binding;

        int count = 0;


        public MyHolderItem(ItemNewsMainMediumBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.clicker.setOnClickListener(this);

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
            Bundle bundle = new Bundle();
            bundle.putString(KEY_NEWS_ID, items.get(getAdapterPosition()).getNewsId());
            Navigation.findNavController(v).navigate(R.id.action_global_fragmentNewsContent, bundle);
        }
    }


    public class MyLoadingHolder extends RecyclerView.ViewHolder {


        public MyLoadingHolder(View v) {
            super(v);

        }

    }

}
