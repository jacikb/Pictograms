package jacek.brzezinski.piktogramy;
//SQLite Database Tutorial for Android Studio 1,13m
//https://www.youtube.com/watch?v=hDSVInZ2JCs

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String P_TABLE = "PICTOGRAM_TABLE";
    public static final String P_COLUMN_ID = "ID";
    public static final String P_COLUMN_NAME = "NAME";
    public static final String P_COLUMN_PATH = "PATH";
    public static final String P_COLUMN_POSITION = "POSITION";
    public static final String P_COLUMN_ACTIVE = "ACTIVE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "pictogram.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + P_TABLE + " (" + P_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + P_COLUMN_NAME + " TEXT, " + P_COLUMN_PATH + " TEXT, " + P_COLUMN_POSITION + " INTEGER,  " + P_COLUMN_ACTIVE + " BOOL)";
        db.execSQL(createTableStatement);

        PictogramModel pictogramModel = new PictogramModel();
        pictogramModel.createNew("Tak", "p_tak", 1);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Nie", "p_nie", 2);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Koniec", "p_koniec", 3);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Toaleta", "p_toaleta", 4);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Pić", "p_pic", 5);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Jeść", "p_jesc", 6);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Kanapka", "p_kanapka", 7);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Owoce", "p_owoce", 8);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Czekolada", "p_czekolada", 9);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Jabłko", "p_jablko", 10);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Komputer", "p_komputer", 11);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Jeszcze", "p_jeszcze", 12);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Kąpać", "p_kapac", 13);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Spacer", "p_spacer", 14);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Zimno", "p_zimno", 15);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Odpocząć", "p_odpoczac", 16);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Spać", "p_spac", 17);
        addOneOnCreate(db, pictogramModel);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createTableStatement = "DROP TABLE IF EXISTS " + P_TABLE;
        db.execSQL(createTableStatement);
        onCreate(db);
    }

    public boolean addOneOnCreate(SQLiteDatabase db, PictogramModel pictogramModel) {
        ContentValues cv = new ContentValues();
        cv.put(P_COLUMN_NAME, pictogramModel.getName());
        cv.put(P_COLUMN_PATH, pictogramModel.getPath());
        cv.put(P_COLUMN_POSITION, pictogramModel.getPosition());
        cv.put(P_COLUMN_ACTIVE, pictogramModel.isActive());

        long insert = db.insert(P_TABLE, null, cv);

        if (insert == -1) {
            return true;
        } else {
            return true;
        }
    }

    public boolean addOne(PictogramModel pictogramModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(P_COLUMN_NAME, pictogramModel.getName());
        cv.put(P_COLUMN_PATH, pictogramModel.getPath());
        cv.put(P_COLUMN_POSITION, pictogramModel.getPosition());
        cv.put(P_COLUMN_ACTIVE, pictogramModel.isActive());

        long insert = db.insert(P_TABLE, null, cv);

        if (insert == -1) {
            return true;
        } else {
            return true;
        }
    }

    public boolean updateOne(PictogramModel pictogramModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = pictogramModel.getId();
        ContentValues cv = new ContentValues();
        cv.put(P_COLUMN_NAME, pictogramModel.getName());
        cv.put(P_COLUMN_PATH, pictogramModel.getPath());
        cv.put(P_COLUMN_POSITION, pictogramModel.getPosition());
        cv.put(P_COLUMN_ACTIVE, pictogramModel.isActive());

        long update = db.update(P_TABLE, cv, P_COLUMN_ID + "=" + Integer.toString(id), null);
        return update == 1;
    }

    public boolean deleteOne(PictogramModel pictogramModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String stringQuery = "DELETE FROM " + P_TABLE + " WHERE " + P_COLUMN_ID + "=" + pictogramModel.getId();
        Cursor cursor = db.rawQuery(stringQuery, null);
        return cursor.moveToFirst();
    }

    public List<PictogramModel> getAll(boolean onlyActive) {
        List<PictogramModel> returnList = new ArrayList<>();
        //get data from database
        String queryString = "SELECT * FROM " + P_TABLE + (onlyActive ? " WHERE " + P_COLUMN_ACTIVE + "=1" : "") + " ORDER BY " + P_COLUMN_POSITION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        // is result
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String code = cursor.getString(2);
                int position = cursor.getInt(3);
                boolean active = cursor.getInt(4) == 1;
                PictogramModel pictogramModel = new PictogramModel(id, name, code, position, active);
                returnList.add(pictogramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // db.close();
        return returnList;
    }
}