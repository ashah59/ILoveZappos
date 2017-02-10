package com.example.shah.ilovezappos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_FILE = "SearchPref";
    public static final String PREFS_NAME = "search";
    SearchAdapter adapter;
    ArrayList<SearchHistory> searchArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize search list view and set empty list message
        final ListView listView = (ListView) findViewById(R.id.lvSearch);
        TextView emptyText = (TextView)findViewById(R.id.textViewEmptySearch);
        listView.setEmptyView(emptyText);

        //Initializing retrofit with zappos API as baseUrl and using Gson as converter factory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.zappos.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ZapposService service = retrofit.create(ZapposService.class);

        //Get search file from SharedPreference
        final SharedPreferences preferences = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        //Get search JSON string
        String searchJSONString = preferences.getString(PREFS_NAME, "");
        final SharedPreferences.Editor editor = preferences.edit();
        //Define deserialization type
        final Type listType = new TypeToken<ArrayList<SearchHistory>>() {
        }.getType();
        //Using Gson as deserializer
        final Gson gson = new Gson();

        if (!searchJSONString.isEmpty()) {
            //Deserialize JSON
            searchArrayList = gson.fromJson(searchJSONString, listType);

            //Initialize adapter and set it to list view
            adapter = new SearchAdapter(this, R.layout.search_row, searchArrayList);
            adapter.setNotifyOnChange(true);
            listView.setAdapter(adapter);
        }

        //list view item on click listener to again search for the product and go to product activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Call<SearchResult> call = service.getSearchResults(searchArrayList.get(i).getProductId(), "b743e26728e16b81da139182bb2094357c31d331");

                //asynchronous call to get search result from REST request
                call.enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        if (response.body().getStatusCode().equals("200") && Integer.parseInt(response.body().getCurrentResultCount()) > 0) {
                            List<Product> products = response.body().getProducts();
                            Log.d("demo", products.toString());

                            //Go to product activity
                            Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                            intent.putExtra("product", products.get(0));
                            startActivity(intent);
                        } else {
                            Log.d("demo", "status code - " + response.body().getStatusCode());
                            Log.d("demo", "current result count - " + response.body().getCurrentResultCount());
                            Toast.makeText(MainActivity.this, "Product is no longer available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
                        Log.d("demo", t.getMessage());
                    }
                });
            }
        });

        //list view item on long click listener to delete particular search item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchArrayList.remove(i);
                adapter.notifyDataSetChanged();

                //save updated search array to SharedPreference
                String jsonString = gson.toJson(searchArrayList);
                editor.putString(PREFS_NAME, jsonString);
                editor.commit();
                return true;
            }
        });

        //clears content in the search area
        final EditText etSearch = (EditText) findViewById(R.id.editTextSearch);
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etSearch.setText("");
                return false;
            }
        });

        //On search click
        ImageView btnSearch = (ImageView) findViewById(R.id.buttonSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSearch.getText().length() > 0) {
                    Call<SearchResult> call = service.getSearchResults(etSearch.getText().toString().replace(" ", "_"), "b743e26728e16b81da139182bb2094357c31d331");

                    //asynchronous call to get search result from REST request
                    call.enqueue(new Callback<SearchResult>() {
                        @Override
                        public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                            if (response.body().getStatusCode().equals("200") && Integer.parseInt(response.body().getCurrentResultCount()) > 0) {
                                List<Product> products = response.body().getProducts();
                                Log.d("demo", products.toString());

                                if (searchArrayList == null) {
                                    searchArrayList = new ArrayList<SearchHistory>();
                                }
                                searchArrayList.add(new SearchHistory(products.get(0).getProductName(), products.get(0).getThumbnailImageUrl(), products.get(0).getProductId()));

                                //Initialize adapter and set it to list view if not done already
                                if (adapter == null) {
                                    adapter = new SearchAdapter(MainActivity.this, R.layout.search_row, searchArrayList);
                                    adapter.setNotifyOnChange(true);
                                    listView.setAdapter(adapter);
                                } else
                                    adapter.notifyDataSetChanged();

                                //save updated search array to SharedPreference
                                String jsonString = gson.toJson(searchArrayList);
                                editor.putString(PREFS_NAME, jsonString);
                                editor.commit();

                                //Go to product activity
                                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                                intent.putExtra("product", products.get(0));
                                startActivity(intent);
                            } else {
                                Log.d("demo", "status code - " + response.body().getStatusCode());
                                Log.d("demo", "current result count - " + response.body().getCurrentResultCount());
                                Toast.makeText(MainActivity.this, "No result found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SearchResult> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Search failed", Toast.LENGTH_SHORT).show();
                            Log.d("demo", t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Please enter Product name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //On click of delete all searches, confirm and clear array and reset SharedPreference
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabDel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this).setMessage("Are you sure you want to delete all your searches?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                searchArrayList.clear();
                                adapter.notifyDataSetChanged();

                                editor.putString(PREFS_NAME, "");
                                editor.commit();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    public interface ZapposService {
        @GET("Search")
        Call<SearchResult> getSearchResults(@Query("term") String term, @Query("key") String key);
    }
}
