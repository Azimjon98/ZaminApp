package edu.azimjon.project.zamin.module;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import edu.azimjon.project.zamin.interfaces.ApplicationContext;
import edu.azimjon.project.zamin.interfaces.CacheInterceptor;
import edu.azimjon.project.zamin.interfaces.OfflineCacheInterceptor;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static edu.azimjon.project.zamin.addition.Constants.API_LOG;
import static edu.azimjon.project.zamin.addition.Constants.DELETE_LOG;
import static edu.azimjon.project.zamin.addition.Constants.ERROR_LOG;

@Module(includes = ContextModule.class)
public class InterceptorsMudule {

    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";

    @CacheInterceptor
    @Provides
    public Interceptor provideCacheInterceptor(@ApplicationContext Context context) {

        return chain -> {
            ConnectivityManager e = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = e.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            Response response = chain.proceed(chain.request());

            CacheControl cacheControl;

            if (isConnected) {
                cacheControl = new CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build();
            } else {
                cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();
            }
            Log.d(API_LOG, "provideCacheInterceptor: isConnected: " + isConnected + "  cache: " + cacheControl.toString());

            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }

    @OfflineCacheInterceptor
    @Provides
    public Interceptor provideOfflineCacheInterceptor(@ApplicationContext Context context) {

        return chain -> {
            ConnectivityManager e = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = e.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


            Request request = chain.request();

            Log.d(API_LOG, "provideOfflineCacheInterceptor: isConnected: " + isConnected);
            if (!isConnected) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                Log.d(API_LOG, "provideOfflineCacheInterceptor: isConnected: " + isConnected + "  cache: " + cacheControl.toString());


                request = request.newBuilder()
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .removeHeader(HEADER_PRAGMA)
                        .cacheControl(cacheControl)
                        .build();

            }
            return chain.proceed(request);
        };
    }

    @Provides
    public boolean isConnected(@ApplicationContext Context context) {
        Log.d(API_LOG, "in isConnected");
        try {
            android.net.ConnectivityManager e = (android.net.ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = e.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            Log.d(ERROR_LOG, "isConnected: " + e.toString());
        }

        return false;
    }
}
