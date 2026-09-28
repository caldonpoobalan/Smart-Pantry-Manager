package com.smartpantry.manager.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.manager.R;
import com.smartpantry.manager.logic.UnitConvertLogic;
import com.smartpantry.manager.model.StockEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// adapter to display pantry items inside recyclerview
public class StockItemAdapt extends RecyclerView.Adapter<StockItemAdapt.StockHolder> {

    // list of stock items
    private List<StockEntity> stkItmLst;

    // action click listener
    private OnStockActListener actClickListnr;

    // 5 days warning limit in milliseconds
    private static final long EXP_WARN_LIMIT_MS = 5L * 24 * 60 * 60 * 1000;

    // listener interface for user interaction callbacks
    public interface OnStockActListener {
        void onStockClick(StockEntity itm);
        void onStockLongClick(StockEntity itm, int pos);
        void onStockDelClick(StockEntity itm, int pos);
    }

    public StockItemAdapt(List<StockEntity> stkItmLst, OnStockActListener actClickListnr) {
        this.stkItmLst = stkItmLst;
        this.actClickListnr = actClickListnr;
    }

    @NonNull
    @Override
    public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowVw = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_stock_item, parent, false);
        return new StockHolder(rowVw);
    }

    @Override
    public void onBindViewHolder(@NonNull StockHolder holder, int position) {
        StockEntity stkRowItm = stkItmLst.get(position);
        holder.txtItmNm.setText(stkRowItm.getItmNm());

        String untStr = stkRowItm.getUntLbl() != null ? stkRowItm.getUntLbl() : "";
        holder.txtQtyVal.setText(UnitConvertLogic.formatQty(stkRowItm.getQtyVal()) + " " + untStr);

        // check expiry date and set warning colors
        if (stkRowItm.getExpDateMs() == null) {
            holder.txtExpWarn.setText("No expiry");
            holder.txtExpWarn.setTextColor(Color.GRAY);
        } else {
            SimpleDateFormat sdfFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.txtExpWarn.setText(sdfFormat.format(new Date(stkRowItm.getExpDateMs())));

            long timeDiffMs = stkRowItm.getExpDateMs() - System.currentTimeMillis();
            if (timeDiffMs < 0) {
                // already expired (red)
                holder.txtExpWarn.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.error));
            } else if (timeDiffMs <= EXP_WARN_LIMIT_MS) {
                // expiring within 5 days (orange)
                holder.txtExpWarn.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.expiry_warning));
            } else {
                // fresh or more than 5 days left (gray)
                holder.txtExpWarn.setTextColor(Color.GRAY);
            }
        }

        // handle row click
        holder.itemView.setOnClickListener(v -> {
            if (actClickListnr != null) {
                actClickListnr.onStockClick(stkRowItm);
            }
        });

        // handle long click to delete
        holder.itemView.setOnLongClickListener(v -> {
            if (actClickListnr != null) {
                actClickListnr.onStockLongClick(stkRowItm, position);
                return true;
            }
            return false;
        });

        // handle trash icon click
        holder.btnTrashDel.setOnClickListener(v -> {
            if (actClickListnr != null) {
                actClickListnr.onStockDelClick(stkRowItm, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stkItmLst == null ? 0 : stkItmLst.size();
    }

    // reload adapter with updated data
    public void refreshStockData(List<StockEntity> freshItems) {
        this.stkItmLst = freshItems;
        notifyDataSetChanged();
    }

    // view holder caching views for each row
    public static class StockHolder extends RecyclerView.ViewHolder {
        public TextView txtItmNm;
        public TextView txtQtyVal;
        public TextView txtExpWarn;
        public ImageView btnTrashDel;

        public StockHolder(View itemView) {
            super(itemView);
            txtItmNm = itemView.findViewById(R.id.txt_itm_nm);
            txtQtyVal = itemView.findViewById(R.id.txt_qty_val);
            txtExpWarn = itemView.findViewById(R.id.txt_exp_warn);
            btnTrashDel = itemView.findViewById(R.id.btn_trash_del);
        }
    }
}
