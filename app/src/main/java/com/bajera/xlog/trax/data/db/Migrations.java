package com.bajera.xlog.trax.data.db;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

class Migrations {
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // SQL 3.25 is not supported?
            // So instead of "ALTER TABLE table RENAME COLUMN column_old TO column_new, all this sql below.
            database.execSQL("CREATE TABLE records"
                    + " (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                    + " value REAL NOT NULL,"
                    + " date TEXT NOT NULL,"
                    + " itemId INTEGER NOT NULL)");
            database.execSQL("INSERT INTO records(id, value, date, itemId) SELECT id, value, date, itemInfoId FROM item");

            database.execSQL("CREATE TABLE items"
                    + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + " name TEXT,"
                    + " tracking_type TEXT,"
                    + " goal_type TEXT,"
                    + " goal_time TEXT,"
                    + " goal_value REAL NOT NULL)");
            database.execSQL("INSERT INTO items(id, name, tracking_type, goal_type, goal_time, goal_value)" +
                    " SELECT id, name, tracking_type, goal_type, goal_time, goal_value FROM item_info");

            database.execSQL("DROP TABLE item");
            database.execSQL("DROP TABLE item_info");
        }
    };
}
