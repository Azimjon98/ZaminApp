package edu.azimjon.project.zamin.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.SplashActivity;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.DialogLanguageBinding;
import edu.azimjon.project.zamin.databinding.WindowProfileBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.CALLBACK_LOG;

public class FragmentProfile extends Fragment {
    WindowProfileBinding binding;

    //lottine animator
    ValueAnimator animator1;
    ValueAnimator animator2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                List<NewsCategoryModel> categoryModels = CategoryNewsDatabase
                        .getInstance(getContext())
                        .getDao().getAllEnabledCategories();

                if (categoryModels.size() == 1)
                    binding.textCategories.setText(categoryModels.get(0).getName());
                else if (categoryModels.size() >= 2)
                    binding.textCategories
                            .setText("" +
                                    categoryModels.get(0).getName() + ", " +
                                    categoryModels.get(1).getName() +
                                    ((categoryModels.size() > 2) ? "..." : "")
                            );

                return null;
            }
        }.execute();
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

        AlertDialog dialog =
                new AlertDialog.Builder(getContext())
                        .setView(bindingDialog.getRoot())
                        .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (language.equals("default"))
            bindingDialog.lottieAnimationUz.setProgress(1f);
        else if (language.equals("uz"))
            bindingDialog.lottieAnimationKr.setProgress(1f);

        animator1 = ValueAnimator.ofFloat(0f, 1f).setDuration(500);
        animator2 = ValueAnimator.ofFloat(0f, 1f).setDuration(500);

        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bindingDialog.lottieAnimationUz.setProgress((Float) animation.getAnimatedValue());
            }
        });

        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bindingDialog.lottieAnimationKr.setProgress((Float) animation.getAnimatedValue());
            }
        });

        bindingDialog.languageLt.setOnClickListener(v1 -> {
            newLang[0] = "default";
            animator1.start();
            bindingDialog.lottieAnimationKr.setProgress(0f);
        });

        bindingDialog.languageKr.setOnClickListener(v1 -> {
            newLang[0] = "uz";
            animator2.start();
            bindingDialog.lottieAnimationUz.setProgress(0f);
        });

        bindingDialog.btnCancel.setOnClickListener(v1 -> dialog.dismiss());
        bindingDialog.btnSubmit
                .setOnClickListener(v1 -> {
                    if (newLang[0].equals(language))
                        dialog.dismiss();
                    else {
                        MySettings.getInstance().setLocale(newLang[0]);
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

        dialog.show();
    }

}
