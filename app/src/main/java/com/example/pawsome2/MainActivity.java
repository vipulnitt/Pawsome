package com.example.pawsome2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pawsome2.MyHandler.MyDbHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {
    int check=0,btcheck=0;
    RecyclerView recyclerView;
    List<Data> data;
    Button button;
    Adapter adapter;
    SearchView searchView;
    String url = "https://api.thedogapi.com/v1/breeds/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleview);
        searchView = (SearchView)findViewById(R.id.searchView);
        button = findViewById(R.id.button2);
        data = new ArrayList<>();
        extractdata();
        recyclerView.hasFixedSize();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final ItemTouchHelper[] itemTouchHelper = {new ItemTouchHelper(simpleCallback)};
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btcheck==0){
                    button.setText("Back");
                    url = "https://api.thedogapi.com/v1/favourites?api_key=da2396a3-2f31-4346-8d2c-97543f419603";
                    btcheck=1;
                    check=2;
                    itemTouchHelper[0].attachToRecyclerView(recyclerView);
                    data.clear();
                    adapter =new Adapter(getApplicationContext(),data);
                    recyclerView.setAdapter(adapter);
                    createfav();
                }
                else if(btcheck==1)
                {
                    url="https://api.thedogapi.com/v1/breeds/";
                    button.setText("Favourite");
                    btcheck=0;
                    check=0;
                    itemTouchHelper[0].attachToRecyclerView(null);
                    data.clear();
                    adapter =new Adapter(getApplicationContext(),data);
                    recyclerView.setAdapter(adapter);
                    extractdata();
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "Hello!", Toast.LENGTH_SHORT).show();
                data.clear();
                check=1;
                adapter =new Adapter(getApplicationContext(),data);
                recyclerView.setAdapter(adapter);
                url="https://api.thedogapi.com/v1/breeds/search?q="+query;
                extractdata();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                url="https://api.thedogapi.com/v1/breeds/";
                data.clear();
                adapter =new Adapter(getApplicationContext(),data);
                recyclerView.setAdapter(adapter);
                extractdata();
                check=0;
                return false;
            }
        });

    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position= viewHolder.getAdapterPosition();
            Log.d("deleteid",""+direction);
            MyDbHandler db = new MyDbHandler(MainActivity.this);
            db.delete(data.get(position).getId());
            data.remove(position);
            adapter.notifyItemRemoved(position);
        }
    };
    public void extractdata()
    {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.DEPRECATED_GET_OR_POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Data dat = new Data();
                        if(check==2)
                        {
                            getimgurl(obj.optString("image_id"),obj.optInt("id"));

                        }
                        if(check==1)
                        {
                            getimgurl( obj.optString("reference_image_id"),0);
                        }
                        if(check==0) {
                            dat.setName(obj.getString("name"));
                            JSONObject img = obj.getJSONObject("image");
                            if (img.optString("url") != ""){
                                dat.setUrl(img.optString("url"));
                                dat.setImage_id(img.optString("id"));
                            }
                            else {
                                dat.setUrl("https://miro.medium.com/max/880/0*H3jZONKqRuAAeHnG.jpg");
                            }
                            dat.setBred_for(obj.optString("bred_for"));
                            dat.setOrigin(obj.optString("origin"));
                            dat.setTemprament(obj.optString("temperament"));
                            dat.setHeight(obj.optString("height"));
                            dat.setWeight(obj.optString("weight"));
                            dat.setId(obj.getInt("id"));
                            data.add(dat);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("abcd", "Error");
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter =new Adapter(getApplicationContext(),data);
                recyclerView.setAdapter(adapter);
            }

        }, error -> {
            Toast.makeText(MainActivity.this, "Something wents wrong!", Toast.LENGTH_SHORT).show();
            //   Log.d("abcd","Error");
        });

        requestQueue.add(jsonArrayRequest);
    }
    void getimgurl(String id,int favid)
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.thedogapi.com/v1/images/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Data dat = new Data();
                    JSONArray jsonArray = response.getJSONArray("breeds");
                    dat.setUrl(response.getString("url"));
                    JSONObject obj =jsonArray.getJSONObject(0);
                    dat.setName(obj.getString("name"));
                    dat.setBred_for(obj.optString("bred_for"));
                    dat.setOrigin(obj.optString("origin"));
                    dat.setTemprament(obj.optString("temperament"));
                    dat.setHeight(obj.optString("height"));
                    dat.setWeight(obj.optString("weight"));
                    dat.setId(obj.getInt("id"));
                    if(favid!=0)
                    {
                        dat.setId(favid);
                    }
                    data.add(dat);
                }
                catch (JSONException e){
                    e.printStackTrace();

                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter =new Adapter(getApplicationContext(),data);
                recyclerView.setAdapter(adapter);
            }

        }, error -> {
            Toast.makeText(MainActivity.this, "Something wents wrong! in this", Toast.LENGTH_SHORT).show();
            //   Log.d("abcd","Error");
        });
        requestQueue.add(jsonObjectRequest);
    }

/*public void deletefav(int id)
{
    Call<DeleteResponse> call = ApiClient.getUserServicess().deleteItem(id);
    call.enqueue(new Callback<DeleteResponse>() {
        @Override
        public void onResponse(Call<DeleteResponse> call, retrofit2.Response<DeleteResponse> response) {
            if(response.isSuccessful())
                Log.d("checkedv","fine");
            else   Log.d("checkedv","Notfine");
        }

        @Override
        public void onFailure(Call<DeleteResponse> call, Throwable t) {
            Log.d("checkedv","Nfine"+t.getLocalizedMessage());
        }
    });
}*/
void createfav()
{
    MyDbHandler db = new MyDbHandler(MainActivity.this);
    List<Data> favdata = db.getalldata();
    for(Data d:favdata)
    {
        data.add(d);
    }
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    adapter =new Adapter(getApplicationContext(),data);
    recyclerView.setAdapter(adapter);
}

}


