package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.databinding.WindowTopNewsBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentTopNews;

import static edu.azimjon.project.zamin.addition.Constants.MY_LOG;

public class FragmentTopNews extends Fragment implements IFragmentTopNews {

    //TODO: Constants here


    //TODO: variables here
    WindowTopNewsBinding binding;
    PresenterTopNews presenterTopNews;

    //adapters
    MediumNewsAdapter mediumNewsAdapter;


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

        binding.listTopNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>(),
                () -> {
                    Log.d(MY_LOG, "onScrolled");
                    presenterTopNews.getContinue();
                });
        binding.listTopNews.setAdapter(mediumNewsAdapter);


        //*****************************************************************************

        presenterTopNews.init();
    }

    //TODO: override methods


    //#################################################################


    //TODO: all methods from interface


    @Override
    public void addNews(List<NewsSimpleModel> items) {
        mediumNewsAdapter.add_items(items);

    }


    //#################################################################

    //TODO: Additional methods


    //#################################################################


}
