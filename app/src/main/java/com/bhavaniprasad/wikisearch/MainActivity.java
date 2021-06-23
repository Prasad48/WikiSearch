package com.bhavaniprasad.wikisearch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bhavaniprasad.wikisearch.StoreSharedPreferences.SaveAndGetPrefs;
import com.bhavaniprasad.wikisearch.adapter.UsersAdapter;
import com.bhavaniprasad.wikisearch.model.wikiUsersList;
import com.bhavaniprasad.wikisearch.viewmodel.MainViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private Context context;
    private ProgressBar progressBar;
    private RelativeLayout layout_nonetwork;
    private UsersAdapter usersAdapter;
    private RecyclerView recyclerView;
    private ArrayList<wikiUsersList> arrayList;
    NetworkConnection object;
    private ArrayList<wikiUsersList> usersfrommdb2;
    SaveAndGetPrefs saveAndGetPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersfrommdb2=new ArrayList<>();
        progressBar = findViewById(R.id.progress_bar);
        layout_nonetwork = findViewById(R.id.no_network2);
        recyclerView=findViewById(R.id.usersrecyclerview);
        context=this;
        object = new NetworkConnection();
        saveAndGetPrefs=new SaveAndGetPrefs();
        mainViewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);
        arrayList=new ArrayList<>();
        usersAdapter = new UsersAdapter(MainActivity.this, arrayList);

        if(!object.isConnected(this)){
            try{
                final ArrayList<wikiUsersList> result=saveAndGetPrefs.getArrayList("USERSLIST",context);
                if(result.size()>0)
                    setRecyclerView(result,context);
                else
                    showOfflineAlert("");
            }
            catch (Exception e){
                showOfflineAlert("");
                Log.d("GOT Exception","GOT EXCEPTION"+e);
            }
        }
        else {
            layout_nonetwork.setVisibility(View.INVISIBLE);
            getdata("wikipedia");
        }
    }

    /***
     * Shows an Alert on First time offline mode
     */
    private void showOfflineAlert(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("No Internet");
        if(msg=="New")
            alertDialog.setMessage("Please SwitchOn the Internet to fetch the New Input Data!");
        else
            alertDialog.setMessage("Please SwitchOn the Internet to view the app in Offline Mode from next time Onwards!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        Toast.makeText(context, "Please SwitchOn the Internet to view the app in Offline Mode from next time Onwards!",
                Toast.LENGTH_LONG).show();
    }

    /***
     * Sets RecyclerView, shows list of data
     * @param result
     * @param context
     */
    private void setRecyclerView(final ArrayList<wikiUsersList> result, final Context context) {
        layout_nonetwork.setVisibility(View.INVISIBLE);
        usersAdapter = new UsersAdapter(MainActivity.this, result);
        usersAdapter.setOnUsersClick(new UsersAdapter.OnUsersclicklistener() {
            @Override
            public void onclickuser(int adapterposition) {
                Intent browseintent = new Intent(context,SecondActivity.class);
                browseintent.putExtra("url",result.get(adapterposition).getTitle());
                context.startActivity(browseintent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(usersAdapter);
    }

    /***
     * Loads data from the Wikipedia API
     * @param query
     */
    private void getdata(String query) {
        mainViewModel.getMutableLiveData(MainActivity.this, query).observe(MainActivity.this, new Observer<ArrayList<wikiUsersList>>() {
            @Override
            public void onChanged(final ArrayList<wikiUsersList> list) {
                if(list.size()>0) {
                    saveAndGetPrefs.saveArrayList(list,"USERSLIST",context);

                    for(int i=0;i<list.size();i++){
                        wikiUsersList setdata=new wikiUsersList(list.get(i).getTitle(),list.get(i).getDescription(),list.get(i).getThumbnail());
                        setdata.setTitle(list.get(i).getTitle());
                        setdata.setDescription(list.get(i).getDescription());
                        setdata.setThumbnail(list.get(i).getThumbnail());

                    }
                    setRecyclerView(list,context);
                }
            }
        });

        mainViewModel.showToastonNullResults().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Please Provide a valid one word input",
                            Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });

    }

    /***
     * SearchView for Searching Wikipedia
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(isInternetAvailable()) {
                    getdata(query);
                    usersAdapter.getFilter().filter(query);
                }
                else {
                    showOfflineAlert("New");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usersAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /***
     * Checks Internet Connection
     * @return
     */
    public boolean isInternetAvailable(){
        if(!object.isConnected(this))
            return false;
        else
            return true;
    }

}