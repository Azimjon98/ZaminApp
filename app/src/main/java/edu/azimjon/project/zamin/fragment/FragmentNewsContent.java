package edu.azimjon.project.zamin.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.activity.NavigationActivity;
import edu.azimjon.project.zamin.adapter.MediumNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.Converters;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.databinding.FooterNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.HeaderWindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.WindowNewsContentBinding;
import edu.azimjon.project.zamin.databinding.WindowNoConnectionBinding;
import edu.azimjon.project.zamin.model.ContentNewsModel;
import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterNewsContent;
import edu.azimjon.project.zamin.mvp.view.IFragmentNewsContent;
import edu.azimjon.project.zamin.room.database.FavouriteNewsDatabase;
import edu.azimjon.project.zamin.util.MyChromeClient;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_NEWS_MODEL;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_ID;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_TOOLBAR_NAME;
import static edu.azimjon.project.zamin.addition.Constants.KEY_SEARCH_WHERE;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;
import static edu.azimjon.project.zamin.addition.Constants.SEARCH_TAG;
import static edu.azimjon.project.zamin.addition.Constants.WEB_URL;

public class FragmentNewsContent extends Fragment implements IFragmentNewsContent {
    //TODO: Constants here
    String newsId;
    ContentNewsModel contentModel;

    //TODO: variables here
    LinearLayoutManager manager;
    WindowNewsContentBinding binding;
    HeaderWindowNewsContentBinding bindingHeader;
    WindowNoConnectionBinding bindingNoConnection;
    FooterNoConnectionBinding bindingFooter;

    PresenterNewsContent presenterNewsContent;

    //adapters
    MediumNewsAdapter mediumNewsAdapter;

    //scrolling variables
    boolean changeFontVisible = false;
    boolean isScrollingEnabled = false;
    boolean isScrolling = false;
    boolean isLoading = false;
    boolean mShouldPause = false;

    int total_items, visible_items, scrollout_items;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsId = getArguments() != null ? getArguments().getString(KEY_NEWS_ID) : "";

        if (presenterNewsContent == null)
            presenterNewsContent = new PresenterNewsContent(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.window_news_content, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.iconBack.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .popBackStack(MySettings.getInstance().getWhichIdCallsContent(), false);
        });

        //initialize adapters and append to lists

        if (manager == null) {
            manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return isScrollingEnabled;
                }
            };
            binding.listLastNews.setLayoutManager(manager);
            binding.listLastNews.setItemAnimator(null);
            mediumNewsAdapter = new MediumNewsAdapter(getContext(), new ArrayList<SimpleNewsModel>());
            binding.listLastNews.setAdapter(mediumNewsAdapter);

            bindingHeader = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.header_window_news_content, binding.listLastNews, false);
            bindingNoConnection = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.window_no_connection, binding.listLastNews, false);
            bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.footer_no_connection, binding.listLastNews, false);

