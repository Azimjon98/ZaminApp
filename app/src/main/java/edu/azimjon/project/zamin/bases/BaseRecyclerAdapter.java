package edu.azimjon.project.zamin.bases;

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
import edu.azimjon.project.zamin.adapter.VideoNewsAdapter;
import edu.azimjon.project.zamin.addition.Converters;
import edu.azimjon.project.zamin.databinding.ItemVideoNewsBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.*;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<T> items;
    Context context;

    //header states
    public boolean isLoading = false;
    public boolean hasHeader = false;
    public boolean hasFooter = false;

    //views
    public View headerView;
    public View footerView;



    public BaseRecyclerAdapter(Context context, ArrayList<T> items) {
        super();
        this.context = context;
        this.items = items;
    }

    public void withHeader(View headerView) {
        this.headerView = headerView;
        hasHeader = true;
    }

    public void withFooter(View footerView) {
        this.footerView = footerView;
        hasFooter = true;
    }

    public void removeHeader() {
        hasHeader = false;
    }

    public void removeFooter() {
        hasFooter = false;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0)
            return TYPE_HEADER;
        else if (hasFooter && position == (items.size() - 1)) {
            return TYPE_FOOTER;
        } else if (isLoading && position == (items.size() - 1))
            return TYPE_LOADING;
        else
            return TYPE_ITEM;
    }


    //    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//        lastPosition = i;
//
//        if (hasHeader && i == 0)
//            return;
//        //loading case skips
//        if (isLoading && i == items.size() - 1) {
//            return;
//        }
//
//        final int position = hasHeader ? i - 1 : i;
//
//        VideoNewsAdapter.MyHolderItem myHolder = (VideoNewsAdapter.MyHolderItem) viewHolder;
//        myHolder.binding.setModel(items.get(position));
//
//    }

    //########################################################################

    //TODO: Additional methods


    public void init_items(List<T> items) {
        clear_items();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    public void add_all(List<T> items) {
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
        notifyDataSetChanged();
    }

    public void hideLoading() {
        isLoading = false;
        notifyDataSetChanged();
    }

    //#######################################################


    @Override
    public int getItemCount() {
        int count = items.size();
        if (hasHeader)
            count++;
        if (hasFooter || isLoading)
            count++;

        return count;
    }


    //################################################################

    //TODO: Holders


    public class MyHolderHeader extends RecyclerView.ViewHolder {
        public MyHolderHeader(@NonNull View itemView) {
            super(itemView);
        }
    }


    public class MyLoadingFooter extends RecyclerView.ViewHolder {


        public MyLoadingFooter(View v) {
            super(v);

        }

    }

}
