package com.smartpantry.manager.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.R;
import com.smartpantry.manager.fragment.PrefCfgFrag;
import com.smartpantry.manager.logic.UnitConvertLogic;
import com.smartpantry.manager.model.AlmostRecpItem;
import com.smartpantry.manager.model.RecpEntity;

import java.util.List;

// adapter for displaying almost there recipes that are missing one ingredient
public class AlmostRecpAdapt extends RecyclerView.Adapter<AlmostRecpAdapt.AlmostHolder> {

    private List<AlmostRecpItem> almostDataLst;

    public AlmostRecpAdapt(List<AlmostRecpItem> almostDataLst) {
        this.almostDataLst = almostDataLst;
    }

    @NonNull
    @Override
    public AlmostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardVw = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_almost_recp_card, parent, false);
        return new AlmostHolder(cardVw);
    }

    @Override
    public void onBindViewHolder(@NonNull AlmostHolder holder, int position) {
        AlmostRecpItem item = almostDataLst.get(position);
        RecpEntity recp = item.getRecp();

        holder.txtAlmostTitle.setText(recp.getRecpNm());
        holder.txtAlmostSummary.setText(recp.getRecpDesc());

        // formats missing quantity according to unit preferences
        Context ctx = holder.itemView.getContext();
        SharedPreferences sp = ctx.getSharedPreferences(PrefCfgFrag.PREF_STORAGE_TAG, Context.MODE_PRIVATE);
        boolean isImperial = "Imperial".equalsIgnoreCase(sp.getString(PrefCfgFrag.KEY_UNIT_SYS, "Metric"));
        String dispQty = UnitConvertLogic.formatDisplayQtyAndUnit(item.getMissingQty(), item.getMissingUnit(), isImperial);

        String badgeText = String.format(ctx.getString(R.string.missing_badge_prefix), item.getMissingIngName(), dispQty);
        holder.txtAlmostMissingBadge.setText(badgeText);
    }

    @Override
    public int getItemCount() {
        return almostDataLst == null ? 0 : almostDataLst.size();
    }

    // handles refreshing almost there recipe list data
    public void refreshAlmostData(List<AlmostRecpItem> freshData) {
        this.almostDataLst = freshData;
        notifyDataSetChanged();
    }

    // view holder caching views for almost there recipe cards
    public static class AlmostHolder extends RecyclerView.ViewHolder {
        public TextView txtAlmostTitle;
        public TextView txtAlmostSummary;
        public TextView txtAlmostMissingBadge;

        public AlmostHolder(@NonNull View itemView) {
            super(itemView);
            txtAlmostTitle = itemView.findViewById(R.id.txt_almost_title);
            txtAlmostSummary = itemView.findViewById(R.id.txt_almost_summary);
            txtAlmostMissingBadge = itemView.findViewById(R.id.txt_almost_missing_badge);
        }
    }
}
