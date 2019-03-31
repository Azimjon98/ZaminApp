package edu.azimjon.project.zamin.module;


import android.content.Context;

import dagger.Module;
import dagger.Provides;
import edu.azimjon.project.zamin.interfaces.ApplicationContext;
import edu.azimjon.project.zamin.interfaces.MyApplicationScope;

@ApplicationContext
@Module
public class ContextModule {
    Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @MyApplicationScope
    @ApplicationContext
    @Provides
    Context context() {
        return context.getApplicationContext();
    }
}
