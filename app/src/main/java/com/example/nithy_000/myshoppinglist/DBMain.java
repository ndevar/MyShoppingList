package com.example.nithy_000.myshoppinglist;


/**
 * Created by nithy_000 on 17-11-2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBMain extends SQLiteOpenHelper {
    private static String DB_PATH= "/data/data/com.example.nithy_000.myshoppinglist/databases/";
    private static String DB_NAME = "test.sqlite";
    private SQLiteDatabase dbObj;
    private final Context context;

    public DBMain(Context context) {
        super(context, DB_NAME, null, 5);
        this. context  = context;
    }

    public void createDB() throws IOException {

        try {

           Log.d("test", String.valueOf(checkDB())) ;
            if(checkDB()==false) {
                CopyDatabase(context);
            }
            Log.d("test", String.valueOf(checkDB())) ;

        } catch (Exception e) {

            throw new Error("Error copying database");
        }
    }


    private boolean checkDB() {

        boolean checkdb = false;
        try {

            File databaseFile = new File( "/data/data/com.example.nithy_000.myshoppinglist/databases/test.sqlite");

            if (databaseFile.exists()){
                checkdb=true;
            }

            //String myPath = "/data/data/com.example.nithy_000.myshoppinglist/databases/test.sqlite";
            //File dbfile = new File(myPath);
            //checkdb = dbfile.exists();

        } catch (SQLiteException e) {
            e.printStackTrace();

        }
        return checkdb;
    }

    //Method to insert the category and the related product into the table
    public void InsertDB(DBMain main, String catName, String productName)
    {
        try
        {
            //dbObj =  context.getAssets(DB_NAME).toString() .open(DB_NAME);//SQLiteDatabase.openDatabase(context.getAssets().open(DB_NAME).toString(), null, SQLiteDatabase.OPEN_READWRITE);
            dbObj=main.getWritableDatabase();
            ContentValues cv = new ContentValues();
            //cv.putNull("Id");
            cv.put("Category_Name", catName.toLowerCase());
            dbObj.insert("Category", null, cv);
            //InsertProduct(main,catName,productName);//
        }
        catch(Exception ex)
        {

        }

    }

    //Method to insert the category and the related product into the table
    public void InsertProduct(DBMain main, String catName, String productName)
    {
        try
        {
            int row_Id=0;
            Log.d("test","In Product Insert");
            dbObj=main.getWritableDatabase();
            Cursor c= dbObj.rawQuery("select Id from Category where Category_Name='"+catName+"'",null);
            Log.d("test","After Query");
            c.moveToFirst();
            Log.d("test", "Before Iteration Query");
            while(! c.isAfterLast()) {

                row_Id=Integer.parseInt(c.getString(0));
                c.moveToNext();
            }

            Log.d("test","Value of new row "+String.valueOf(row_Id));

            if(row_Id!=0)
            {
                ContentValues productValues = new ContentValues();
                productValues.put("Prod_Name",productName.toLowerCase());
                productValues.putNull("Quantity");
                productValues.put("Cat_Id", row_Id);
                dbObj.insert("Product", null, productValues);
            }

        }
        catch(Exception ex)
        {

        }

    }

    public void getCategory()
    {
        try
        {
            dbObj=SQLiteDatabase.openDatabase(DB_PATH+DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor c= dbObj.rawQuery("select Category_Name from Category",null);
            c.moveToFirst();
            SendMailWithoutIntent.lsCat=new ArrayList<>();
            while(! c.isAfterLast())
            {
                String cat=c.getString(0);
                SendMailWithoutIntent.lsCat.add(cat.toLowerCase().trim());
                c.moveToNext();
            }
        }
        catch (Exception ex)
        {

        }
    }



    public void CopyDatabase(Context context)
    {
        Log.d("test",
                "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try
        {
            myInput =context.getAssets().open(DB_NAME);
            Log.d("test",
                    "Input stream done");
            // transfer bytes from the inputfile to the
            // outputfile

            File databaseFile = new File( "/data/data/com.example.nithy_000.myshoppinglist/databases");

            if (!databaseFile.exists()){
                databaseFile.mkdir();
            }


            myOutput =new FileOutputStream(DB_PATH+ DB_NAME);
            Log.d("test",
                    "Output stream done");
            while((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.d("test","New database has been copied to device!");


        }
        catch(Exception ex)
        {

        }
    }

    public void openDB() throws SQLException
    {
        Log.d("test","Before opening db");
        dbObj = SQLiteDatabase.openDatabase(DB_PATH+DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        Log.d("test","After opening db");
    }

    @Override
    public synchronized void close() {

        if(dbObj != null)
            dbObj.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}