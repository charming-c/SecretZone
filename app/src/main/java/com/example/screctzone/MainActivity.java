package com.example.screctzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.screctzone.matrix.MatrixToolActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openMatrixActivity(View view) {
        Intent intent = new Intent(this, MatrixToolActivity.class);
        startActivity(intent);
    }
}
