package com.bhavaniprasad.wikisearch.remote;

import com.bhavaniprasad.wikisearch.model.wikiUsersList;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/***
 * Gets data from Wikipedia API
 */
public interface ApiService {
    @GET("api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=100&pilimit=30&wbptterms=description")
    Call<wikiUsersList> getUsersList(@Query("gpssearch") String gpssearch,@Query("gpslimit") int gpslimit);
}

