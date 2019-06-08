package com.firatnet.wts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.firatnet.wts.database.PhoneContract.PhoneEntry;
import com.firatnet.wts.entities.Phone;

import java.util.ArrayList;

public class SafetyDbHelper extends SQLiteOpenHelper {


    public static final String LOG_TAG = SafetyDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "safety.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;


    private static final String ENCODING_SETTING = "PRAGMA encoding ='windows-1256'";

    /**
     * Constructs a new instance of {@link SafetyDbHelper}.
     *
     * @param context of the app
     */
    public SafetyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the products table
        String SQL_CREATE_TABLE =
                "CREATE TABLE " + PhoneEntry.TABLE_NAME + " ("
                        + PhoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + PhoneEntry.COLUMN_NUMBER + " TEXT NOT NULL); ";

        // Execute the SQL statements
        db.execSQL(SQL_CREATE_TABLE);

    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + PhoneEntry.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            db.execSQL(ENCODING_SETTING);
        }
    }
    /**
     * Adding new Phone
     *
     * @param phone
     */
    public void saveNumber(Phone phone) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PhoneEntry.COLUMN_NUMBER , phone.getNumber());

        db.insertOrThrow (PhoneEntry.TABLE_NAME, null, values); // Inserting Row
        db.close(); // Closing database connection
    }

    /**
     * Getting single phone
     *
     * @return phones
     */
    public ArrayList<Phone> getAllPhones() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Phone> phones = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                PhoneEntry._ID,
                PhoneEntry.COLUMN_NUMBER
        };


        // Perform a query on the phones table
        Cursor cursor = db.query(
                PhoneEntry.TABLE_NAME,   // The table to query
                projection,                // The columns to return
                null,                 // The columns for the WHERE clause
                null,             // The values for the WHERE clause
                null,              // Don't group the rows
                null,               // Don't filter by row groups
                null);            // The sort order


        // Figure out the index of each column
        int idColumnIndex = cursor.getColumnIndex(PhoneEntry._ID);
        int numberColumnIndex = cursor.getColumnIndex(PhoneEntry.COLUMN_NUMBER);


        while (cursor.moveToNext()) {

            Phone phone = new Phone();
            phone.setId(Integer.parseInt(cursor.getString(idColumnIndex)));
            phone.setNumber(cursor.getString(numberColumnIndex));
            phones.add(phone);
        }

        cursor.close();
        return phones;

    }


    /**
     * Delete a phone from the database
     *
     * @param phone
     */
    public void deletePhone(Phone phone) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PhoneEntry.TABLE_NAME,
                PhoneEntry._ID + " = ?",
                new String[]{phone.getId()+""}
        );

    }


    /**
     * Delete all phones from the database
     */
    public void deleteAllPhones() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PhoneEntry.TABLE_NAME, null, null);

    }

}