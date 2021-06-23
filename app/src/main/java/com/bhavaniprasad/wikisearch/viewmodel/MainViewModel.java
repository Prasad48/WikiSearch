package com.bhavaniprasad.wikisearch.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhavaniprasad.wikisearch.MainActivity;
import com.bhavaniprasad.wikisearch.model.wikiUsersList;
import com.bhavaniprasad.wikisearch.remote.ApiMaker;
import com.bhavaniprasad.wikisearch.remote.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private ArrayList<wikiUsersList> arrlist;

    Call<wikiUsersList> usersListcall;
    public MutableLiveData<ArrayList<wikiUsersList>> UsersListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> showToast = new MutableLiveData<>();

    public LiveData<ArrayList<wikiUsersList>> getMutableLiveData(MainActivity mainActivity, String query) {
        try{
            ApiService apiService = new ApiMaker().getService();
            usersListcall= apiService.getUsersList(query,30);
            usersListcall.enqueue(new Callback<wikiUsersList>() {
                @Override
                public void onResponse(Call<wikiUsersList> call, Response<wikiUsersList> response) {
                    if (response.isSuccessful()) {
                        arrlist=new ArrayList<>();
                        arrlist.add(response.body());
                        if(arrlist.get(0).getQuery()!=null) {
                            UsersListMutableLiveData.setValue((ArrayList<wikiUsersList>) arrlist.get(0).getQuery().getPages());
                            showToast.setValue(false);
                        }
                        else
                            showToast.setValue(true);

                    } else {
                        Log.d("error message", "error");
                    }
                }

                @Override
                public void onFailure(Call<wikiUsersList> call, Throwable t) {
                    String error_message= t.getMessage();
                    Log.d("Error loading data", error_message);
                }

            });

        }
        catch (Exception e){
            Log.d("Exception","Auth exception");
        }
        return UsersListMutableLiveData;
    }

    public LiveData<Boolean> showToastonNullResults() {
        return showToast;
    }
}
