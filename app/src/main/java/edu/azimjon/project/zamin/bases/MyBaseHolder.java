package edu.azimjon.project.zamin.bases;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyBaseHolder extends RecyclerView.ViewHolder {
    View v;

    public MyBaseHolder(@NonNull View itemView) {
        super(itemView);
        v = itemView;
    }
}
