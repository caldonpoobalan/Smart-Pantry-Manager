package com.smartpantry.manager.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import com.smartpantry.manager.dao.RecpDataAcc;
import com.smartpantry.manager.dao.RecpIngredDataAcc;
import com.smartpantry.manager.dao.StockDataAcc;
import com.smartpantry.manager.model.RecpEntity;
import com.smartpantry.manager.model.RecpIngredEntity;
import com.smartpantry.manager.model.StockEntity;

// main room database setup
@Database(entities = {StockEntity.class, RecpEntity.class, RecpIngredEntity.class}, version = 1, exportSchema = false)
public abstract class PantryRoomDb extends RoomDatabase {

    public abstract StockDataAcc stockDataAcc();
    public abstract RecpDataAcc recpDataAcc();
    public abstract RecpIngredDataAcc recpIngredDataAcc();

    // singleton instance
    private static volatile PantryRoomDb dbInst;

    // gets db instance
    public static PantryRoomDb getDbInst(Context appCtx) {
        if (dbInst == null) {
            synchronized (PantryRoomDb.class) {
                if (dbInst == null) {
                    dbInst = Room.databaseBuilder(appCtx.getApplicationContext(),
                            PantryRoomDb.class, "smart_pantry_db")
                            .addCallback(new SeedDbCallback(appCtx))
                            .build();
                }
            }
        }
        return dbInst;
    }

    // callback to populate starter recipes when database is first created
    private static class SeedDbCallback extends RoomDatabase.Callback {
        private final Context callCtx;

        SeedDbCallback(Context callCtx) {
            this.callCtx = callCtx;
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase sdb) {
            super.onCreate(sdb);
            Executors.newSingleThreadExecutor().execute(() -> {
                RecpSeedData.seedDefaultRecp(PantryRoomDb.getDbInst(callCtx));
            });
        }
    }
}
