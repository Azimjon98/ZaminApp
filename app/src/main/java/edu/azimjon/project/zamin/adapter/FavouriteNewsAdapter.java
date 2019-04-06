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
import edu.azimjon.project.zamin.databinding.ItemFavouriteNewsBinding;
import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;

public class FavouriteNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FavouriteNewsModel> items;
    Context context;

    int lastPosition = -1;

    private final static int TYPE_HEADER = 1;
    private final static int TYPE_ITEM = 2;

    public FavouriteNewsAdapter(Context context, ArrayList<FavouriteNewsModel> items) {
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

        if (i == TYPE_HEADER)
            return new MyHolder1(inflater.inflate(R.layout.header_window_favourites, viewGroup, false));
        else
            return new MyHolder2(DataBindingUtil
                    .inflate(inflater,
                            R.layout.item_favourite_news,
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


    public void init_items(List<FavouriteNewsModel> items) {
        clear_items();
        this.items.add(new FavouriteNewsModel());
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

    //TODO: Hoders


    public class MyHolder1 extends RecyclerView.ViewHolder {
        public MyHolder1(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class MyHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemFavouriteNewsBinding binding;
        int count = 0;


        public MyHolder2(ItemFavouriteNewsBinding binding) {
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
                                        .insert(binding.getModel());
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


}
