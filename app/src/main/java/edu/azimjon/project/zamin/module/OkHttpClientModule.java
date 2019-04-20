package edu.azimjon.project.zamin.module;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import edu.azimjon.project.zamin.interfaces.ApplicationContext;
import edu.azimjon.project.zamin.interfaces.CacheInterceptor;
import edu.azimjon.project.zamin.interfaces.MyApplicationScope;
import edu.azimjon.project.zamin.interfaces.OfflineCacheInterceptor;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module(includes = {ContextModule.class, InterceptorsMudule.class})
public class OkHttpClientModule {


    //adding network interceptors and configuring RETROFIT, GLIDE CACHE
    @Provides
    public OkHttpClient okHttpClient(Cache cache, HttpLoggingInterceptor interceptor,
                                     @OfflineCacheInterceptor Interceptor offlineCacheIntercepror,
                                     @CacheInterceptor Interceptor cacheInterceptor) {

        return new OkHttpClient.Builder()
                .addInterceptor(offlineCacheIntercepror)
                .addNetworkInterceptor(cacheInterceptor)
                .addInterceptor(interceptor)
                .cache(cache)
                .build();
    }


    @Provides
    public Cache cache(File file) {
        return new Cache(file, 1000 * 1000 * 1000); //1000mb
    }

    @Provides
    @MyApplicationScope
    public File file(@ApplicationContext Context context) {
        File file = new File(context.getCacheDir(), "HttpCache");
        file.mkdir();
        return file;
    }

    @Provides
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Timber.d(message));
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}
