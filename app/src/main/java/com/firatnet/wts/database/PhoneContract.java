package com.firatnet.wts.database;

import android.provider.BaseColumns;

public final class PhoneContract {


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PhoneContract() {
    }

    /**
     * Inner class that defines constant values for the Phones database table.
     * Each entry in the table represents a single Phone.
     */
    public static final class PhoneEntry implements BaseColumns {


        /**
         * Name of database table for Phone
         */
        public final static String TABLE_NAME = "phones";


        /**
         * Unique ID number for the Phone (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;


        /**
         * Number of the Phone.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_NUMBER = "number_phone";


    }


}