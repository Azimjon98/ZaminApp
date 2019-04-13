package edu.azimjon.project.zamin.fragment;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.ArrayList;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.adapter.SelectCategoriesAdapter;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.WindowSelectCategoriesBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.room.dao.CategoryNewsDao;
import edu.azimjon.project.zamin.room.database.CategoryNewsDatabase;

import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;

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
        binding.list.setHasFixedSize(true);

        new GetAllCategoriesAsyckTask().execute();


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallBack(adapter));
        itemTouchHelper.attachToRecyclerView(binding.list);
    }


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
    public void onStop() {
        updateDatabase();
        super.onStop();

    }

    //TODO: CHECK AND UPDATE DATABASE
    public void updateDatabase() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                dao.deleteAll();

                for (NewsCategoryModel i : adapter.items) {
                    i.setId(0);
                    dao.insert(i);
                }


                return null;
            }
        }.execute();
    }

    //Another Thread AsycyTsks

    public class GetAllCategoriesAsyckTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            adapter.init_items(dao.getAll());

            return null;
        }
    }
}