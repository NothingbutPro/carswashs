package com.tukuri.ics;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tukuri.ics.Fragment.Search_fragment;

public class AskNumber  extends AppCompatActivity {
    Button b1,b2;
    EditText area_spin;
    String number;
    boolean enter=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_asknumber);
        b1=(Button)findViewById(R.id.b1);
        b2=(Button)findViewById(R.id.b2);
        area_spin=(EditText) findViewById(R.id.area);


        number = area_spin.getText().toString();


            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!area_spin.getText().toString().isEmpty())
                    {
                        if(area_spin.getText().length()==10)
                        {
                            ni();
                        }
                        else
                        {
                            Toast.makeText(AskNumber.this, "Enter Valid Number", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                        {
                            Toast.makeText(AskNumber.this, "Number Required", Toast.LENGTH_SHORT).show();

                        }
                }
            });













        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(AskNumber.this, AskAreaActivity.class);
                startActivity(in);
            }
        });

        return;
    }

    private void ni() {
        Intent in = new Intent(AskNumber.this, AskAreaActivity.class);
        startActivity(in);
    }
}
