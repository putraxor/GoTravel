package vladimir.gotravel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "GoTravel_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        int N = 16, M = 8;
        String[] typeTrans = { "Plane", "Plane", "Plane", "Plane", "Plane", "Plane", "Train",
                "Train", "Train", "Train", "Train", "Train", "Bus", "Bus", "Bus", "Bus" };
        String[] cityFrom = { "Lvov", "Lvov", "Lvov", "Lvov", "Lvov", "Lvov", "Lvov", "Lvov",
                "Lvov", "Lvov", "Lvov", "Lvov", "Lvov", "Lvov", "Lvov", "Lvov"};
        String[] cityTo = { "Rome", "Berlin", "Paris", "Rome", "London", "London", "Paris", "Berlin",
                "Rome", "Berlin", "Paris", "Rome", "London", "London", "Paris", "Berlin"};
        double[] priceTrUsd = new double[]{ 400, 311, 195, 142, 128, 82, 80, 160, 30, 11, 95, 42, 28, 42, 40, 60 };
        double[] priceTrEu = new double[N], priceTrUa = new double[N];
        for (int i = 0; i < N; i++) {
            priceTrEu[i] = priceTrUsd[i] * 1.1;
            priceTrUa[i] = priceTrUsd[i] * 15.5;
        }
        int[] seatsFree = { 3, 2, 4, 5, 2, 1, 5, 1, 2, 3, 2, 4, 5, 2, 1, 2 };
        //------------------------------------------------------------------------------
        String[] typeHot = { "Hotel", "Hotel", "Hotel", "Hotel", "Hostel", "Hostel", "Hostel", "Hostel" };
        String[] nameHot = { "Grand Hotel", "Plaza", "Ocean", "Europe", "Sweet Dreams", "Hostage",
                "London", "Kolizium" };
        String[] cityHot = { "London", "Rome", "Berlin", "Paris", "Berlin", "Paris", "London", "Rome" };
        int[] starsHot = { 5, 4, 3, 2, 1, 2, 3, 2 };
        int[] priceHotUsd = { 400, 311, 195, 142, 80, 100, 120, 150 };
        double[] priceHotEu = new double[M], priceHotUa = new double[M];
        for (int i = 0; i < M; i++) {
            priceHotEu[i] = priceHotUsd[i] * 1.1;
            priceHotUa[i] = priceHotUsd[i] * 15.5;
        }
        int[] spaceFree = { 4, 3, 1, 2, 1, 2, 3, 4 };
        ContentValues cv = new ContentValues();
        //---------------------------------------------------------------------------------
        db.execSQL("create table TransTable (id integer primary key autoincrement, typeTrans text," +
                " cityFrom text, cityTo text, priceUsd real, priceEu real, priceUa real, seatsFree integer);");
        cv.clear();
        for (int i = 0; i < N; i++) {
            cv.put("typeTrans", typeTrans[i]);
            cv.put("cityFrom", cityFrom[i]);
            cv.put("cityTo", cityTo[i]);
            cv.put("priceUsd", priceTrUsd[i]);
            cv.put("priceEu", priceTrEu[i]);
            cv.put("priceUa", priceTrUa[i]);
            cv.put("seatsFree", seatsFree[i]);
            db.insert("TransTable", null, cv);
            cv.clear();
            cv.put("typeTrans", typeTrans[i]);
            cv.put("cityFrom", cityTo[i]);
            cv.put("cityTo", cityFrom[i]);
            cv.put("priceUsd", priceTrUsd[i]);
            cv.put("priceEu", priceTrEu[i]);
            cv.put("priceUa", priceTrUa[i]);
            cv.put("seatsFree", seatsFree[N - 1 - i]);
            db.insert("TransTable", null, cv);
        }

        db.execSQL("create table HotelTable (id integer primary key autoincrement, typeHot text," +
                " nameHot text, cityHot text, starsHot integer, priceUsd real, priceEu real, priceUa real, spaceFree integer);");
        cv.clear();
        for (int i = 0; i < M; i++) {
            cv.put("typeHot", typeHot[i]);
            cv.put("nameHot", nameHot[i]);
            cv.put("cityHot", cityHot[i]);
            cv.put("starsHot", starsHot[i]);
            cv.put("priceUsd", priceHotUsd[i]);
            cv.put("priceEu", priceHotEu[i]);
            cv.put("priceUa", priceHotUa[i]);
            cv.put("spaceFree", spaceFree[i]);
            db.insert("HotelTable", null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Out cursor in log
    final String LOG_TAG = "myLogs";
    public void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }
}
