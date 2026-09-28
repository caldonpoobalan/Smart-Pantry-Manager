package com.smartpantry.manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.R;
import com.smartpantry.manager.model.RecpEntity;

import java.util.List;

// adapter for displaying matched recipe cards in recyclerview
public class RecpCardAdapt extends RecyclerView.Adapter<RecpCardAdapt.RecpHolder> {

    private List<RecpEntity> recpDataLst;
    private OnRecpCardClickListener cardClickListnr;

    // handles click callback interface for recipe selection
    public interface OnRecpCardClickListener {
        void onRecpSelected(RecpEntity recp);
    }

    public RecpCardAdapt(List<RecpEntity> recpDataLst, OnRecpCardClickListener cardClickListnr) {
        this.recpDataLst = recpDataLst;
        this.cardClickListnr = cardClickListnr;
    }

    @NonNull
    @Override
    public RecpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardVw = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_recp_card, parent, false);
        return new RecpHolder(cardVw);
    }

    @Override
    public void onBindViewHolder(@NonNull RecpHolder holder, int position) {
        RecpEntity recpRowItm = recpDataLst.get(position);
        holder.txtRecpTitle.setText(recpRowItm.getRecpNm());
        holder.txtRecpSummary.setText(recpRowItm.getRecpDesc());

        // tap the recipe card to view full instructions
        holder.itemView.setOnClickListener(v -> {
            if (cardClickListnr != null) {
                cardClickListnr.onRecpSelected(recpRowItm);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recpDataLst == null ? 0 : recpDataLst.size();
    }

    // handles refreshing recipe list data
    public void refreshRecpData(List<RecpEntity> freshRecps) {
        this.recpDataLst = freshRecps;
        notifyDataSetChanged();
    }

    // view holder caching views for recipe cards
    public static class RecpHolder extends RecyclerView.ViewHolder {
        public TextView txtRecpTitle;
        public TextView txtRecpSummary;

        public RecpHolder(View itemView) {
            super(itemView);
            txtRecpTitle = itemView.findViewById(R.id.txt_recp_title);
            txtRecpSummary = itemView.findViewById(R.id.txt_recp_summary);
        }
    }
}
