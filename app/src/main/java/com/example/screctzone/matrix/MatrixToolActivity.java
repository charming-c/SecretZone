package com.example.screctzone.matrix;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.screctzone.R;
import com.example.screctzone.cyan._1.MatrixFactory;
import com.example.screctzone.cyan._1.math.Matrix;

import java.util.HashMap;

public class MatrixToolActivity extends AppCompatActivity {

    private HashMap<String, Matrix> map = new HashMap<>();
    TextView display;

    private RecyclerView listview;
    private MatrixItemAdapter adapter;

    private String calc_cmd = "";

    final int REQUEST_1 = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_tool);
        display = findViewById(R.id.matrix_display_1);
        listview = findViewById(R.id.activity_matrix_tool_recyclerview_1);
        listview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MatrixItemAdapter(map);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(data != null) {
                String mark = data.getStringExtra(MatrixToolDialogActivity.MARK_STRING);
                String matrix = data.getStringExtra(MatrixToolDialogActivity.MATRIX_STRING);

                if (mark != null && matrix != null) {
                    map.put(mark, MatrixFactory.newInstance().decode(matrix));
                }
                refreshMapView();
            }
        }
    }

    public void addMatrix(View view) {
        startActivityForResult(new Intent(this, MatrixToolDialogActivity.class), MatrixToolDialogActivity.REQUEST);
    }

    private void refreshMapView() {
        adapter.refresh();
    }

    public void calc(View view) {
        EditText input = new EditText(this);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calc_cmd = s.toString();
            }
        });

        new AlertDialog.Builder(this).setTitle("输入指令:").setView(input).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Matrix m = MatrixFactory.newInstance().decode(calc_cmd.toCharArray(), map);
                    display.setText(m.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }
}