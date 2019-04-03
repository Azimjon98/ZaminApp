package edu.azimjon.project.zamin.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyRestService {

    @GET(".")
    Call<JsonObject> getNewsData(@Query("offset") String offset,
                             @Query("limit") String limit,
                             @Query("lang") String lang);

    @GET(".")
    Call<JsonObject> getMainNews(@Query("offset") String offset,
                                 @Query("limit") String limit,
                                 @Query("lang") String lang,
                                 @Query("main") String main);




    @GET(".")
    Call<JsonObject> getNewsWithCategory(@Query("offset") String offset,
                                         @Query("limit") String limit,
                                         @Query("lang") String lang,
                                         @Query("category") String category);

    @GET(".")
    Call<JsonObject> getTopNews(@Query("offset") String offset,
                                @Query("limit") String limit,
                                @Query("tagname") String popular,
                                @Query("lang") String lang);

    @GET("article.php")
    Call<JsonObject> getNewsContentWithId(@Query("id") String id,
                                          @Query("lang") String lang);

    @GET("tags.php")
    Call<JsonObject> getNewsContentTags(@Query("id") String id,
                                        @Query("lang") String lang);

    @GET("category.php")
    Call<JsonArray> getAllCategories(@Query("lang") String lang);


    @GET("tags.php")
    Call<JsonObject> getNewsContentTags(@Query("offset") String offset,
                                        @Query("limit") String limit,
                                        @Query("tagname") String tagname,
                                        @Query("lang") String lang);



}
