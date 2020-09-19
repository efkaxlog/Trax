package com.bajera.xlog.trax.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.bajera.xlog.trax.data.db.model.Converters;
import com.bajera.xlog.trax.data.db.model.Item;
import com.bajera.xlog.trax.data.db.model.ItemDao;
import com.bajera.xlog.trax.data.db.model.Record;

@Database(entities = {Record.class, Item.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    AppDatabase.class, "AppDatabase")
                    .allowMainThreadQueries()
                    .addMigrations(Migrations.MIGRATION_1_2)
                    .build();
        }
        return instance;
    }

    public abstract ItemDao itemDao();
}
