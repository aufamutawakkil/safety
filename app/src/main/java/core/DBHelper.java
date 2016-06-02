package core;

/**
 * Created by root on 9/25/15.
 */
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "safety.db";
        private HashMap hp;

        public DBHelper(Context context)
        {
            super(context, DATABASE_NAME , null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(
                    "create table login (id int, username varchar, password varchar);" +
                    "create table setting (no_hardware varchar,command_reset varchar);" +
                    "create table location (id int, lng varchar,lat varchar, tgl datetime default current_timestamp);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            //db.execSQL("DROP TABLE IF EXISTS login");
            //onCreate(db);
        }

        public boolean insert (String tableName,HashMap<String,String> data)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            Iterator it = data.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                contentValues.put( pair.getKey().toString(),pair.getValue().toString() );
            }
            db.insert(tableName, null, contentValues);
            return true;
        }

        public Cursor getData(String tableName,int id){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from " + tableName + " where id="+id+"", null );
            //Cursor res =  db.rawQuery(sql, null );
            return res;
        }

    public Cursor query(String sql){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(sql, null );
        return res;
    }

        public int numberOfRows(String tableName){
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.queryNumEntries(db,tableName);
            return numRows;
        }

        public boolean update (String tableName,Integer id, HashMap<String,String> data)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            Iterator it = data.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry) it.next();
                contentValues.put( pair.getKey().toString(),pair.getValue().toString() );
            }
            db.update(tableName, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
            return true;
        }

        public Integer delete (String tableName,Integer id)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(tableName,
                    "id = ? ",
                    new String[] { Integer.toString(id) });
        }

       /* public ArrayList<String> getAllLogin()
        {
            ArrayList<String> array_list = new ArrayList<String>();

            //hp = new HashMap();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from login", null );
            res.moveToFirst();

            while(res.isAfterLast() == false){
                array_list.add(res.getString(res.getColumnIndex(COLUMN_USERNAME)));
                array_list.add(res.getString(res.getColumnIndex(COLUMN_PASSWORD)));
                res.moveToNext();
            }
            return array_list;
        }*/

        private static boolean isDBExits(ContextWrapper context, String dbName) {
            File dbFile = context.getDatabasePath(dbName);
            return dbFile.exists();
        }
}
