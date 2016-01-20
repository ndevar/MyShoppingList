package com.example.nithy_000.myshoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddNewProduct extends Activity {
    Context context= this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        if(!SendMailWithoutIntent.lsCat.contains("Please Select a Category")) {
            SendMailWithoutIntent.lsCat.add(0, "Please Select a Category");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,  SendMailWithoutIntent.lsCat);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        RadioButton rdSelectCat=(RadioButton)findViewById(R.id.radioButton1);
        RadioButton rdOtherCat=(RadioButton)findViewById(R.id.radioButton2);
        final EditText editCategory=(EditText)findViewById(R.id.editCategory);

        final EditText editProduct=(EditText)findViewById(R.id.editProduct);

        rdSelectCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                spinner.setClickable(true);
                editCategory.setEnabled(false);
                editCategory.setText("");
            }
        });


        rdOtherCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editCategory.setEnabled(true);
                spinner.setSelection(0);
                spinner.setClickable(false);

            }
        });

        Typeface iconFont = FontManager.getTypeface(this, FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(findViewById(R.id.txtOK), iconFont);
        FontManager.markAsIconContainer(findViewById(R.id.txtCancel), iconFont);

        TextView txtOk=(TextView)findViewById(R.id.txtOK);
        TextView txtCancel=(TextView)findViewById(R.id.txtCancel);

        txtOk.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                String spinnerOption = spinner.getSelectedItem().toString();
                String categoryOption = editCategory.getText().toString();
                String productName = editProduct.getText().toString();
                Log.d("test",spinnerOption+categoryOption+productName);
                if (Validate(spinnerOption, categoryOption, productName)==true)
                {
                    DBMain object = new DBMain(context);
                    String categoryName;
                    if (spinner.getSelectedItem().toString() != "Please Select a Category") {
                        categoryName = spinnerOption;
                        object.InsertProduct(object, categoryName, productName);
                    } else {
                        categoryName = categoryOption;
                        object.InsertDB(object, categoryName, productName);
                        object.InsertProduct(object, categoryName, productName);
                    }
                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SendMailWithoutIntent.class);
                startActivity(i);
            }
        });

    }

    public Boolean Validate(String spinnerOption,String categoryOption,String productName)
    {
        Boolean goNext=true;
        if(spinnerOption=="Please Select a Category" && categoryOption.length()==0 )
        {
                Toast toast = Toast.makeText(context, "Please choose a category", Toast.LENGTH_SHORT);
                toast.show();
                return false;
        }
        if(productName.length()==0)
        {
            Toast toast=Toast.makeText(context,"Please choose a product",Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        //Log.d("test",categoryOption.toLowerCase().trim());
        //String catOption= new String(categoryOption.toLowerCase().trim());
        //ArrayList<String> lstCatoptin= new ArrayList<String>();
        //lstCatoptin.add(categoryOption);
        //Array[String] rr=SendMailWithoutIntent.lsCat.toArray();

        if(  SendMailWithoutIntent.lsCat.contains(categoryOption.toLowerCase().trim()))
        {
            Toast toast=Toast.makeText(context,"Category already exists.Please choose from existing category",Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return goNext;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
