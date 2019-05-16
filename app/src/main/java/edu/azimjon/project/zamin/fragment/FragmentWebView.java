package edu.azimjon.project.zamin.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.navigation.Navigation;

import java.util.Objects;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.databinding.WindowWebViewBinding;
import edu.azimjon.project.zamin.util.MyChromeClient;

import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.WEB_URL;

public class FragmentWebView extends Fragment {
    WindowWebViewBinding binding;
    String url;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = Objects.requireNonNull(getArguments()).getString(WEB_URL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_web_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.iconBack.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack()
        );

        binding.iconShare.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = url;
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.text_share)));
        });

        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setLoadWithOverviewMode(true);
        binding.webview.getSettings().setDisplayZoomControls(true);
        binding.webview.setInitialScale(1);
        binding.webview.getSettings().setUseWideViewPort(true);
//        binding.webview.setWebChromeClient(new MyChromeClient(getActivity()));
//        binding.webview.setWebChromeClient(new WebChromeClient());


        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.progress.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progress.setVisibility(View.GONE);
                FragmentWebView.this.url = url;

            }
        });

        binding.webview.loadUrl(url);
    }


}