//            mediumNewsAdapter.withHeader(bindingHeader.getRoot());
            //disable auto scrolling
            binding.listLastNews.setHasFixedSize(true);

            //*****************************************************************************


            //TODO: Header binding initializators
            configureWebViews(bindingHeader.contentWeb, 0);
            configureWebViews(bindingHeader.adWeb, 1);


            binding.listLastNews.addOnScrollListener(scrollListener);

            bindingNoConnection.btnRefresh.setOnClickListener(v -> {
                presenterNewsContent.init(newsId);
            });

            binding.increaseFont.setOnClickListener(v -> {
                int currentSize = MySettings.getInstance().getFontSize();
                if (currentSize < 30) {
                    MySettings.getInstance().setFontSize(currentSize + 3);
                    bindingHeader.contentWeb.getSettings().setDefaultFontSize(currentSize + 3);
                }
            });

            binding.decreaseFont.setOnClickListener(v -> {
                int currentSize = MySettings.getInstance().getFontSize();
                if (currentSize > 15) {
                    MySettings.getInstance().setFontSize(currentSize - 3);
                    bindingHeader.contentWeb.getSettings().setDefaultFontSize(currentSize - 3);
                }
            });

            //TODO: Change locale
            bindingNoConnection.textNoConnection.setText(MyUtil.getLocalizedString(getContext(), R.string.text_no_connection));
            bindingNoConnection.btnRefresh.setText(MyUtil.getLocalizedString(getContext(), R.string.text_refresh));
            //###################################

            presenterNewsContent.init(newsId);
        } else {
            reloadContent();
        }


    }

    private void configureWebViews(WebView v, int position) {
        File file = new File(getContext().getCacheDir(), "WebCache");
        v.getSettings().setJavaScriptEnabled(true);
        v.getSettings().setLoadWithOverviewMode(true);
        v.getSettings().setAllowFileAccess(true);
        v.getSettings().setAppCachePath(file.getAbsolutePath());
        v.getSettings().setAppCacheEnabled(true);
        v.setWebChromeClient(new WebChromeClient());
        if (position == 0) {
            v.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            v.getSettings().setDefaultFontSize(MySettings.getInstance().getFontSize());
        }

        v.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (position == 0)
                    bindingHeader.progress.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (position == 0) {
                    isScrollingEnabled = true;
                    binding.iconChangeFont.setVisibility(View.VISIBLE);
                    bindingHeader.progress.setVisibility(View.GONE);
                }else if(position == 1){
                    bindingHeader.separator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                if (url.contains("youtube.com")) mShouldPause = true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    System.out.println("url: " + url);
                if (url.contains("http://bit.ly/tgzamin") || url.contains("https://t.me")) {
                    Intent tgIntent = new Intent(Intent.ACTION_VIEW);
                    tgIntent.setData(Uri.parse(url));
                    if (getActivity() != null)
                        getActivity().startActivity(tgIntent);
                    return true;

                } else if (url.contains("https://zamin.uz/index.php?do=go")) {
                    Bundle bundle = new Bundle();
                    bundle.putString(WEB_URL, url);
                    Navigation.findNavController(view).navigate(R.id.action_fragmentNewsContent_to_fragmentWebView, bundle);
                    return true;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(WEB_URL, url);
                    Navigation.findNavController(view).navigate(R.id.action_fragmentNewsContent_to_fragmentWebView, bundle);
                    return true;
                }
            }


        });
    }

    private void reloadContent() {
        if (binding == null || mediumNewsAdapter == null)
            return;

        int iFirst = manager.findFirstVisibleItemPosition();
        int iLast = manager.findFirstVisibleItemPosition();

        if (iFirst == 0)
            iFirst = 1;

        mediumNewsAdapter.notifyItemRangeChanged(iFirst, iLast - iFirst);

        initIcons();
    }

    //TODO: override methods

    @Override
    public void onPause() {
        super.onPause();

        //pause video in webview when exiting
        if (bindingHeader == null || bindingHeader.contentWeb == null){
            System.out.println("in pause");
            return;
        }
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(bindingHeader.contentWeb, (Object[]) null);

        } catch(ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
    }

    //#################################################################


    //TODO: all methods from interface


    @Override
    public void initContent(ContentNewsModel model, int message) {
        if (getContext() == null)
            return;


        if (message == MESSAGE_NO_CONNECTION) {
            mediumNewsAdapter.withHeaderNoInternet(bindingNoConnection.getRoot());
        } else if (message == MESSAGE_OK) {
            mediumNewsAdapter.withHeader(bindingHeader.getRoot());
            bindingHeader.setModel(model);
            contentModel = model;
            initIcons();
            bindingHeader.contentWeb.loadUrl(model.getContentUrl());
            bindingHeader.adWeb.loadUrl("http://m.zamin.uz/api/v1/adv.php?id=22");
        }


    }

    @Override
    public void initLastNews(List<SimpleNewsModel> items, int message) {
        if (getContext() == null)
            return;

        if (message == MESSAGE_OK) {
            //check if news contains news with this id
            List<SimpleNewsModel> news = new ArrayList<>(items);
            for (SimpleNewsModel i : news) {
                if (i.getNewsId().equals(newsId)) {
                    news.remove(i);
                    break;
                }
            }

            mediumNewsAdapter.initItems(news);
        }

    }

    @Override
    public void addLastNews(List<SimpleNewsModel> items, int message) {
        if (getContext() == null)
            return;

        mediumNewsAdapter.hideLoading();
        isLoading = false;

        if (message == MESSAGE_NO_CONNECTION) {
            mediumNewsAdapter.withFooter(bindingFooter.getRoot());
            return;
        }

        if (message == MESSAGE_OK) {
            //check if news contains news with this id
            List<SimpleNewsModel> news = new ArrayList<>(items);
            for (SimpleNewsModel i : news) {
                if (i.getNewsId().equals(newsId)) {
                    news.remove(i);
                    break;
                }
            }

            mediumNewsAdapter.addItems(news);

        }
    }


    @Override
    public void initTags(List<String> tags) {
        if (getContext() == null)
            return;
        if (tags.size() != 0) {
            bindingHeader.tagsGroup.setVisibility(View.VISIBLE);
        } else {
            bindingHeader.tagsGroup.setVisibility(View.GONE);
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (String tag : tags) {
            Chip chip = (Chip) inflater.inflate(R.layout.item_tag, bindingHeader.tagsGroup, false);
            chip.setText(tag);
//            chip.setChipBackgroundColorResource(R.color.chip_color6);

            chip.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_SEARCH_ID, tag);
                bundle.putString(KEY_SEARCH_TOOLBAR_NAME, tag);
                bundle.putInt(KEY_SEARCH_WHERE, SEARCH_TAG);
                Navigation.findNavController(v).navigate(R.id.action_global_fragmentSearchResults, bundle);
            });

            bindingHeader.tagsGroup.addView(chip);
        }

    }


    //#################################################################

    //TODO: Additional methods




    private void initIcons() {

        if (NavigationActivity.allFavouriteIds.contains(contentModel.getNewsId())) {
            contentModel.setWished(true);
        } else {
            contentModel.setWished(false);
        }

        binding.iconBookmark.setImageResource(
                contentModel.isWished() ?
                        R.drawable.bookmark_active :
                        R.drawable.bookmark_inactive);

        binding.iconBookmark.setOnClickListener(v -> {
            boolean isWished = contentModel.isWished();
            contentModel.setWished(!isWished);

            binding.iconBookmark.setImageResource(
                    contentModel.isWished ?
                            R.drawable.bookmark_active :
                            R.drawable.bookmark_inactive
            );
            //delete or inser news to favourites in another thread
            new Thread(() -> {
                if (isWished) {
                    FavouriteNewsDatabase.getInstance(getContext())
                            .getDao()
                            .delete(contentModel.getNewsId());
                } else {
                    FavouriteNewsDatabase.getInstance(getContext())
                            .getDao()
                            .insert(Converters
                                    .fromContentNewstoFavouritesNews(contentModel));
                }
            }).start();
        });

        binding.iconShare.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = bindingHeader.getModel().getOriginalUrl();
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

            startActivity(Intent.createChooser(sharingIntent,
                    MyUtil.getLocalizedString(v.getContext(), R.string.text_share)
            ));
        });

        binding.iconChangeFont.setOnClickListener(v -> {
            if (changeFontVisible)
                binding.layChangeFont.setVisibility(View.GONE);
            else
                binding.layChangeFont.setVisibility(View.VISIBLE);

            changeFontVisible = !changeFontVisible;
        });
    }

    //#################################################################

    //TODO: Argument variables
    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);


            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
                if (changeFontVisible) {
                    changeFontVisible = false;
                    binding.layChangeFont.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            total_items = manager.getItemCount();
            visible_items = manager.getChildCount();
            scrollout_items = manager.findFirstVisibleItemPosition();
            Log.d(Constants.MY_LOG, "total_items: " + total_items + " " +
                    "visible_items: " + visible_items + " " +
                    "scrollout_items: " + scrollout_items);

            if (isScrolling && (visible_items + scrollout_items == total_items) && !isLoading) {
                isScrolling = false;
                isLoading = true;

                mediumNewsAdapter.showLoading();
                presenterNewsContent.getContinue();
            }
        }

    };


}