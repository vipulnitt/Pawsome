package com.example.pawsome2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import com.example.pawsome2.MyHandler.MyDbHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {
   ImageView imageView;
   TextView textView;
   Button button,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView = findViewById(R.id.imageView2);
        textView = findViewById(R.id.textView2);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button3);
        Intent intent = getIntent();
        Picasso.get().load(intent.getStringExtra("url")).into(imageView);
        textView.setText("Name:"+intent.getStringExtra("name")+"\nOrigin:"+intent.getStringExtra("origin"));
        textView.append("\nWeight:"+intent.getStringExtra("Weight"));
        textView.append("\nHeight:"+intent.getStringExtra("Height"));
        textView.append("\nBred_for"+intent.getStringExtra("Bred_for"));
        MyDbHandler db = new MyDbHandler(MainActivity2.this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data data = new Data();
                data.setId(intent.getIntExtra("id",0));
                data.setName(intent.getStringExtra("name"));
                data.setOrigin(intent.getStringExtra("origin"));
                data.setBred_for(intent.getStringExtra("bred_for"));
                data.setHeight(intent.getStringExtra("Height"));
                data.setWeight(intent.getStringExtra("Weight"));
                data.setTemprament(intent.getStringExtra("temprament"));
                data.setUrl(intent.getStringExtra("url"));
                db.addFav(data);
           //     vote(createuserRequest(intent.getStringExtra("Image_id")));
            //   fav(favRequest(intent.getStringExtra("Image_id")));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
   shareImage();
            }
        });

    }
   /* public UserRequest createuserRequest(String imageid)
    {

        UserRequest userRequest = new UserRequest();
        userRequest.setImage_id(imageid);
        userRequest.setSub_id("New");
        userRequest.setValue(1);
        return userRequest;
    }
    public FavRequest favRequest(String imageid)
    {
        FavRequest favRequest = new FavRequest();
        favRequest.setImage_id(imageid);
        favRequest.setSub_id("New_user");
      return favRequest;
    }*/


    public void vote(UserRequest userRequest){
        Call<UserResponse> userResponseCall =ApiClient.getUserService().saveUser(userRequest);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                if(response.isSuccessful())
                    Log.d("checkedv","fine");
                else   Log.d("checkedv","Notfine");
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("checkedv","Nfine"+t.getLocalizedMessage());
            }
        });
    }
    /*
    public void fav(FavRequest favRequest){
        Call<UserResponse> userResponseCall =ApiClient.getUserServices().saveUser(favRequest);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                if(response.isSuccessful())
                    Log.d("checkedv","fine");
                else   Log.d("checkedv","Notfine");
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("checkedv","Nfine"+t.getLocalizedMessage());
            }
        });
    }*/
    public void shareImage() {
        BitmapDrawable bitmapDrawable =(BitmapDrawable)imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Dog Love",null);
        Uri bitmapuri = Uri.parse(bitmapPath);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM,bitmapuri);
        startActivity(Intent.createChooser(intent,"Share Image"));
    }
}