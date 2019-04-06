package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.WindowTopNewsBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentTopNews;

import static edu.azimjon.project.zamin.addition.Constants.MY_LOG;

public class FragmentTopNews extends Fragment implements IFragmentTopNews {

    //TODO: Constants here
    LinearLayoutManager manager;


    //TODO: variables here
    WindowTopNewsBinding binding;
    PresenterTopNews presenterTopNews;

    //adapters
    MediumNewsAdapter mediumNewsAdapter;

    //scrolling variables
    boolean is_scrolling = false;

    int total_items, visible_items, scrollout_items;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterTopNews = new PresenterTopNews(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_top_news, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize adapters and append to lists
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.listTopNews.setLayoutManager(manager);
        mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        binding.listTopNews.setAdapter(mediumNewsAdapter);

        binding.listTopNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                total_items = manager.getItemCount();
                visible_items = manager.getChildCount();
                scrollout_items = manager.findFirstVisibleItemPosition();
                Log.d(Constants.MY_LOG, "total_items: " + total_items + " " +
                        "visible_items: " + visible_items + " " +
                        "scrollout_items: " + scrollout_items);

                if (is_scrolling && (visible_items + scrollout_items == total_items)) {
                    is_scrolling = false;
                    mediumNewsAdapter.showLoading();
                    presenterTopNews.getContinue();
                }
            }
        });


        //*****************************************************************************

        presenterTopNews.init();
    }

    //TODO: override methods


    //#################################################################


    //TODO: all methods from interface


    @Override
    public void addNews(List<NewsSimpleModel> items) {
        binding.getRoot().setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());

        if (mediumNewsAdapter.getItemCount() != 0)
            mediumNewsAdapter.hideLoading();

        mediumNewsAdapter.add_items(items);
    }


    //#################################################################

    //TODO: Additional methods


    //#################################################################


}
