package edu.azimjon.project.zamin.bases;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.azimjon.project.zamin.addition.Constants.TYPE_FOOTER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER_NO_INTERNET;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_HEADER_NO_ITEM;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_ITEM;
import static edu.azimjon.project.zamin.addition.Constants.TYPE_LOADING;

//*///////////////////////
//All rights are reserved Numonov Azimjon© 2019
//T is model of items is used in adapter
//
///////////////////////*//
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<T> items;
    public Context context;

    //header states
    public boolean hasHeader = false;
    public boolean hasHeaderNoInternet = false;
    public boolean hasHeaderNoItem = false;
    private boolean isLoading = false;
    private boolean hasFooter = false;

    //views
    public View headerView;
    public View headerNoInternetView;
    public View headerNoItemView;
    public View footerView;

    public BaseRecyclerAdapter(Context context, ArrayList<T> items) {
        super();
        this.context = context;
        this.items = items;
    }

    public void withHeader(View headerView) {
        this.headerView = headerView;

        if (!hasHeader) {
            if (hasHeaderNoItem || hasHeaderNoInternet) {
                hasHeaderNoItem = false;
                hasHeaderNoInternet = false;
                notifyItemRemoved(0);
            }

            hasHeader = true;
            notifyItemInserted(0);
        }


    }

    public void withHeaderNoInternet(View headerNoInternetView) {
        this.headerNoInternetView = headerNoInternetView;

        if (!hasHeaderNoInternet) {
            if (hasHeader || hasHeaderNoItem) {
                hasHeader = false;
                hasHeaderNoItem = false;
                notifyItemRemoved(0);
            }

            hasHeaderNoInternet = true;
            notifyItemInserted(0);
        }
    }

    public void withHeaderNoItem(View headerNoItem) {
        this.headerNoItemView = headerNoItem;

        if (!hasHeaderNoItem) {
            if (hasHeader || hasHeaderNoInternet) {
                hasHeaderNoInternet = false;
                hasHeader = false;
                notifyItemRemoved(0);
            }

            hasHeaderNoItem = true;
            notifyDataSetChanged();
        }
    }

    public void removeHeaders() {
        hasHeader = false;
        hasHeaderNoInternet = false;
        hasHeaderNoItem = false;
        notifyDataSetChanged();
    }



    public void withFooter(View footerView) {
        this.footerView = footerView;

        if (!hasFooter) {
            if (isLoading) {
                isLoading = false;
                notifyItemRemoved(getItemCount() - 1);
            }

            hasFooter = true;
            notifyItemInserted(getItemCount());
        }

    }

    public void removeFooter() {
        if (hasFooter) {
            hasFooter = false;
            notifyItemRemoved(getItemCount() - 1);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (hasHeader && position == 0)
            return TYPE_HEADER;
        else if (hasHeaderNoInternet && position == 0)
            return TYPE_HEADER_NO_INTERNET;
        else if (hasHeaderNoItem && position == 0)
            return TYPE_HEADER_NO_ITEM;
        else if (hasFooter && position == (getItemCount() - 1)) {
            return TYPE_FOOTER;
        } else if (isLoading && position == (getItemCount() - 1))
            return TYPE_LOADING;
        else
            return TYPE_ITEM;
    }

    //########################################################################

    //TODO: Additional methods

    public void clearItems() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public void initItems(List<T> items) {
        clearItems();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        int start = getItemCount();
        this.items.addAll(items);
        notifyItemRangeInserted(start, items.size());
    }


    //TODO: indicator item show/hide when loading data
    public void showLoading() {
        if (!isLoading) {
            if (hasFooter) {
                hasFooter = false;
                notifyItemRemoved(getItemCount() - 1);
            }

            isLoading = true;
            notifyItemInserted(getItemCount());
        }

    }

    public void hideLoading() {
        if (isLoading) {
            isLoading = false;
            notifyItemRemoved(getItemCount());
        }
    }

    //#######################################################

    @Override
    public int getItemCount() {
        int count = items.size();
        if (hasHeader || hasHeaderNoInternet || hasHeaderNoItem)
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
