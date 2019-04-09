package edu.azimjon.project.zamin.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AbsListView;

import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.fragment.FragmentNewsFeed;

public class MyOnScrollListener extends RecyclerView.OnScrollListener {

    public interface MyInterface {
        void scrollEnded();
    }

    MyInterface myInterface;
    RecyclerView.Adapter adapter;

    boolean is_scrolling = false;

    int total_items, visible_items, scrollout_items;

    public MyOnScrollListener(RecyclerView.Adapter adapter, MyInterface m) {
        this.adapter = adapter;
        this.myInterface = m;
    }


    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        Log.d(Constants.MY_LOG, "onScrollStateChanged");

        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            is_scrolling = true;
        }

    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        Log.d(Constants.MY_LOG, "onScrolled");

        total_items = recyclerView.getLayoutManager().getItemCount();
        visible_items = recyclerView.getLayoutManager().getChildCount();
        scrollout_items = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        Log.d(Constants.MY_LOG, "total_items: " + total_items + " " +
                "visible_items: " + visible_items + " " +
                "scrollout_items: " + scrollout_items);

        if (is_scrolling && (visible_items + scrollout_items == total_items)) {
            is_scrolling = false;
            //execute function we wanted when end scrolling
            myInterface.scrollEnded();
        }

    }
}