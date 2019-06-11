package com.firatnet.wts.classes;

import android.content.Context;
import android.content.SharedPreferences;

//import com.firatnet.wts.entities.Student;

import com.firatnet.wts.entities.Student;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceHelper {


    private static final String KEY = "com.firatnet.wts";

    private static final String SETTING_KEY_ID = "com.firatnet.wts.SETTING_KEY_ID";
    private static final String SETTING_KEY_NAME = "com.firatnet.wts.SETTING_KEY_NAME";
    private static final String SETTING_KEY_EMAIL = "com.firatnet.wts.SETTING_KEY_EMAIL";

    private static final String SETTING_KEY_PASSWORD = "com.firatnet.wts.SETTING_KEY_PASSWORD";
    private static String SETTING_KEY_PHONE = "com.firatnet.wts.SETTING_KEY_PHONE";
    private static String SETTING_KEY_PHOTO_URL = "com.firatnet.wts.SETTING_KEY_PHOTO_URL";

    private static String SETTING_KEY_FACULTY = "com.firatnet.wts.SETTING_KEY_PHOTO_URL";
    private static String SETTING_KEY_DEPARTMENT = "com.firatnet.wts.SETTING_KEY_PHOTO_URL";
    private static String SETTING_KEY_SUBJECT = "com.firatnet.wts.SETTING_KEY_PHOTO_URL";
    private static String SETTING_KEY_LEVEL = "com.firatnet.wts.SETTING_KEY_PHOTO_URL";

    private static String SETTING_KEY_LOGIN_STATE = "com.firatnet.wts.SETTING_KEY_LOGIN_STATE";

    private static String SETTING_KEY_MESSAGE= "com.firatnet.wts.SETTING_KEY_MESSAGE";

    public static String SETTING_VALUE_ID = "";
    public static String SETTING_VALUE_NAME = "";
    public static String SETTING_VALUE_EMAIL = "";
    public static String SETTING_VALUE_PASSWORD = "";
    public static String SETTING_VALUE__PHONE = "";
    private static String SETTING_VALUE_LOGIN_STATE = "";


    private Context context;

    public PreferenceHelper(Context context) {
        this.context = context;
    }


    public String getSettingValueId() {
        return readSharedPreference(KEY, SETTING_KEY_ID);
    }

    public String getSettingValueEmail() {
        return readSharedPreference(KEY, SETTING_KEY_EMAIL);
    }
    public String getSettingValuePhotoUrl() {
        return readSharedPreference(KEY, SETTING_KEY_PHOTO_URL);
    }

    public String getSettingValueMessage() {
        if(readSharedPreference(KEY, SETTING_KEY_MESSAGE).equals(""))
            return "Please Help Me!!!!!! I need your help";
        return readSharedPreference(KEY, SETTING_KEY_MESSAGE);
    }

    public String getSettingValueName() {
        return readSharedPreference(KEY, SETTING_KEY_NAME);
    }


    /**
     * Returns a value saved in SharedPreference
     *
     * @param key  package name of the app
     * @param name The stored value's key
     * @return Value of the key that stored in the SharedPreference
     */
    public String readSharedPreference(String key, String name) {
        SharedPreferences sharedPref = context.getSharedPreferences(key, MODE_PRIVATE);

        //0 is default_value if no vaule
        return sharedPref.getString(name, "");
    }


    /**
     * Persist a (Key, Value) pair in SharedPreference
     *
     * @param valueToSave The value to be saved
     * @param key         package name of the app
     * @param name        The stored value's key
     */
    public void writeSharedPreference(String valueToSave, String key, String name) {

        SharedPreferences sharedPref = context.getSharedPreferences(key, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(name, valueToSave);

        editor.apply();

    }


    public void setLoginState(Boolean islogin) {

        if (islogin) {
            SETTING_VALUE_LOGIN_STATE = "1";
            writeSharedPreference(SETTING_VALUE_LOGIN_STATE, KEY, SETTING_KEY_LOGIN_STATE);
        } else {
            SETTING_VALUE_LOGIN_STATE = "";
            writeSharedPreference(SETTING_VALUE_LOGIN_STATE, KEY, SETTING_KEY_LOGIN_STATE);
        }

    }

    public String getLoginState() {

        return readSharedPreference(KEY, SETTING_KEY_LOGIN_STATE);

    }

    public void deleteUser() {
        writeSharedPreference("", KEY, SETTING_KEY_ID);
        writeSharedPreference("", KEY, SETTING_KEY_NAME);
        writeSharedPreference("", KEY, SETTING_KEY_EMAIL);

        writeSharedPreference("", KEY, SETTING_KEY_PHONE);
        writeSharedPreference("", KEY, SETTING_KEY_PHOTO_URL);
        writeSharedPreference("", KEY, SETTING_KEY_FACULTY);
        writeSharedPreference("", KEY, SETTING_KEY_DEPARTMENT);

        writeSharedPreference("", KEY, SETTING_KEY_SUBJECT);
        writeSharedPreference("", KEY, SETTING_KEY_LEVEL);
        //writeSharedPreference("", KEY, SETTING_KEY_PASSWORD);

    }

    //
    public void saveUser(Student student) {
        writeSharedPreference(student.getId(), KEY, SETTING_KEY_ID);
        writeSharedPreference(student.getName(), KEY, SETTING_KEY_NAME);
        writeSharedPreference(student.getEmail(), KEY, SETTING_KEY_EMAIL);

        writeSharedPreference(student.getPhone(), KEY, SETTING_KEY_PHONE);
        writeSharedPreference(student.getPhoto_url(), KEY, SETTING_KEY_PHOTO_URL);
        writeSharedPreference(student.getFaculty(), KEY, SETTING_KEY_FACULTY);
        writeSharedPreference(student.getDepartment(), KEY, SETTING_KEY_DEPARTMENT);

        writeSharedPreference(student.getSubject(), KEY, SETTING_KEY_SUBJECT);
        writeSharedPreference(student.getLevel(), KEY, SETTING_KEY_LEVEL);
        //writeSharedPreference(student.getPassword(), KEY, SETTING_KEY_PASSWORD);

    }

    public void saveMessage(String message)
    {
        writeSharedPreference(message, KEY, SETTING_KEY_MESSAGE);
    }
//    public void editUser(Student register) {
//
//        writeSharedPreference(register.getName(), KEY, SETTING_KEY_NAME);
//        writeSharedPreference(register.getEmail(), KEY, SETTING_KEY_EMAIL);
//
////        writeSharedPreference(register.getPassword(), KEY, SETTING_KEY_PASSWORD);
//        writeSharedPreference(register.getPhone(), KEY, SETTING_KEY_PHONE);
//       // writeSharedPreference(register.getPhoto_url(), KEY, SETTING_KEY_PHOTO_URL);
//
//    }

}
