package edu.azimjon.project.zamin.module;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import edu.azimjon.project.zamin.interfaces.ApplicationContext;
import edu.azimjon.project.zamin.interfaces.MyApplicationScope;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module(includes = ContextModule.class)
public class OkHttpClientModule {


    //adding network interceptors and configuring RETROFIT, GLIDE CACHE
    @Provides
    public OkHttpClient okHttpClient(Cache cache, HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)

                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url().newBuilder().addQueryParameter("lang", "ru").build();
                        request = request.newBuilder().url(url).build();

                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .request(request)
                                .removeHeader("Pragma")
                                .header("Cache-Control", "max-age=60")
                                .build();
                    }
                })
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(cache)
                .build();
    }

    @Provides
    public Cache cache(File file) {
        return new Cache(file, 100 * 1000 * 1000); //10mb
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
