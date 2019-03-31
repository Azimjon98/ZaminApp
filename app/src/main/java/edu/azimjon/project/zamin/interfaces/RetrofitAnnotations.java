package edu.azimjon.project.zamin.interfaces;

import javax.inject.Qualifier;

public final class RetrofitAnnotations {
    @Qualifier
    public @interface MyBaseUrl {
    }

    @Qualifier
    public @interface RetrofitApp {
    }

}
