package edu.azimjon.project.zamin.fragment;

import android.animation.ValueAnimator;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.SplashActivity;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.databinding.DialogLanguageBinding;
import edu.azimjon.project.zamin.databinding.WindowProfileBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import edu.azimjon.project.zamin.room.dao.CategoryNewsDao;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;

public class FragmentProfile extends Fragment {
    WindowProfileBinding binding;

    //lottine animator
    ValueAnimator animator1;
    ValueAnimator animator2;

    volatile CategoryNewsDao dao;

    volatile MyHandler handler = new MyHandler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = CategoryNewsDatabase
                .getInstance(getContext().getApplicationContext())
                .getDao();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());


        binding.categoryLay.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentProfile_to_fragmentSelectCategories));
        binding.languageLay.setOnClickListener(languageListener);

        dao.
                getAllEnabledCategoriesLive().
                observe(this, new Observer<List<NewsCategoryModel>>() {
                    @Override
                    public void onChanged(@Nullable List<NewsCategoryModel> categories) {
                        if (categories.size() == 1)
                            binding.textCategories.setText(categories.get(0).getName());
                        else if (categories.size() >= 2)
                            binding.textCategories
                                    .setText("" +
                                            categories.get(0).getName() + ", " +
                                            categories.get(1).getName() +
                                            ((categories.size() > 2) ? "..." : "")
                                    );
                    }
                });

        //TODO: localize texts
        binding.toolbar.setTitle(MyUtil.getLocalizedString(getContext(), R.string.toolbar_profile));
        binding.textNotification.setText(MyUtil.getLocalizedString(getContext(), R.string.text_notification));
        binding.textCateg.setText(MyUtil.getLocalizedString(getContext(), R.string.text_categories));
        binding.textLanguage.setText(MyUtil.getLocalizedString(getContext(), R.string.text_select_language));
        binding.textCurrentLang.setText(MyUtil.getLocalizedString(getContext(), R.string.language));

    }


    //TODO: Listenters


    //Language click listener
    public View.OnClickListener languageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initDialogView(v);
        }
    };

    //TODO: Additional methods

    void initDialogView(View v) {
        String language = MySettings.getInstance().getLocale();
        final String[] newLang = {language};

        DialogLanguageBinding bindingDialog = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.dialog_language,
                null,
                false);

        //TODO: change locale
        bindingDialog.textSelectLanguage.setText(MyUtil.getLocalizedString(getContext(), R.string.text_choose_language));
        bindingDialog.btnCancel.setText(MyUtil.getLocalizedString(getContext(), R.string.text_back));
        bindingDialog.btnSubmit.setText(MyUtil.getLocalizedString(getContext(), R.string.text_submit));

        //#######################################

        AlertDialog dialog =
                new AlertDialog.Builder(getContext())
                        .setView(bindingDialog.getRoot())
                        .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (language.equals("uz"))
            bindingDialog.lottieAnimationUz.setProgress(1f);
        else if (language.equals("kr"))
            bindingDialog.lottieAnimationKr.setProgress(1f);

        animator1 = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
        animator2 = ValueAnimator.ofFloat(0f, 1f).setDuration(500);

        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bindingDialog.lottieAnimationUz.setProgress((Float) animation.getAnimatedValue());

                if ((Float)animation.getAnimatedValue() == 1f)
                    bindingDialog.lottieAnimationKr.setProgress(0);

            }
        });

        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bindingDialog.lottieAnimationKr.setProgress((Float) animation.getAnimatedValue());

                if ((Float)animation.getAnimatedValue() == 1f)
                    bindingDialog.lottieAnimationUz.setProgress(0);
            }
        });

        bindingDialog.languageLt.setOnClickListener(v1 -> {
            newLang[0] = "uz";
            animator1.start();
            animator2.cancel();
            bindingDialog.lottieAnimationKr.setProgress(0f);
        });

        bindingDialog.languageKr.setOnClickListener(v1 -> {
            newLang[0] = "kr";
            animator2.start();
            animator1.cancel();
            bindingDialog.lottieAnimationUz.setProgress(0f);
        });

        bindingDialog.btnCancel.setOnClickListener(v1 -> dialog.dismiss());
        bindingDialog.btnSubmit
                .setOnClickListener(v1 -> {
                    dialog.dismiss();

                    if (!newLang[0].equals(language))
                        change_language(newLang[0]);

                });

        dialog.show();
    }

    void change_language(String newLang) {
        if (!MyUtil.hasConnectionToNet(getActivity())) {
            Toast.makeText(getContext(),
                    MyUtil.getLocalizedString(getContext(), R.string.text_no_connection)
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressLay.setVisibility(View.VISIBLE);

        try {
            MyApplication.getInstance().
                    getMyApplicationComponent()
                    .getRetrofitApp()
                    .create(MyRestService.class)
                    .getAllCategories(
                            newLang.equals("uz") ?
                                    "oz" : "uz"
                    )
                    .enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(@NotNull Call<JsonArray> call, @NotNull Response<JsonArray> response) {
                            if (response.isSuccessful()) {

                                //Creating a categories array from server
                                List<NewsCategoryModel> categories = new ArrayList<>();
                                for (JsonElement element : response.body()) {
                                    JsonObject category = element.getAsJsonObject();

                                    NewsCategoryModel model = new NewsCategoryModel(
                                            category.getAsJsonPrimitive("name").getAsString(),
                                            category.getAsJsonPrimitive("id").getAsString(),
                                            ""
                                    );
                                    categories.add(model);

                                    new ChangeLanguageThread(categories, newLang)
                                            .execute();

                                }

                            } else {
                                Log.d(API_LOG, "onResponse: " + response.message());
                                Toast.makeText(getContext(),
                                        MyUtil.getLocalizedString(getContext(), R.string.check_the_connection),
                                        Toast.LENGTH_SHORT).show();
                                binding.progressLay.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.d(API_LOG, "onFailure: " + t.getMessage());
                            Toast.makeText(getContext(),
                                    MyUtil.getLocalizedString(getContext(), R.string.check_the_connection),
                                    Toast.LENGTH_SHORT).show();
                            binding.progressLay.setVisibility(View.GONE);


                        }
                    });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //TODO: Checking database for changings in another Thread
    public class ChangeLanguageThread extends AsyncTask<Void, Void, Void> {
        List<NewsCategoryModel> categories;
        String newLang;

        public ChangeLanguageThread(List<NewsCategoryModel> categories, String newLang) {
            this.categories = categories;
            this.newLang = newLang;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<NewsCategoryModel> oldCategories = dao.getAll();
            List<NewsCategoryModel> newCategories = categories;


            //updating isEnabled state of new Categories
            for (NewsCategoryModel new_ : newCategories) {

                for (NewsCategoryModel old : oldCategories) {
                    if (new_.getCategoryId().equals(old.getCategoryId())) {
                        new_.setEnabled(old.isEnabled());
                    }
                }
            }

            //change database
            dao.deleteAndCreate(new ArrayList<>(newCategories));
            MySettings.getInstance().setLocale(newLang);

            handler.sendEmptyMessage(1);

            return null;
        }
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            FragmentProfile.this.getActivity().finish();
            Intent intent = new Intent(FragmentProfile.this.getActivity(), SplashActivity.class);
            FragmentProfile.this.getContext().startActivity(intent);
        }
    }

}