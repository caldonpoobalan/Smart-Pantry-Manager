package com.smartpantry.manager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartpantry.manager.R;
import com.smartpantry.manager.adapter.StockItemAdapt;
import com.smartpantry.manager.database.PantryRoomDb;
import com.smartpantry.manager.model.StockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

// fragment to view and manage pantry stock items
public class StockVwFrag extends Fragment implements StockItemAdapt.OnStockActListener {

    private RecyclerView recStockLst;
    private TextView txtEmptyNotice;
    private FloatingActionButton fabAddStock;
    private StockItemAdapt stkAdapt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragVw = inflater.inflate(R.layout.frag_stock_vw, container, false);

        recStockLst = fragVw.findViewById(R.id.rec_stock_lst);
        txtEmptyNotice = fragVw.findViewById(R.id.txt_empty_notice);
        fabAddStock = fragVw.findViewById(R.id.fab_add_stock);

        recStockLst.setLayoutManager(new LinearLayoutManager(getContext()));
        stkAdapt = new StockItemAdapt(new ArrayList<>(), this);
        recStockLst.setAdapter(stkAdapt);

        // placeholder until entry form activity is built
        fabAddStock.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Opening entry form...", Toast.LENGTH_SHORT).show();
        });

        return fragVw;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStockInventory();
    }

    // load items from room database in background
    public void loadStockInventory() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<StockEntity> freshStock = PantryRoomDb.getDbInst(getContext()).stockDataAcc().getAllStock();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    stkAdapt.refreshStockData(freshStock);
                    if (freshStock.isEmpty()) {
                        txtEmptyNotice.setVisibility(View.VISIBLE);
                        recStockLst.setVisibility(View.GONE);
                    } else {
                        txtEmptyNotice.setVisibility(View.GONE);
                        recStockLst.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @Override
    public void onStockClick(StockEntity itm) {
        Toast.makeText(getContext(), "Selected: " + itm.getItmNm(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStockLongClick(StockEntity itm, int pos) {
        promptStockDelete(itm);
    }

    @Override
    public void onStockDelClick(StockEntity itm, int pos) {
        promptStockDelete(itm);
    }

    // confirm before deleting an ingredient
    private void promptStockDelete(StockEntity delTarget) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.confirm_delete_title)
                .setMessage(getString(R.string.confirm_delete_message) + " (" + delTarget.getItmNm() + ")")
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        PantryRoomDb.getDbInst(getContext()).stockDataAcc().deleteStock(delTarget);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(this::loadStockInventory);
                        }
                    });
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
