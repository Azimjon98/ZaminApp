package edu.azimjon.project.zamin.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.bases.BaseRecyclerAdapter;
import edu.azimjon.project.zamin.bases.MyBaseHolder;
import edu.azimjon.project.zamin.databinding.ItemAudioNewsBinding;
import edu.azimjon.project.zamin.model.SimpleNewsModel;

import static edu.azimjon.project.zamin.addition.Constants.TYPE_FOOTER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER_NO_INTERNET;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER_NO_ITEM;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_LOADING;

public class AudioNewsAdapter extends BaseRecyclerAdapter<SimpleNewsModel> {

    public boolean isPlaying = false;
    public String playingMusicId;

    public static interface IMyPlayer {
        void playPressed(SimpleNewsModel m);

        void pausePressed(SimpleNewsModel m);

        void updateItems(SimpleNewsModel m, int positionTo);

    }

    IMyPlayer myPlayer;

    public AudioNewsAdapter(Context context, ArrayList<SimpleNewsModel> items, IMyPlayer player) {
        super(context, items);

        this.context = context;
        this.items = items;
        this.myPlayer = player;
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
        else if (i == TYPE_HEADER_NO_ITEM)
            return new MyBaseHolder(headerNoItemView);
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
                            R.layout.item_audio_news,
                            viewGroup,
                            false));


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MyHolderItem) {
            int position = i;
            if (hasHeader || hasHeaderNoInternet || hasHeaderNoItem)
                position--;

            MyHolderItem myHolder = (MyHolderItem) viewHolder;
            myHolder.binding.setModel(items.get(position));


            //FIXME: change icons
            if (myHolder.binding.getModel().getNewsId().equals(playingMusicId)) {
                if (isPlaying) {
                    myHolder.binding.btnPlay.setImageResource(R.drawable.micon_player_pause);
                } else {
                    myHolder.binding.btnPlay.setImageResource(R.drawable.micon_player_play);
                }
                myHolder.binding.clicker.setBackgroundResource(R.color.ripple_color);
            } else {
                myHolder.binding.btnPlay.setImageResource(R.drawable.micon_musci_disabled);
                myHolder.binding.clicker.setBackgroundResource(R.color.white);
            }

        }


    }


    //#######################################################

    //TODO: Additional methods

    public void nextMusic(String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getNewsId().equals(id)) {
                if (i != items.size() - 1) {
                    isPlaying = true;
                    SimpleNewsModel model = items.get(i + 1);
                    playingMusicId = model.getNewsId();
                    myPlayer.updateItems(model, i + 2);
                    notifyItemChanged(Integer.valueOf(i + 1));
                    notifyItemChanged(Integer.valueOf(i + 2));
                }
                break;
            }
        }

    }

    public void prevMusic(String id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getNewsId().equals(id)) {
                if (i != 0) {
                    isPlaying = true;
                    SimpleNewsModel model = items.get(i - 1);
                    playingMusicId = model.getNewsId();
                    myPlayer.updateItems(model, i);
                    notifyItemChanged(Integer.valueOf(i + 1));
                    notifyItemChanged(Integer.valueOf(i));
                }
                break;
            }
        }
    }

    //################################################################

    //TODO: Holders


    public class MyHolderItem extends RecyclerView.ViewHolder {
        ItemAudioNewsBinding binding;


        public MyHolderItem(ItemAudioNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnPlay.setOnClickListener(v -> {
                binding.clicker.setBackgroundResource(R.color.ripple_color);

                if (binding.getModel().getNewsId().equals(playingMusicId)) {
                    if (isPlaying) {
                        isPlaying = false;
                        binding.btnPlay.setImageResource(R.drawable.micon_player_play);
                        myPlayer.pausePressed(binding.getModel());
                    } else {
                        isPlaying = true;
                        binding.btnPlay.setImageResource(R.drawable.micon_player_pause);
                        myPlayer.playPressed(binding.getModel());
                    }

                } else {
                    isPlaying = true;
                    playingMusicId = binding.getModel().getNewsId();
                    binding.btnPlay.setImageResource(R.drawable.micon_player_pause);
                    myPlayer.updateItems(binding.getModel(), getAdapterPosition());
                }

            });
        }

    }


}
