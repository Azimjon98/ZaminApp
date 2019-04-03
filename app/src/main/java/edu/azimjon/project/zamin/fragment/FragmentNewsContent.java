package edu.azimjon.project.zamin.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;
import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.CategoryNewsAdapter;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.databinding.WindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.WindowTopNewsBinding;
import edu.azimjon.project.zamin.model.NewsCategoryModel;
import edu.azimjon.project.zamin.model.NewsContentModel;
import edu.azimjon.project.zamin.model.NewsSimpleModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsContent;
import edu.azimjon.project.zamin.mvp.presenter.PresenterTopNews;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsContent;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;

public class FragmentNewsContent extends Fragment implements IFragmentNewsContent {
    //TODO: Constants here


    //TODO: variables here
    WindowNewsContentBinding binding;
    PresenterNewsContent presenterNewsContent;

    //adapters
    MediumNewsAdapter mediumNewsAdapter;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterNewsContent = new PresenterNewsContent(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_news_content, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize adapters and append to lists

        binding.listLastNews.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<NewsSimpleModel>(),
                () -> {
                    presenterNewsContent.getContinue();
                    Toast.makeText(getContext(), "Salom", Toast.LENGTH_SHORT).show();
                });
        binding.listLastNews.setAdapter(mediumNewsAdapter);

        String newsId = getArguments().getString(KEY_NEWS_ID);


// Enable Javascript
        binding.contentWeb.getSettings().setJavaScriptEnabled(true);


        binding.contentWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.progress.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                injectCSS();
                super.onPageFinished(view, url);
                binding.progress.setVisibility(View.GONE);

            }
        });
        Log.d(Constants.MY_LOG, "in fragmentview:" + getArguments().getString(Constants.WEB_URL));

        initIcons();
        presenterNewsContent.init(newsId);
    }

    //TODO: override methods


    //#################################################################


    //TODO: all methods from interface


    @Override
    public void initContent(NewsContentModel model) {
        binding.setModel(model);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        binding.contentWeb.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}img {\n" +
                " margin-top:10px;\n" +
                " margin-bottom:10px;\n" +
                "}</style>" + model.getContent(), mimeType, encoding, "");
    }

    @Override
    public void addLastNews(List<NewsSimpleModel> items) {
        mediumNewsAdapter.add_items(items);
    }


    //#################################################################

    //TODO: Additional methods

    private void initIcons() {
        binding.iconBack.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        binding.iconBookmark.setOnClickListener(v -> Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show());
    }


    //#################################################################

}