package com.example.nithy_000.myshoppinglist;

/**
 * Created by nithy_000 on 20-11-2015.
 */
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap getData() {
        HashMap expandableListDetail = new HashMap();
        for(int i=0;i<SendMailWithoutIntent.lsCat.size();i++) {
            GetChildList(SendMailWithoutIntent.lsCat.get(i),expandableListDetail);
        }
        return expandableListDetail;
    }

    private static void GetChildList(String s, HashMap expandableListDetail) {

        SQLiteDatabase db1;
        String myPath="/data/data/com.example.nithy_000.myshoppinglist/databases/"+"test"+".sqlite";
        db1 = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor c= db1.rawQuery("select * from Product inner join Category on Product.Cat_Id=Category.Id where Category.Category_Name='"+s+"'",null);
        ArrayList<ShoppingList> lstShoppingList= new ArrayList<>();
        c.moveToFirst();

        while(! c.isAfterLast())
        {
            String cat=c.getString(0);

            String cat1=c.getString(1);
            String cat2=c.getString(4);
            int quantity=0;
            if(cat1!=null)
            {
                quantity=Integer.parseInt(cat1);
            }
            ShoppingList list= new ShoppingList(cat,quantity,cat2);
            lstShoppingList.add(list);
            c.moveToNext();

        }
        expandableListDetail.put(s,lstShoppingList);
    }
}