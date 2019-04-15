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
import edu.azimjon.project.zamin.databinding.ItemAudioNewsBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_MODEL;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_FOOTER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_LOADING;

public class AudioNewsAdapter extends BaseRecyclerAdapter<NewsSimpleModel> {

    public boolean isPlaying = false;
    public int playingMusicId = -1;

    public static interface IMyPlayer {
        void playPressed(NewsSimpleModel m);

        void pausePressed(NewsSimpleModel m);

        void updateItems();
    }

    IMyPlayer myPlayer;

    public AudioNewsAdapter(Context context, ArrayList<NewsSimpleModel> items, IMyPlayer player) {
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
            if (hasHeader)
                position--;

            MyHolderItem myHolder = (MyHolderItem) viewHolder;
            myHolder.binding.setModel(items.get(position));


            //FIXME: change icons
            if (myHolder.binding.getModel().getId() == playingMusicId) {

                if (isPlaying) {
                    myHolder.binding.btnPlay.setImageResource(R.drawable.micon_player_pause);

                } else {
                    myHolder.binding.btnPlay.setImageResource(R.drawable.micon_player_play);

                }
            } else
                myHolder.binding.btnPlay.setImageResource(R.drawable.micon_musci_disabled);
        }


    }


    //#######################################################

    //################################################################

    //TODO: Holders


    public class MyHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemAudioNewsBinding binding;


        public MyHolderItem(ItemAudioNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
//            this.binding.clicker.setOnClickListener(this);

            binding.btnPlay.setOnClickListener(v -> {
                //FIXME: change icons
                if (binding.getModel().getId() == playingMusicId) {
                    if (isPlaying) {
                        isPlaying = false;
                        binding.btnPlay.setImageResource(R.drawable.micon_player_pause);
                        myPlayer.pausePressed(binding.getModel());
                    } else {
                        isPlaying = true;
                        binding.btnPlay.setImageResource(R.drawable.micon_player_play);
                        myPlayer.playPressed(binding.getModel());
                    }
                } else {
                    playingMusicId = binding.getModel().getId();
                    binding.btnPlay.setImageResource(R.drawable.micon_player_play);
                    myPlayer.updateItems();
                }

            });
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
