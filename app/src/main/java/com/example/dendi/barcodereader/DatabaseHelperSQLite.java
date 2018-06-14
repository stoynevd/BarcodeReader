package com.example.dendi.barcodereader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperSQLite extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "barcodesManager";

    private static final String TABLE_BARCODES = "barcodes";

    private static final String KEY_ID = "barcodeNumber";

    private static final String KEY_QUANTITY = "quantity";


    public DatabaseHelperSQLite(Context context){

        super(context,DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BARCODES_TABLE = "CREATE TABLE " + TABLE_BARCODES + "( "
                + KEY_ID + "TEXT PRIMARY KEY, " + KEY_QUANTITY + "NUMBER " + ") ";

        db.execSQL(CREATE_BARCODES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARCODES);

        onCreate(db);
    }


    public void addBarcode(Barcode barcode){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("KEY_ID", barcode.getBarcode());

        values.put("KEY_QUANTITY", barcode.getQuantity());

        db.insert(TABLE_BARCODES, null, values);

        db.close();

    }

    public Barcode getBarcode(String barcode){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BARCODES, new String[]{KEY_ID, KEY_QUANTITY}, KEY_ID + "=?",
                new String[]{barcode}, null, null, KEY_ID, null);

        if (cursor != null){

            cursor.moveToFirst();

        }

        Barcode b = new Barcode(cursor.getString(0), Integer.parseInt(cursor.getString(1)));

        return b;

    }

    public List<Barcode> getAllBarcodes(){

        List<Barcode> barcodes = new ArrayList<Barcode>();

        String selectQuery  = "SELECT * FROM  " + TABLE_BARCODES;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){

            do {

                Barcode barcode = new Barcode(cursor.getString(0), Integer.parseInt(cursor.getString(1)));

                barcode.setBarcode(cursor.getString(0));

                barcode.setQuantity(Integer.parseInt(cursor.getString(1)));

                barcodes.add(barcode);
            }while (cursor.moveToNext());

        }

        return barcodes;
    }


    public int updateBarcode(Barcode barcode){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, barcode.getBarcode());
        values.put(KEY_QUANTITY, barcode.getQuantity());

        return db.update(TABLE_BARCODES, values, KEY_ID + "=?",
                new String[]{barcode.getBarcode()});

    }

    public void deleteBarcode(Barcode barcode){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_BARCODES, KEY_ID + "=?",
                new String[]{barcode.getBarcode()});
        db.close();

    }

    public int getBarcodeCount(){

        String countQuery = "SELECT * FROM " + TABLE_BARCODES;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        cursor.close();

        return cursor.getCount();

    }

}
