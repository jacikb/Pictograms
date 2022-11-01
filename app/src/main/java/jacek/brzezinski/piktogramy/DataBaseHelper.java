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
    public static final String P_COLUMN_RESOURCE = "RESOURCE";
    public static final String P_COLUMN_POSITION = "POSITION";
    public static final String P_COLUMN_ACTIVE = "ACTIVE";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "pictogram.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + P_TABLE + " (" + P_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + P_COLUMN_NAME + " TEXT, " + P_COLUMN_PATH + " TEXT, " + P_COLUMN_RESOURCE + " BOOL, " + P_COLUMN_POSITION + " INTEGER,  " + P_COLUMN_ACTIVE + " BOOL)";
        db.execSQL(createTableStatement);

        PictogramModel pictogramModel = new PictogramModel();
        pictogramModel.createNew("Tak", "p_tak", true, 1);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Nie", "p_nie", true, 2);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Koniec", "p_koniec", true, 3);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Toaleta", "p_toaleta", true, 4);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Pić", "p_pic", true, 5);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Jeść", "p_jesc", true, 6);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Kanapka", "p_kanapka", true, 7);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Owoce", "p_owoce", true, 8);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Czekolada", "p_czekolada", true, 9);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Jabłko", "p_jablko", true, 10);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Komputer", "p_komputer", true, 11);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Jeszcze", "p_jeszcze", true, 12);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Kąpać", "p_kapac", true, 13);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Spacer", "p_spacer", true, 14);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Zimno", "p_zimno", true, 15);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Odpocząć", "p_odpoczac", true, 16);
        addOneOnCreate(db, pictogramModel);
        pictogramModel.createNew("Spać", "p_spac", true, 17);
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
        cv.put(P_COLUMN_RESOURCE, pictogramModel.isResource() ? 1 : 0);
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
        cv.put(P_COLUMN_RESOURCE, pictogramModel.isResource());
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
        cv.put(P_COLUMN_RESOURCE, pictogramModel.isResource());
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
                String path = cursor.getString(2);
                Boolean resource = cursor.getInt(3) == 1;
                int position = cursor.getInt(4);
                Boolean active = cursor.getInt(5) == 1;
                PictogramModel pictogramModel = new PictogramModel(id, name, path, resource, position, active);
                returnList.add(pictogramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // db.close();
        return returnList;
    }
}