package com.example.screctzone.matrix;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.screctzone.R;
import com.example.screctzone.cyan._1.math.Matrix;
import com.example.screctzone.cyan._1.math.MatrixRing;

import java.util.Map;

public class MatrixItemAdapter extends RecyclerView.Adapter<MatrixItemAdapter.MatrixItemView> {
    private Map<String, Matrix> map;
    private Object[] keys;

    public MatrixItemAdapter(Map<String, Matrix> m) {
        map = m;
    }

    @NonNull
    @Override
    public MatrixItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MatrixItemView(LayoutInflater.from(parent.getContext()).inflate(R.layout.matrix_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MatrixItemView holder, int position) {
//        String[] ss = (String[])map.keySet().toArray();
//        String key; Matrix m;
//        key = ss[position];
//        if (key != null && (m = map.get(key)) != null) {
//            holder.bind(key, m);
//        }

        Object[] ks = keys;
        Map<String, Matrix> mp = map;
        if(ks.length > position){
            String key = (String)ks[position];
            Matrix m = mp.get(key);
            if (key != null && m != null) {
                holder.bind(key, m);
            } else {
                holder.bind("", MatrixRing.instance().zero());
            }
        }
    }

    public void refresh(){
        notifyDataSetChanged();
        keys = map.keySet().toArray();
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public static class MatrixItemView extends RecyclerView.ViewHolder {
        public MatrixItemView(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(String key, Matrix value){
            ((TextView) itemView.findViewById(R.id.matrix_item_view_label_1)).setText(key);
            ((TextView) itemView.findViewById(R.id.matrix_item_view_content_1)).setText(value.toString());
        }
    }
}
