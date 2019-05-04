package edu.azimjon.project.zamin.module;

import android.content.Context;
import android.support.constraint.Placeholder;
import android.support.v4.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import dagger.Module;
import dagger.Provides;
import edu.azimjon.project.zamin.interfaces.ApplicationContext;

@Module(includes = {ContextModule.class})
public class GlideModule {
    @Provides
    public RequestManager glideRequestManager(@ApplicationContext Context context, CircularProgressDrawable circularProgressDrawable) {
        return Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions()
//                        .placeholder(circularProgressDrawable)
                        .centerCrop());
    }

    @Provides
    public CircularProgressDrawable circularProgressDrawable(@ApplicationContext Context context) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        return circularProgressDrawable;
    }

}