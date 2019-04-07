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
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.ItemAudioNewsBinding;
import edu.azimjon.project.zamin.databinding.ItemNewsCategoryBinding;
import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

public class AudioNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<NewsSimpleModel> items;
    Context context;

    int lastPosition = -1;

    private final static int TYPE_HEADER = 1;
    private final static int TYPE_ITEM = 2;

    public AudioNewsAdapter(Context context, ArrayList<NewsSimpleModel> items) {
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
            View header = inflater.inflate(R.layout.header_window_audio_inside_media, viewGroup, false);
            header.setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());
            return new MyHolder1(header);
        } else
            return new MyHolder2(DataBindingUtil
                    .inflate(inflater,
                            R.layout.item_audio_news,
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
        ItemAudioNewsBinding binding;

        int count = 0;


        public MyHolder2(ItemAudioNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.clicker.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {


        }
    }
}
