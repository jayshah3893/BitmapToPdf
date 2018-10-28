package com.sample.generatepdf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FormActivity extends AppCompatActivity {

    EditText edName, edMobileNumber, edAddress, edBloodGroup;
    Button btn_generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        init();
    }

    private void init() {
        edName = (EditText) findViewById(R.id.edName);
        edMobileNumber = (EditText) findViewById(R.id.edMobileNumber);
        edAddress = (EditText) findViewById(R.id.edAddress);
        edBloodGroup = (EditText) findViewById(R.id.edBloodGroup);
        btn_generate = (Button) findViewById(R.id.btn_generate);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edName.getText().length() > 0 &&
                        edMobileNumber.getText().length() > 0 &&
                        edAddress.getText().length() > 0 &&
                        edBloodGroup.getText().length() > 0){

                    Intent  intent = new Intent(FormActivity.this,MainActivity.class);
                    intent.putExtra("NAME",edName.getText().toString());
                    intent.putExtra("MOBILE",edMobileNumber.getText().toString());
                    intent.putExtra("ADDRESS",edAddress.getText().toString());
                    intent.putExtra("BLOOD", edBloodGroup.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }
}
