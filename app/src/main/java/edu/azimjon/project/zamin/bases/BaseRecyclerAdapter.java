package edu.azimjon.project.zamin.bases;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.AudioNewsAdapter;
import edu.azimjon.project.zamin.adapter.NewsFeedAdapter;
import edu.azimjon.project.zamin.adapter.VideoNewsAdapter;
import edu.azimjon.project.zamin.addition.Converters;
import edu.azimjon.project.zamin.databinding.ItemNewsMainMediumBinding;
import edu.azimjon.project.zamin.databinding.ItemVideoNewsBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.*;

//*///////////////////////
//All rights are reserved Numonov Azimjon© 2019
//T is model of items is used in adapter
//
///////////////////////*//
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<T> items;
    public Context context;

    //header states
    public boolean isLoading = false;
    public boolean hasHeader = false;
    public boolean hasHeaderNoInternet = false;
    public boolean hasFooter = false;

    //views
    public View headerView;
    public View headerNoInternetView;
    public View footerView;


    public BaseRecyclerAdapter(Context context, ArrayList<T> items) {
        super();
        this.context = context;
        this.items = items;
    }

    public void withHeader(View headerView) {
        hasHeaderNoInternet = false;
        this.headerView = headerView;
        hasHeader = true;
        notifyDataSetChanged();
    }

    public void withHeaderNoInternet(View headerView) {
        hasHeaderNoInternet = true;
        this.headerNoInternetView = headerView;
        if (hasHeader) {
            hasHeader = false;
            notifyItemChanged(0);
        } else {
            notifyItemInserted(0);
        }
    }

    public void withFooter(View footerView) {
        this.footerView = footerView;
        hasFooter = true;
        notifyItemInserted(getItemCount());
    }

    public void removeHeaders() {
        if (hasHeaderNoInternet || hasHeader) {
            hasHeader = false;
            hasHeaderNoInternet = false;
            notifyDataSetChanged();
        }
    }

    public void removeFooter() {
        if (hasFooter) {
            hasFooter = false;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (hasHeader && position == 0)
            return TYPE_HEADER;
        else if (hasHeaderNoInternet && position == 0)
            return TYPE_HEADER_NO_INTERNET;
        else if (hasFooter && position == (getItemCount() - 1)) {
            return TYPE_FOOTER;
        } else if (isLoading && position == (getItemCount() - 1))
            return TYPE_LOADING;
        else
            return TYPE_ITEM;
    }

    //########################################################################

    //TODO: Additional methods

    public void clear_items() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public void init_items(List<T> items) {
        clear_items();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void add_all(List<T> items) {
        int start = getItemCount();
        this.items.addAll(items);
        notifyItemRangeInserted(start, items.size());
    }


    //TODO: indicator item show/hide when loading data
    public void showLoading() {
        isLoading = true;
        notifyItemInserted(getItemCount());
    }

    public void hideLoading() {
        isLoading = false;
        notifyItemRemoved(getItemCount());
    }

    //#######################################################

    @Override
    public int getItemCount() {
        int count = items.size();
        if (hasHeader || hasHeaderNoInternet)
            count++;
        if (hasFooter || isLoading)
            count++;

        return count;
    }

    //################################################################

    //TODO: ItemTouchHelper methods

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


}
