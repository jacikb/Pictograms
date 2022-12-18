package jacek.brzezinski.piktogramy;
//SQLite Database Tutorial for Android Studio 1,13m
//https://www.youtube.com/watch?v=hDSVInZ2JCs

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static final String P_TABLE = "PICTOGRAM_TABLE";
    public static final String P_COLUMN_ID = "ID";
    public static final String P_COLUMN_NAME = "NAME";
    public static final String P_COLUMN_PATH = "PATH";
    public static final String P_COLUMN_RESOURCE = "RESOURCE";
    public static final String P_COLUMN_POSITION = "POSITION";
    public static final String P_COLUMN_ACTIVE = "ACTIVE";
    public static final String P_COLUMN_PARENT = "PARENT";
    public static final String P_COLUMN_ROLE = "ROLE";

    //4 - add parent, role
    public DataBaseHelper(@Nullable Context context) {
        super(context, "pictogram.db", null, 4);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + P_TABLE + " ("
                + P_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + P_COLUMN_NAME + " TEXT, "
                + P_COLUMN_PATH + " TEXT, "
                + P_COLUMN_RESOURCE + " BOOL, "
                + P_COLUMN_POSITION + " INTEGER, "
                + P_COLUMN_ACTIVE + " BOOL, "
                + P_COLUMN_PARENT + " INTEGER, "
                + P_COLUMN_ROLE + " INTEGER "
                + ")";
        db.execSQL(createTableStatement);

        int position = 1;

        addOneOnCreate(db, "root", 0, "Tak", "p_tak", false, position++);
        addOneOnCreate(db, "root", 0, "Nie", "p_nie", false, position++);
        addOneOnCreate(db, "root", 0, "Proszę", "p_prosze", false, position++);
        addOneOnCreate(db, "root", 0, "Ja chcę", "p_ja_chce", false, position++);
        addOneOnCreate(db, "root", 0, "Nie chcę!", "p_nie_chce", false, position++);
        addOneOnCreate(db, "root", 0, "Koniec", "p_koniec", false, position++);
        addOneOnCreate(db, "root", 0, "Toaleta", "p_toaleta", false, position++);
        addOneOnCreate(db, "root", PictogramModel.ROLE_DIR, "Jedzenie", "d_jedzenie", false, position++);
        addOneOnCreate(db, "d_jedzenie", 0, "Jeść", "p_jesc", false, position++);
        addOneOnCreate(db, "d_jedzenie", 0, "Pić", "p_pic", false, position++);
        addOneOnCreate(db, "d_jedzenie", 0, "Kanapka", "p_kanapka", false, position++);
        addOneOnCreate(db, "d_jedzenie", 0, "Śniadanie", "p_sniadanie", false, position++);
        addOneOnCreate(db, "d_jedzenie", 0, "Obiad", "p_obiad", false, position++);
        addOneOnCreate(db, "d_jedzenie", 0, "Owoce", "p_owoce", false, position++);
        addOneOnCreate(db, "d_jedzenie", 0, "Czekolada", "p_czekolada", false, position++);
        addOneOnCreate(db, "d_jedzenie", 0, "Jabłko", "p_jablko", false, position++);
        addOneOnCreate(db, "root", 0, "Komputer", "p_komputer", false, position++);
        addOneOnCreate(db, "root", 0, "Jeszcze", "p_jeszcze", false, position++);
        addOneOnCreate(db, "root", 0, "Dom", "p_dom", false, position++);
        addOneOnCreate(db, "root", 0, "Plecak", "p_plecak", false, position++);
        addOneOnCreate(db, "root", 0, "Spacer", "p_spacer", false, position++);
        addOneOnCreate(db, "root", 0, "Zimno", "p_zimno", false, position++);
        addOneOnCreate(db, "root", 0, "Odpocząć", "p_odpoczac", false, position++);
        addOneOnCreate(db, "root", 0, "Kąpać", "p_kapac", false, position++);
        addOneOnCreate(db, "root", 0, "Spać", "p_spac", false, position++);


        FileHelper fileHelper = new FileHelper(this.context);
        List<PictogramModel> pictograms = getAll(false);
        for (int i = 0; i < pictograms.size(); i++) {
            String path = pictograms.get(i).getPath();
            try {
                fileHelper.copyResourceToLocal(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean addOneOnCreate(SQLiteDatabase db, String parentKey, int role, String name, String path, boolean resource, int position) {
        Log.w("addOneOnCreate", name);
        int parent;
        if (parentKey.equals("root") || parentKey.isEmpty()) {
            parent = PictogramModel.TREE_ROOT;
        } else {
            parent = this.findIdByPath(db, parentKey);
        }
        ContentValues cv = new ContentValues();
        cv.put(P_COLUMN_NAME, name);
        cv.put(P_COLUMN_PATH, path);
        cv.put(P_COLUMN_RESOURCE, resource ? 1 : 0);
        cv.put(P_COLUMN_POSITION, position);
        cv.put(P_COLUMN_ACTIVE, 1);
        cv.put(P_COLUMN_PARENT, parent);
        cv.put(P_COLUMN_ROLE, role);

        long insert = db.insert(P_TABLE, null, cv);

        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createTableStatement = "DROP TABLE IF EXISTS " + P_TABLE;
        db.execSQL(createTableStatement);
        onCreate(db);
    }

    public void reCreate() {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1, 4);
    }

    public boolean addOneOnCreateNie(SQLiteDatabase db, PictogramModel pictogramModel) {
        ContentValues cv = new ContentValues();
        cv.put(P_COLUMN_NAME, pictogramModel.getName());
        cv.put(P_COLUMN_PATH, pictogramModel.getPath());
        cv.put(P_COLUMN_RESOURCE, pictogramModel.isResource() ? 1 : 0);
        cv.put(P_COLUMN_POSITION, pictogramModel.getPosition());
        cv.put(P_COLUMN_ACTIVE, pictogramModel.isActive());
        cv.put(P_COLUMN_PARENT, pictogramModel.getParent());
        cv.put(P_COLUMN_ROLE, pictogramModel.getRole());

        long insert = db.insert(P_TABLE, null, cv);

        if (insert == -1) {
            return true;
        } else {
            return true;
        }
    }

    public PictogramModel getById(int id) {
        PictogramModel pictogramModel = new PictogramModel();
        String queryString = "SELECT * FROM " + P_TABLE + " WHERE " + P_COLUMN_ID + "=" + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        // is result
        if (cursor.moveToFirst()) {
            pictogramModel.setId(cursor.getInt(0));
            pictogramModel.setName(cursor.getString(1));
            pictogramModel.setPath(cursor.getString(2));
            pictogramModel.setResource(cursor.getInt(3) == 1);
            pictogramModel.setPosition(cursor.getInt(4));
            pictogramModel.setActive(cursor.getInt(5) == 1);
            pictogramModel.setParent(cursor.getInt(6));
            pictogramModel.setRole(cursor.getInt(7));
        }
        cursor.close();
        db.close();
        return pictogramModel;
    }

    public boolean addOne(PictogramModel pictogramModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(P_COLUMN_NAME, pictogramModel.getName());
        cv.put(P_COLUMN_PATH, pictogramModel.getPath());
        cv.put(P_COLUMN_RESOURCE, pictogramModel.isResource());
        cv.put(P_COLUMN_POSITION, pictogramModel.getPosition());
        cv.put(P_COLUMN_ACTIVE, pictogramModel.isActive());
        cv.put(P_COLUMN_PARENT, pictogramModel.getParent());
        cv.put(P_COLUMN_ROLE, pictogramModel.getRole());

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
        cv.put(P_COLUMN_PARENT, pictogramModel.getParent());
        cv.put(P_COLUMN_ROLE, pictogramModel.getRole());

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

    public int getMaxPosition() {
        //get data from database
        int position = 0;
        String queryString = "SELECT max(position) as position FROM " + P_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        // is result
        if (cursor.moveToFirst()) {
            position = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return position;
    }

    public int findIdByPath(SQLiteDatabase db, String path) {
        //get data from database
        int id = 0;
        String queryString = "SELECT id FROM " + P_TABLE + " WHERE " + P_COLUMN_PATH + "='" + path + "'";
        Log.w("findIdByPath", queryString);
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        // is result
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        //db.close();
        return id;
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
                int parent = cursor.getInt(6);
                int role = cursor.getInt(7);
                PictogramModel pictogramModel = new PictogramModel(id, parent, role, name, path, resource, position, active);
                returnList.add(pictogramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }

    public List<PictogramModel> getTree(int byParent) {
        List<PictogramModel> returnList = new ArrayList<>();
        //get data from database
        String queryString = "SELECT * FROM " + P_TABLE + " WHERE " + P_COLUMN_PARENT + "=" + byParent + " AND " + P_COLUMN_ACTIVE + "=1 ORDER BY " + P_COLUMN_POSITION;
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
                int parent = cursor.getInt(6);
                int role = cursor.getInt(7);
                PictogramModel pictogramModel = new PictogramModel(id, parent, role, name, path, resource, position, active);
                returnList.add(pictogramModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }
}