package com.example.nithy_000.myshoppinglist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nithy_000 on 20-11-2015.
 */
public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    ExpandListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {

                final ShoppingList childText = (ShoppingList) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.txt);
        final TextView txtQuantity = (TextView) convertView
                .findViewById(R.id.quantity);

        final EditText editQuantity = (EditText) convertView
                .findViewById(R.id.editQuantity);

        String[] some_array = _context.getResources().getStringArray(R.array.measurement);

        //Log.d("test",childText._unit);

        //Log.d("test","The array contains"+String.valueOf(Arrays.asList(some_array).contains(childText._unit)));

        int position= Arrays.asList(some_array).indexOf(childText._unit);
        //Log.d("test","position is "+String.valueOf(position));
        //Log.d("test","unit of array is "+childText._unit);
        Spinner spinner=(Spinner)convertView.findViewById(R.id.spinner1);

        spinner.setSelection(position);



        txtQuantity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                editQuantity.setVisibility(View.VISIBLE);
            }
        });


        RelativeLayout MainLayout = (RelativeLayout) convertView.findViewById(R.id.mainLayout);

        MainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (editQuantity.isFocused()) {
                    txtQuantity.setText(editQuantity.getText().toString());
                    editQuantity.setVisibility(View.GONE);
                    ShoppingList spList=new ShoppingList(childText._Name,Integer.parseInt(editQuantity.getText().toString()),childText._unit);
                    //SendMailWithoutIntent.expandableListDetail.put(parent,spList);
                    SendMailWithoutIntent.adapter.notifyDataSetChanged();
                }
                return false;
            }
        });



        txtListChild.setText(childText._Name);
        txtQuantity.setText(String.valueOf(childText._quantity));

        Typeface iconFont = FontManager.getTypeface(_context, FontManager.FONTAWESOME);
        //FontManager.markAsIconContainer(convertView.findViewById(R.id.txtIncrease), iconFont);
        //FontManager.markAsIconContainer(convertView.findViewById(R.id.txtDecrease), iconFont);


        return convertView;
    }




    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandlistview, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.Name);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}