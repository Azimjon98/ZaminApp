package edu.azimjon.project.zamin.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.bases.BaseRecyclerAdapter;
import edu.azimjon.project.zamin.databinding.ItemSelectCategoryBinding;
import edu.azimjon.project.zamin.model.CategoryNewsModel;

public class SelectCategoriesAdapter extends BaseRecyclerAdapter<CategoryNewsModel> {

    public SelectCategoriesAdapter(Context context, ArrayList<CategoryNewsModel> items) {
        super(context, items);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemSelectCategoryBinding binding = DataBindingUtil
                .inflate(inflater,
                        R.layout.item_select_category,
                        viewGroup,
                        false);
        return new MyHolderItem(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof MyHolderItem) {
            int position = i;
            if (hasHeader || hasHeaderNoInternet || hasHeaderNoItem)
                position--;

            MyHolderItem myHolder = (MyHolderItem) viewHolder;
            myHolder.binding.setModel(items.get(position));
        }
    }

    //TODO: Holders

    public class MyHolderItem extends RecyclerView.ViewHolder {
        ItemSelectCategoryBinding binding;

        public MyHolderItem(ItemSelectCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.enabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
                items.get(getAdapterPosition()).setEnabled(isChecked);

//                binding.getModel().setEnabled(isChecked);
//                //update category in another thread
//                new Thread(() -> {
//                    CategoryNewsDatabase.getInstance(context)
//                            .getDao()
//                            .update(binding.getModel());
//                }).start();

            });
        }

    }
}
