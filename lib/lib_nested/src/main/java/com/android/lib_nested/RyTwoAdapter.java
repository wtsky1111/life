package com.android.lib_nested;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wentao *_*
 * on 2019-06-22
 */
public class RyTwoAdapter extends RecyclerView.Adapter<RyTwoAdapter.TextViewHolder> {

    @NonNull
    @Override
    public RyTwoAdapter.TextViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TextViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nested_textitem_two, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder textViewHolder, int i) {
        textViewHolder.bindData(i);
    }


    @Override
    public int getItemCount() {
        return 100;
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }

        public void bindData(int position) {
            tv.setText("当前Ry2的position:" + position);
        }
    }
}