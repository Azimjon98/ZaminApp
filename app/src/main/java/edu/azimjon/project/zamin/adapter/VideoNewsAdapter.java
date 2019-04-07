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
import edu.azimjon.project.zamin.databinding.ItemNewsCategoryBinding;
import edu.azimjon.project.zamin.databinding.ItemVideoNewsBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

public class VideoNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<NewsSimpleModel> items;
    Context context;

    int lastPosition = -1;

    private final static int TYPE_HEADER = 1;
    private final static int TYPE_ITEM = 2;

    public VideoNewsAdapter(Context context, ArrayList<NewsSimpleModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);

        //header with bottom padding
        if (i == TYPE_HEADER) {
            View header = inflater.inflate(R.layout.header_window_video_inside_media, viewGroup, false);
            header.setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());
            return new MyHolder1(header);
        } else
            return new MyHolder2(DataBindingUtil
                    .inflate(inflater,
                            R.layout.item_video_news,
                            viewGroup,
                            false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        lastPosition = i;

        if (i == 0)
            return;

        MyHolder2 myHolder = (MyHolder2) viewHolder;
        myHolder.binding.setModel(items.get(i));

    }


    public void init_items(List<NewsSimpleModel> items) {
        clear_items();
        this.items.add(new NewsSimpleModel());
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


    @Override
    public int getItemCount() {
        return items.size();
    }


    //################################################################

    //TODO: Holders


    public class MyHolder1 extends RecyclerView.ViewHolder {
        public MyHolder1(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class MyHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemVideoNewsBinding binding;

        int count = 0;


        public MyHolder2(ItemVideoNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

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
}
