package com.sadek.apps.freelance7rfeen.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.sadek.apps.freelance7rfeen.R;


/**
 * Created by Mahmoud Sadek on 3/11/2017.
 */
public class FreelanceDbHelper extends SQLiteOpenHelper {


    public final static String DATABASE_NAME = "freelancetabase";
    public final static int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "freelancetable";
    public final static String UID = "id";
    public final static String NAME = "name";
    public final static String ADDRESS = "address";
    public final static String SKILLS = "skills";
    public final static String EDUCATION = "education";
    public final static String EXPERIENCE = "experience";
    public final static String PHONE = "phone";
    public final static String EX_YEARS = "years_experience";
    public final static String EVALUATION = "global_evaluation";
    public final static String AVAILABLE = "available";
    public final static String GOVERNMENT = "government";
    public final static String CITY = "city";
    public final static String CARRIER = "carrier";
    public final static String RATE = "rate";
    public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + UID + " INTEGER PRIMARY KEY ," + NAME +
            " VARCHAR(255) ," + ADDRESS + " VARCHAR(255) ," + SKILLS + " VARCHAR(555) ," + EDUCATION +
            " VARCHAR(555) ," + EXPERIENCE + " VARCHAR(255) ," + PHONE + " VARCHAR(255) ," + EX_YEARS + " VARCHAR(255) ,"
            + EVALUATION + " INTEGER ," + AVAILABLE + " INTEGER ," + GOVERNMENT + " INTEGER ,"
            + CITY + " INTEGER ," + CARRIER + " INTEGER ," + RATE + "  VARCHAR(255));";
    public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public Context context;

    public FreelanceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
            Toast.makeText(context, R.string.database_created, Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(context, context.getString(R.string.database_error) + e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
        } catch (SQLException e) {
            Toast.makeText(context, context.getString(R.string.database_upgrade_error) + e, Toast.LENGTH_SHORT).show();
        }
    }
}
