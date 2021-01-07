package edu.azimjon.project.zamin.bases;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyBaseHolder extends RecyclerView.ViewHolder {
    View v;

    public MyBaseHolder(@NonNull View itemView) {
        super(itemView);
        v = itemView;
    }
}
