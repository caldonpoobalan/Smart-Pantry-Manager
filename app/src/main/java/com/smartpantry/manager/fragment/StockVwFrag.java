package com.smartpantry.manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smartpantry.manager.R;
import com.smartpantry.manager.activity.IngredFormAct;
import com.smartpantry.manager.adapter.StockItemAdapt;
import com.smartpantry.manager.database.PantryRoomDb;
import com.smartpantry.manager.model.StockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

// fragment to view and manage pantry stock items
public class StockVwFrag extends Fragment implements StockItemAdapt.OnStockActListener {

    private RecyclerView recStockLst;
    private View layEmptyNotice;
    private FloatingActionButton fabAddStock;
    private EditText edtSearchStock;
    private StockItemAdapt stkAdapt;
    private List<StockEntity> fullStockLst = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragVw = inflater.inflate(R.layout.frag_stock_vw, container, false);

        recStockLst = fragVw.findViewById(R.id.rec_stock_lst);
        layEmptyNotice = fragVw.findViewById(R.id.lay_empty_notice);
        fabAddStock = fragVw.findViewById(R.id.fab_add_stock);
        edtSearchStock = fragVw.findViewById(R.id.edt_search_stock);

        recStockLst.setLayoutManager(new LinearLayoutManager(getContext()));
        stkAdapt = new StockItemAdapt(new ArrayList<>(), this);
        recStockLst.setAdapter(stkAdapt);

        // handles real-time pantry search filtering
        edtSearchStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                filterPantryItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // tap the + button to add a new ingredient
        fabAddStock.setOnClickListener(v -> {
            Intent openAddInt = new Intent(getContext(), IngredFormAct.class);
            startActivity(openAddInt);
        });

        return fragVw;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStockInventory();
    }

    // handles loading items from room database in background
    public void loadStockInventory() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<StockEntity> freshStock = PantryRoomDb.getDbInst(getContext()).stockDataAcc().getAllStock();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    fullStockLst = freshStock;
                    String curQuery = edtSearchStock != null ? edtSearchStock.getText().toString() : "";
                    if (!TextUtils.isEmpty(curQuery)) {
                        filterPantryItems(curQuery);
                    } else {
                        stkAdapt.refreshStockData(freshStock);
                        if (freshStock.isEmpty()) {
                            layEmptyNotice.setVisibility(View.VISIBLE);
                            recStockLst.setVisibility(View.GONE);
                        } else {
                            layEmptyNotice.setVisibility(View.GONE);
                            recStockLst.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    // filters pantry items based on search query text
    private void filterPantryItems(String query) {
        if (TextUtils.isEmpty(query)) {
            stkAdapt.refreshStockData(fullStockLst);
            if (fullStockLst.isEmpty()) {
                layEmptyNotice.setVisibility(View.VISIBLE);
                recStockLst.setVisibility(View.GONE);
            } else {
                layEmptyNotice.setVisibility(View.GONE);
                recStockLst.setVisibility(View.VISIBLE);
            }
            return;
        }

        String clnQuery = query.trim().toLowerCase();
        List<StockEntity> filtered = new ArrayList<>();
        for (StockEntity item : fullStockLst) {
            if (item.getItmNm() != null && item.getItmNm().toLowerCase().contains(clnQuery)) {
                filtered.add(item);
            }
        }

        stkAdapt.refreshStockData(filtered);
        if (filtered.isEmpty()) {
            recStockLst.setVisibility(View.GONE);
        } else {
            recStockLst.setVisibility(View.VISIBLE);
            layEmptyNotice.setVisibility(View.GONE);
        }
    }

    // tap the item card to edit ingredient details
    @Override
    public void onStockClick(StockEntity itm) {
        Intent openEditInt = new Intent(getContext(), IngredFormAct.class);
        openEditInt.putExtra(IngredFormAct.KEY_ROW_ID, itm.getRowId());
        startActivity(openEditInt);
    }

    @Override
    public void onStockLongClick(StockEntity itm, int pos) {
        promptStockDelete(itm);
    }

    @Override
    public void onStockDelClick(StockEntity itm, int pos) {
        promptStockDelete(itm);
    }

    // handles confirming before deleting an ingredient
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
