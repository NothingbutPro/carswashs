package com.tukuri.ics;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tukuri.ics.Fragment.Edit_profile_fragment;
import com.tukuri.ics.Fragment.Home_fragment;

import java.util.ArrayList;

public class AskAreaActivity extends Activity {
Button b1;
RelativeLayout lay;
Spinner area_spin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askarea);
        Button b1;
        b1=(Button) findViewById(R.id.b1);
       // lay=(RelativeLayout) findViewById(R.id.lay);
        area_spin=(Spinner) findViewById(R.id.area_spin);
       // lay.setBackgroundResource(R.color.black);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent in = new Intent(AskAreaActivity.this, MainActivity.class);
                startActivity(in);
            }
        });

        ArrayList<String> spinnerarraylist = new ArrayList<>();
        spinnerarraylist.add("Select Area");
        spinnerarraylist.add("Vijay Nagar");
        spinnerarraylist.add("Redisson");
        spinnerarraylist.add("Bhawarkua");
        spinnerarraylist.add("Geeta Bhawan");

        ArrayAdapter aa = new ArrayAdapter(AskAreaActivity.this,android.R.layout.simple_spinner_item,spinnerarraylist);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        area_spin.setAdapter(aa);
        area_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(area_spin.getSelectedItemPosition() !=0) {
                    Intent intent = new Intent(AskAreaActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(AskAreaActivity.this, "Please select Area", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        area_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            public void onClick(View v) {
//                Intent in = new Intent(AskAreaActivity.this, MainActivity.class);
//                startActivity(in);
//
//            }
//
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//                Intent in = new Intent(AskAreaActivity.this, MainActivity.class);
//                startActivity(in);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//            }
//
//        });

return;
    }
}

