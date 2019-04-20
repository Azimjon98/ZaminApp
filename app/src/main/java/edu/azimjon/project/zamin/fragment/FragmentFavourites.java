package edu.azimjon.project.zamin.fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.FavouriteNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.WindowFavouritesBinding;
import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterFavouriteNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentFavouriteNews;
import edu.azimjon.project.zamin.room.dao.FavouriteNewsDao;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;

public class FragmentFavourites extends Fragment implements IFragmentFavouriteNews {

    //TODO: Constants here
    LinearLayoutManager manager;


    //TODO: variables here
    WindowFavouritesBinding binding;
    //    PresenterFavouriteNews presenterFavouriteNews;
    View viewHeader;
    View viewHeaderNoItem;

    //adapters
    FavouriteNewsAdapter favouriteNewsAdapter;

    //TODO room components
    FavouriteNewsDao dao;
    LiveData<List<FavouriteNewsModel>> allData;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dont need any presenter yet
//        presenterFavouriteNews = new PresenterFavouriteNews(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.window_favourites, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize adapters and append to lists
        if (manager == null) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.listFavourite.setLayoutManager(manager);
            favouriteNewsAdapter = new FavouriteNewsAdapter(getContext(), new ArrayList<FavouriteNewsModel>());
            viewHeader = LayoutInflater.from(getContext())
                    .inflate(
                            R.layout.header_window_favourites,
                            binding.listFavourite,
                            false);
            viewHeaderNoItem = LayoutInflater.from(getContext())
                    .inflate(
                            R.layout.header_window_favourites_noitem,
                            binding.listFavourite,
                            false);
            favouriteNewsAdapter.withHeader(viewHeader);
            binding.listFavourite.setAdapter(favouriteNewsAdapter);
            binding.getRoot().setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());
            binding.listFavourite.setHasFixedSize(true);

        }

        //*****************************************************************************

        //room init
        initRoom();

        //TODO: init locales
        ((TextView) viewHeader.findViewById(R.id.text_title)).setText(MyUtil.getLocalizedString(getContext(), R.string.title_favourites));
        ((TextView) viewHeader.findViewById(R.id.text_1)).setText(MyUtil.getLocalizedString(getContext(), R.string.message_favourites));
        ((TextView) viewHeaderNoItem.findViewById(R.id.text_title)).setText(MyUtil.getLocalizedString(getContext(), R.string.title_favourites));
        ((TextView) viewHeaderNoItem.findViewById(R.id.text_1)).setText(MyUtil.getLocalizedString(getContext(), R.string.text_no_favourites));

    }

    //TODO: override methods


    //#################################################################


    //TODO: all methods from interface


    @Override
    public void initFavourites(List<FavouriteNewsModel> items) {
        favouriteNewsAdapter.init_items(items);
    }


    //#################################################################

    //TODO: Additional methods

    //initialize live data and observable here
    void initRoom() {
        dao = FavouriteNewsDatabase.getInstance(getContext()).getDao();
        allData = dao.getAll();
        allData.observe(this, new Observer<List<FavouriteNewsModel>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteNewsModel> tourModels) {
                Log.d(Constants.MY_LOG, "in favourite onChanged");
                if (tourModels.size() == 0)
                    favouriteNewsAdapter.withHeaderNoInternet(viewHeaderNoItem);
                else
                    favouriteNewsAdapter.withHeader(viewHeader);

                initFavourites(tourModels);
            }
        });
    }

    //#################################################################

}
