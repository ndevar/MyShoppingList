package com.example.nithy_000.myshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;

import android.app.Activity;

import android.util.Log;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;


import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class SendMailWithoutIntent extends Activity {

    TextView txt;
    Context context =this;
    public static ArrayList<String> lsCat = new ArrayList<>();

    ExpandableListView expandableListView;
    public static ExpandListAdapter adapter;
    List expandableListTitle;
    public static HashMap expandableListDetail;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_mail_without_intent);

        txt=(TextView)findViewById(R.id.txt);

        Typeface iconFont = FontManager.getTypeface(this, FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.send), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.txtMyCart), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.txtAdProduct), iconFont);

        TextView txtAddProduct =(TextView)findViewById(R.id.txtAdProduct);

        txtAddProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AddNewProduct.class);
                startActivity(i);
            }
        });




        TextView send = (TextView) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v) {

                // TODO Auto-generated method stub

                new Thread(new Runnable() {

                    public void run() {

                        try {

                            GMailSender sender = new GMailSender(

                                    "nithya329@gmail.com",

                                    "activa4858");
                            StringBuilder mailText= new StringBuilder();


                            mailText.append("<html><body>");

                            Iterator myVeryOwnIterator = expandableListDetail.keySet().iterator();
                            while(myVeryOwnIterator.hasNext()) {
                                String key=(String)myVeryOwnIterator.next();

                              mailText.append("<div style=\"color:#0000FF;\"><h4><b>");
                                mailText.append(key);
                                mailText.append("</b></h4></div><table border=\"1\" width=\"400\" >");
                                mailText.append("<tr><th>Product</th><th>Quantity</th></tr>");

                                ArrayList<ShoppingList> lstShoppingList=(ArrayList<ShoppingList>)expandableListDetail.get(key);
                                for(int i=0;i<lstShoppingList.size();i++) {
                                mailText.append("<tr><td width=\"300\" >");
                                    mailText.append(lstShoppingList.get(i)._Name);
                                    mailText.append("</td><td>");
                                    mailText.append(lstShoppingList.get(i)._quantity);
                                    mailText.append("</td></tr>");
                                }
                                mailText.append("</table></br>");
                            }

                            mailText.append("</body></html>");

                            sender.sendMail("My List", mailText.toString(),

                                    "nithya329@gmail.com",

                                    "nithyadevarajan@gmail.com");

                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();



                        }

                    }

                }).start();

            }

        });



        DBMain db;
        db = new DBMain(context);

        try {

            db.createDB();
        } catch (IOException ioe) {

            throw new Error("Database not created....");
        }

        try {
            db.openDB();

        }catch(Exception ex){


        }
        db.getCategory();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableList);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList(expandableListDetail.keySet());
        adapter = new ExpandListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                                                          @Override
                                                          public void onGroupCollapse(int groupPosition) {



                                                              ExpandableListAdapter adapter = expandableListView.getExpandableListAdapter();

                                                              int childcount = adapter.getChildrenCount(groupPosition);
                                                              String categoryName = adapter.getGroup(groupPosition).toString();// .getGroup(groupPosition);
                                                              //Log.d("test", groupName);
                                                              for (int i = 0; i < childcount; i++) {

                                                              ShoppingList lst=(ShoppingList) adapter.getChild(groupPosition, i);
                                                              Log.d("test",lst._Name);
                                                              Log.d("test",String.valueOf(lst._quantity));
                                                              }


                                                              Log.d("test", "The Child Count is" + String.valueOf(childcount));

                                                          }
                                                      }

        );

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()

                                                       {
                                                           @Override
                                                           public boolean onChildClick(ExpandableListView parent, View v,
                                                                                       int groupPosition, int childPosition, long id) {


                                                               Log.d("test", "I am in child");

                                                               return false;
                                                           }
                                                       }

            );

        }

    }
