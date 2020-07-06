package com.example.screctzone.matrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.screctzone.R;

public class MatrixToolDialogActivity extends AppCompatActivity {

    EditText mark;
    EditText matrix;
    Button confirm;
    Button cancel;

    String mark_string;
    String matrix_string;

    public static int REQUEST = 1;

    public static String MARK_STRING = "mark_string";
    public static String MATRIX_STRING = "matrix_string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_tool_dialog);

        mark = findViewById(R.id.editTextTextPersonName2);
        matrix = findViewById(R.id.editTextTextPersonName3);
        confirm = findViewById(R.id.button4);
        cancel = findViewById(R.id.button5);

        mark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mark_string = s.toString();
            }
        });
        matrix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                matrix_string = s.toString();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(MARK_STRING, mark_string);
                i.putExtra(MATRIX_STRING, matrix_string);

                setResult(RESULT_OK, i);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}