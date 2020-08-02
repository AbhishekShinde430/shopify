package com.example.abhishek.shopify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;


public class DatabaseOperations extends SQLiteOpenHelper {

    public static final int database_version =2;
    public String CREATE_QUER=" CREATE TABLE "+ TableData.TABLE_NAME+"("+ TableData.TableInfo.NAME+" TEXT,"
            + TableData.TableInfo.QTY+ " TEXT,"
            + TableData.TableInfo.CHECK+" TEXT,"
            + TableData.TableInfo.MEASURE+" INTEGER,"+
            TableData.TableInfo.VAL+" TEXT );";

    public DatabaseOperations( Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);
        Log.d("Database Operations ","Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_QUER);
        Log.d("Database Operations ","Table Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(DatabaseOperations dop){
        SQLiteDatabase sq=dop.getWritableDatabase();
        sq.execSQL(CREATE_QUER);
        Log.d("Database Operations ","Table Created");
    }

    public void putInfo(DatabaseOperations dop,String name,String qty,String val,Integer index){


        SQLiteDatabase Sq=dop.getReadableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(TableData.TableInfo.NAME,name);
        cv.put(TableData.TableInfo.QTY,qty);
        cv.put(TableData.TableInfo.CHECK,"z");//z means nchecked and a means checked.
        cv.put(TableData.TableInfo.VAL,val);
        cv.put(TableData.TableInfo.MEASURE,index);

       long k= Sq.insert(TableData.TABLE_NAME,null,cv);

        Log.d("Database Operations ","Row Inserted");

    }


    public Cursor getInfo(DatabaseOperations dp){

        SQLiteDatabase sq=dp.getReadableDatabase();
        String[]colmns={TableData.TableInfo.NAME, TableData.TableInfo.QTY, TableData.TableInfo.VAL,TableData.TableInfo.CHECK, TableData.TableInfo.MEASURE};

        Cursor cr =sq.query(TableData.TABLE_NAME,colmns,null,null,null ,null, TableData.TableInfo.CHECK +" ASC");

        return cr;
    }

    public void updateUserInfo(DatabaseOperations dop,String name,String Qty,String val,String index,String new_name,String
                               new_Qty,String new_Val,String new_index)
    {
        SQLiteDatabase sq=dop.getWritableDatabase();
        String selection = TableData.TableInfo.NAME+
                " LIKE ? AND "+ TableData.TableInfo.QTY+
                " LIKE ? AND "+ TableData.TableInfo.VAL+
                " LIKE ? AND "+ TableData.TableInfo.MEASURE+
                " LIKE ?";

        String args[] = {name,Qty,val,index};
        ContentValues values=new ContentValues();
        values.put(TableData.TableInfo.NAME,new_name);
        values.put(TableData.TableInfo.QTY,new_Qty);
        values.put(TableData.TableInfo.VAL,new_Val);
        values.put(TableData.TableInfo.MEASURE,new_index);

        sq.update(TableData.TABLE_NAME,values,selection,args);
    }


    public void updateState(DatabaseOperations dop,String name,String State,String
            new_State)
    {
        SQLiteDatabase sq=dop.getWritableDatabase();
        String selection = TableData.TableInfo.NAME+
                " LIKE ? AND "+ TableData.TableInfo.CHECK+
                " LIKE ?";

        String args[] = {name,State};
        ContentValues values=new ContentValues();
        values.put(TableData.TableInfo.CHECK,new_State);


        sq.update(TableData.TABLE_NAME,values,selection,args);
    }

    public void deleteProd(DatabaseOperations dop,String name, String Qty, String val,String measure){
        String selection = TableData.TableInfo.NAME+
                " LIKE ? AND "+ TableData.TableInfo.QTY+
                " LIKE ? AND "+ TableData.TableInfo.VAL+
                " LIKE ? AND "+ TableData.TableInfo.MEASURE+
                " LIKE ?";
        String args[] = {name,Qty,val,measure};

        SQLiteDatabase sq= dop.getWritableDatabase();
        sq.delete(TableData.TABLE_NAME,selection,args);
    }

    public void delete() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting rows
        sqLiteDatabase.delete(TableData.TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }
}
