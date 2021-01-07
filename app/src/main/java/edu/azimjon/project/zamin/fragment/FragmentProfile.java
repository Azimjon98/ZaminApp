package edu.azimjon.project.zamin.fragment;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.databinding.DialogLanguageBinding;
import edu.azimjon.project.zamin.databinding.WindowProfileBinding;
import edu.azimjon.project.zamin.model.CategoryNewsModel;
import edu.azimjon.project.zamin.retrofit.MyRestService;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;
import edu.azimjon.project.zamin.util.MyUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;

public class FragmentProfile extends Fragment {
    WindowProfileBinding binding;
    MyHandler handler = new MyHandler();

    //lottie animator
    ValueAnimator animator1;
    ValueAnimator animator2;

    boolean isDidLoad = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.window_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        binding.swithNofifications.setChecked(MySettings.getInstance().getNotificationEnabled());
        binding.swithNofifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySettings.getInstance().setNotificationEnabled(isChecked);
            }
        });

        binding.registrationLay.setOnClickListener(v -> {
            Toast.makeText(getContext(), MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.toast_login_disabled), Toast.LENGTH_SHORT).show();
        });

        binding.categoryLay.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_fragmentProfile_to_fragmentSelectCategories));
        binding.languageLay.setOnClickListener(languageListener);
        binding.cacheClearLay.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Are you really want to clear cache")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyUtil.deleteCache(getContext());
                            Toast.makeText(getContext(), "Cache cleared", Toast.LENGTH_SHORT).show();
                            getActivity().recreate();
                        }
                    })
                    .create();

            dialog.show();
        });


        try {
            CategoryNewsDatabase.getInstance(MyApplication.getInstance()).getDao().getAllEnabledCategoriesLive().observe(FragmentProfile.this, new Observer<List<CategoryNewsModel>>() {
                @Override
                public void onChanged(@Nullable List<CategoryNewsModel> categoryNewsModels) {
                    if (categoryNewsModels.size() == 1)
                        binding.textCategories.setText(categoryNewsModels.get(0).getName());
                    else if (categoryNewsModels.size() >= 2){
                        String enabledCategories = "" +
                                categoryNewsModels.get(0).getName() + ", " +
                                categoryNewsModels.get(1).getName() +
                                ((categoryNewsModels.size() > 2) ? "..." : "");

                        binding.textCategories.setText(enabledCategories);

                    }

                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        reloadContent();
        isDidLoad = true;
    }

    private void reloadContent(){
        changeLanguage();
    }

    private void changeLanguage(){
        //TODO: localize texts
        binding.toolbar.setTitle(MyUtil.getLocalizedString(getContext(), R.string.toolbar_profile));
        binding.textNotification.setText(MyUtil.getLocalizedString(getContext(), R.string.text_notification));
        binding.textCateg.setText(MyUtil.getLocalizedString(getContext(), R.string.text_categories));
        binding.textLanguage.setText(MyUtil.getLocalizedString(getContext(), R.string.text_select_language));
        binding.textCurrentLang.setText(MyUtil.getLocalizedString(getContext(), R.string.language));
        binding.textRegistration.setText(MyUtil.getLocalizedString(getContext(), R.string.text_registration));


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
        String locale = MySettings.getInstance().getLocale();
        final String[] newLang = {locale};

        DialogLanguageBinding bindingDialog = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.dialog_language,
                null,
                false);

        //TODO: change locale
        bindingDialog.textSelectLanguage.setText(MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.text_choose_language));
        bindingDialog.btnCancel.setText(MyUtil.getLocalizedString(getContext(), R.string.text_back));
        bindingDialog.btnSubmit.setText(MyUtil.getLocalizedString(getContext(), R.string.text_submit));

        //#######################################

        AlertDialog dialog =
                new AlertDialog.Builder(getContext())
                        .setView(bindingDialog.getRoot())
                        .create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (locale.equals("uz"))
            bindingDialog.lottieAnimationUz.setProgress(1f);
        else if (locale.equals("kr"))
            bindingDialog.lottieAnimationKr.setProgress(1f);

        animator1 = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
        animator2 = ValueAnimator.ofFloat(0f, 1f).setDuration(500);

        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animator2.isRunning()){
                    animator2.cancel();
                }
                bindingDialog.lottieAnimationKr.setProgress(0);

                bindingDialog.lottieAnimationUz.setProgress((Float) animation.getAnimatedValue());

            }
        });

        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animator1.isRunning()){
                    animator1.cancel();
                }
                    bindingDialog.lottieAnimationUz.setProgress(0);

                bindingDialog.lottieAnimationKr.setProgress((Float) animation.getAnimatedValue());

            }
        });

        bindingDialog.languageLt.setOnClickListener(v1 -> {
            newLang[0] = "uz";
            animator1.start();
        });

        bindingDialog.languageKr.setOnClickListener(v1 -> {
            newLang[0] = "kr";
            animator2.start();
        });

        bindingDialog.btnCancel.setOnClickListener(v1 -> dialog.dismiss());
        bindingDialog.btnSubmit
                .setOnClickListener(v1 -> {
                    dialog.dismiss();

                    if (!newLang[0].equals(locale))
                        change_language(newLang[0]);

                });

        dialog.show();
    }

    void change_language(String newLang) {

        if (!MyUtil.hasConnectionToNet(Objects.requireNonNull(getActivity()))) {
            Toast.makeText(getContext(),
                    MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.text_no_connection)
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
                                List<CategoryNewsModel> categories = new ArrayList<>();
                                for (JsonElement element : Objects.requireNonNull(response.body())) {
                                    JsonObject category = element.getAsJsonObject();

                                    CategoryNewsModel model = new CategoryNewsModel(
                                            category.getAsJsonPrimitive("name").getAsString(),
                                            category.getAsJsonPrimitive("id").getAsString(),
                                            ""
                                    );
                                    categories.add(model);
                                }

                                new ChangeLanguageThread(categories, handler)
                                        .execute();

                                MySettings.getInstance().setLocale(newLang);
                            } else {
                                Log.d(API_LOG, "onResponse: " + response.message());
                                Toast.makeText(getContext(),
                                        MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.check_the_connection),
                                        Toast.LENGTH_SHORT).show();
                                binding.progressLay.setVisibility(View.GONE);

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            Log.d(API_LOG, "onFailure: " + t.getMessage());
                            Toast.makeText(getContext(),
                                    MyUtil.getLocalizedString(Objects.requireNonNull(getContext()), R.string.check_the_connection),
                                    Toast.LENGTH_SHORT).show();
                            binding.progressLay.setVisibility(View.GONE);


                        }
                    });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //TODO: Checking database for changings in another Thread
    public static class ChangeLanguageThread extends AsyncTask<Void, Void, Void> {
        List<CategoryNewsModel> categories;
        MyHandler handler;

        ChangeLanguageThread(List<CategoryNewsModel> categories, MyHandler handler) {
            this.categories = categories;
            this.handler = handler;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<CategoryNewsModel> oldCategories = NavigationActivity.categoryModels;
            List<CategoryNewsModel> newCategories = categories;

            //updating isEnabled state of new Categories
            for (CategoryNewsModel new_ : newCategories) {

                for (CategoryNewsModel old : oldCategories) {
                    if (new_.getCategoryId().equals(old.getCategoryId())) {
                        new_.setEnabled(old.isEnabled());
                    }
                }
            }

            try {
                CategoryNewsDatabase
                        .getInstance(MyApplication.getInstance())
                        .getDao()
                        .deleteAndCreate(new ArrayList<>(newCategories));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            handler.sendEmptyMessage(1);
            return null;
        }
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (FragmentProfile.this.getActivity() != null) {
                reloadContent();
                binding.progressLay.setVisibility(View.GONE);
            }
        }
    }

}