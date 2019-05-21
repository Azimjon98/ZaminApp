package edu.azimjon.project.zamin.fragment;

import android.app.AlertDialog;
import android.app.Application;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.navigation.Navigation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.adapter.SelectCategoriesAdapter;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.databinding.DialogLanguageBinding;
import edu.azimjon.project.zamin.databinding.DialogResetCategoriesBinding;
import edu.azimjon.project.zamin.databinding.WindowSelectCategoriesBinding;
import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import edu.azimjon.project.zamin.room.dao.CategoryNewsDao;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;


public class FragmentSelectCategories extends Fragment {
    WindowSelectCategoriesBinding binding;
    LinearLayoutManager manager;

    SelectCategoriesAdapter adapter;
    MyHandler handler = new MyHandler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_select_categories, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());
        binding.refreshButton.setOnClickListener(v -> refreshCategories());
        //TODO: set locale
        binding.toolbar.setTitle(MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.title_select_categories));
        //############################################################

        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.list.setLayoutManager(manager);
        adapter = new SelectCategoriesAdapter(getContext(), new ArrayList<>());
        binding.list.setAdapter(adapter);
        binding.list.setHasFixedSize(true);
        binding.list.setItemAnimator(null);
        adapter.initItems(NavigationActivity.categoryModels);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallBack(adapter));
        itemTouchHelper.attachToRecyclerView(binding.list);
    }

    //TODO: - Additional Methods

    private void refreshCategories() {

        DialogResetCategoriesBinding bindingDialog = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.dialog_reset_categories,
                null,
                false);

        //TODO: change locale
        bindingDialog.textTitle.setText(MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.text_reset_alert_title));
        bindingDialog.textMessege.setText(MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.text_reset_alert_message));
        bindingDialog.btnCancel.setText(MyUtil.getLocalizedString(getContext(), R.string.text_back));
        bindingDialog.btnSubmit.setText(MyUtil.getLocalizedString(getContext(), R.string.text_yes));

        //#######################################

        android.support.v7.app.AlertDialog dialog =
                new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setView(bindingDialog.getRoot())
                        .create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        bindingDialog.btnCancel.setOnClickListener(v1 -> dialog.dismiss());
        bindingDialog.btnSubmit
                .setOnClickListener(v1 -> {
                    dialog.dismiss();
                    continueRefresh();

                });





        dialog.show();
    }

    private void continueRefresh() {
        binding.progressLay.setVisibility(View.VISIBLE);
        binding.refreshButton.setVisibility(View.GONE);
        try {
            MyApplication application = MyApplication.getInstance();
            application
                    .getMyApplicationComponent()
                    .getRetrofitApp()
                    .create(MyRestService.class)
                    .getAllCategories(
                            MySettings.getInstance().getLang()
                    )
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(@NotNull Call<JsonArray> call, @NotNull Response<JsonArray> response) {
                            if (response.isSuccessful()) {

                                //Creating a categories array from server
                                List<CategoryNewsModel> categories = new ArrayList<>();
                                assert response.body() != null;
                                for (JsonElement element : response.body()) {
                                    JsonObject category = element.getAsJsonObject();

                                    CategoryNewsModel model = new CategoryNewsModel(
                                            category.getAsJsonPrimitive("name").getAsString(),
                                            category.getAsJsonPrimitive("id").getAsString(),
                                            ""
                                    );
                                    categories.add(model);
                                }


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        CategoryNewsDatabase
                                                .getInstance(application)
                                                .getDao().deleteAndCreate(new ArrayList<>(categories));

                                        handler.sendEmptyMessage(1);
                                    }
                                }).start();

                            } else {
                                Log.d(API_LOG, "onResponse: " + response.message());
                                Toast.makeText(getContext(),
                                        MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.check_the_connection),
                                        Toast.LENGTH_SHORT).show();
                                binding.progressLay.setVisibility(View.GONE);
                                binding.refreshButton.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.d(API_LOG, "onFailure: " + t.getMessage());
                            Toast.makeText(getContext(),
                                    MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.check_the_connection),
                                    Toast.LENGTH_SHORT).show();
                            binding.progressLay.setVisibility(View.GONE);
                            binding.refreshButton.setVisibility(View.VISIBLE);
                        }
                    });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //###################################################################

    //TODO: ItemTouch helper
    public class SimpleItemTouchHelperCallBack extends ItemTouchHelper.Callback {

        private final SelectCategoriesAdapter adapter;

        public SimpleItemTouchHelperCallBack(SelectCategoriesAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            adapter.onItemMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }
    }

    @Override
    public void onPause() {
        updateDatabase(adapter.items);
        super.onPause();
    }

    //TODO: CHECK AND UPDATE DATABASE
    public void updateDatabase(List<CategoryNewsModel> items) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<CategoryNewsModel> categoryNewsModels = new ArrayList<>(items);

                for (CategoryNewsModel i : categoryNewsModels) {
                    i.setId(0);
                }

                try {
                    CategoryNewsDatabase
                            .getInstance(MyApplication.getInstance())
                            .getDao()
                            .deleteAndCreate(new ArrayList<>(categoryNewsModels));
                    System.out.println("FragmentSelectCategoriesdatabaseUpdated: reloadContent");

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (FragmentSelectCategories.this.getActivity() != null) {
                adapter.initItems(NavigationActivity.categoryModels);
                binding.progressLay.setVisibility(View.GONE);
                binding.refreshButton.setVisibility(View.VISIBLE);
            }
        }
    }

}