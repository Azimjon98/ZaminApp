package edu.azimjon.project.zamin.addition;

import android.content.Context;

import java.util.List;

import edu.azimjon.project.zamin.model.NewsCategoryModel;

public class MyDataHelper {
    static private Context appContext;
    static private MyDataHelper instance;
    private List<NewsCategoryModel> categories;


    public void initInstance(Context context) {
        appContext = context;


    }

    public static synchronized MyDataHelper getInstance() {
        return instance;
    }

    public List<NewsCategoryModel> getCategoriesData() {
        return categories;
    }

}
