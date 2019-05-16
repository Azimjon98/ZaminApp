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

import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.FavouriteNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.databinding.WindowFavouritesBinding;
import edu.azimjon.project.zamin.events.EventFavouriteChanged;
import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.model.FavouriteNewsModel;
import edu.azimjon.project.zamin.mvp.view.IFragmentFavouriteNews;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import edu.azimjon.project.zamin.room.dao.FavouriteNewsDao;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;

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
    List<FavouriteNewsModel> allData;


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
//            binding.listFavourite.setItemAnimator(null);
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

        } else {
            reloadContent();
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

    private void reloadContent() {
        if (binding == null || favouriteNewsAdapter == null)
            return;


        for (FavouriteNewsModel model : allData){
//            SVProgressHUD.show()
//            contentUpdating = true
            updateItem(model);
        }

//        for favourite in favourites!{
//        if favourite.lastLocale != UserDefaults.getLocale(){
//            SVProgressHUD.show()
//            contentUpdating = true
//            updateItem(model: favourite)
//            return
//        }
//        }
    }

    private void updateItem(FavouriteNewsModel model){
        try {
            String newsId = model.newsId;
            MyApplication.getInstance()
                    .getMyApplicationComponent()
                    .getRetrofitApp()
                    .create(MyRestService.class)
                    .getNewsContentWithId(String.valueOf(model.newsId),
                            MySettings.getInstance().getLang())
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                JsonObject json = response.body();
                                String title = json.getAsJsonPrimitive("title").getAsString();
                                Thread th = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            FavouriteNewsDatabase.getInstance(MyApplication.getInstance())
                                                    .getDao()
                                                    .updateTitle(newsId, title);
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                th.start();

                            } else {
                                Log.d(API_LOG, "getMainNews onFailure: " + response.message());

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d(API_LOG, "getMainNews onFailure: " + t.getMessage());
                        }
                    });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //TODO: override methods

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }


    //#################################################################


    //TODO: all methods from interface


    @Override
    public void initFavourites(List<FavouriteNewsModel> items) {
        favouriteNewsAdapter.initItems(items);
    }


    //#################################################################

    //TODO: Additional methods

    //initialize live data and observable here
    void initRoom() {
        FavouriteNewsDatabase.getInstance(getContext())
                .getDao()
                .getAll()
                .observe(this, new Observer<List<FavouriteNewsModel>>() {
                    @Override
                    public void onChanged(@Nullable List<FavouriteNewsModel> tourModels) {
                        Log.d(Constants.MY_LOG, "in favourite onChanged");
                        if (tourModels != null && tourModels.size() == 0) {
                            favouriteNewsAdapter.withHeaderNoItem(viewHeaderNoItem);

                        } else {
                            System.out.println("onChangedFavouriteOff: ");
                            favouriteNewsAdapter.withHeader(viewHeader);
                        }

                        allData = tourModels;
                        initFavourites(tourModels);
                    }
                });
    }

    //#################################################################

    //TODO: From EVENTBUS

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(EventFavouriteChanged event) {
        reloadContent();
    }

}
