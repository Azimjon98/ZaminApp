package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.adapter.SelectCategoriesAdapter;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.WindowSelectCategoriesBinding;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.dao.CategoryNewsDao;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;

public class FragmentSelectCategories extends Fragment {
    WindowSelectCategoriesBinding binding;
    CategoryNewsDao dao;
    LinearLayoutManager manager;

    SelectCategoriesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = CategoryNewsDatabase
                .getInstance(getContext())
                .getDao();
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


        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        binding.list.setLayoutManager(manager);
        adapter = new SelectCategoriesAdapter(getContext(), new ArrayList<NewsSimpleModel>());
        binding.list.setAdapter(adapter);
        binding.getRoot().setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                adapter.init_items(dao.getAll());

                return null;
            }
        }.execute();

    }
}