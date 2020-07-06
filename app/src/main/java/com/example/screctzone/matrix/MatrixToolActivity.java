package com.example.screctzone.matrix;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.screctzone.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class MatrixToolActivity extends AppCompatActivity {

    private HashMap<String, Matrix> map = new HashMap<>();
    TextView display;

    final int REQUEST_1 = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_tool);
        display = findViewById(R.id.matrix_display_1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, resultCode + "", Toast.LENGTH_LONG).show();
        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "onActivityResult_2", Toast.LENGTH_LONG).show();
            if(data != null) {
                String mark = data.getStringExtra(MatrixToolDialogActivity.MARK_STRING);
                String matrix = data.getStringExtra(MatrixToolDialogActivity.MATRIX_STRING);

                if (mark != null && matrix != null) {
                    map.put(mark, Matrix.decode(matrix));
                }
                refreshMapView();
            }
        }
    }

    public void addMatrix(View view) {
        startActivityForResult(new Intent(this, MatrixToolDialogActivity.class), MatrixToolDialogActivity.REQUEST);
    }

    private void refreshMapView() {
        Collection<Matrix> set = map.values();
        Matrix[] ms = new Matrix[set.size()];
        set.toArray(ms);
        Toast.makeText(this, "refreshMapView", Toast.LENGTH_LONG).show();
        if(set.size() > 0){
            display.setText(ms[0].toString());
        }
    }
}