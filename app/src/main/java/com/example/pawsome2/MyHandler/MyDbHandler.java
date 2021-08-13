package com.example.pawsome2.MyHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pawsome2.Data;
import com.example.pawsome2.Params.params;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {
   public MyDbHandler(Context context)
    {
        super(context, params.DB_NAME,null,params.DB_VERSION);
        Log.d("vipuldb","Sucess");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("vipuldb","Sucessful");
        String create = "CREATE TABLE "+params.TABLE_NAME + "(" + params.KEY_ID + " INTEGER PRIMARY KEY," + params.KEY_NAME + " TEXT," + params.KEY_URL + " TEXT," + params.KEY_ORGIN + " TEXT," + params.KEY_BRED_FOR + " TEXT," + params.KEY_WEIGHT + " TEXT," + params.KEY_Height + " TEXT," + params.KEY_TEMPRAMENT + " TEXT)";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addFav(Data data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(params.KEY_ID,data.getId());
        values.put(params.KEY_NAME,data.getName());
        values.put(params.KEY_ORGIN,data.getOrigin());
        values.put(params.KEY_BRED_FOR,data.getBred_for());
        values.put(params.KEY_TEMPRAMENT,data.getTemprament());
        values.put(params.KEY_WEIGHT,data.getWeight());
        values.put(params.KEY_Height,data.getHeight());
        values.put(params.KEY_URL,data.getUrl());
        db.insert(params.TABLE_NAME,null,values);
        Log.d("vipuldb","Sucessfully Added"+values.get(params.KEY_ID));
        db.close();
    }
    public List<Data> getalldata(){
       List<Data> favlist = new ArrayList<>();
       SQLiteDatabase db = this.getReadableDatabase();
       String select = "SELECT * FROM "+params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select,null);
        if(cursor.moveToFirst())
        {
            do{
                Data data = new Data();
                data.setId(cursor.getInt(0));
                data.setName(cursor.getString(1));
                data.setUrl(cursor.getString(2));
                data.setOrigin(cursor.getString(3));
                data.setBred_for(cursor.getString(4));
                data.setWeight(cursor.getString(5));
                data.setHeight(cursor.getString(6));
                data.setTemprament(cursor.getString(7));
                favlist.add(data);
            }while(cursor.moveToNext());
        }
        return favlist;
    }
    public void delete(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(params.TABLE_NAME,params.KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }

}
