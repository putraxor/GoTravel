package vladimir.gotravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultsActivity extends ActionBarActivity implements View.OnClickListener {
    String sCityFrom, sCityTo, sValute;
    int sBudget, sPersons, sStars;
    boolean sPlane = false, sTrain = false, sBus = false, sHotel = false, sHostel = false;

    ImageButton btnBack;
    TextView tvNoResult;

    LinearLayout ltResult;
    DBHelper dbHelper;
    SQLiteDatabase dbGoTravel;
    SharedPreferences sPref;

    Cursor cTo, cStay, cFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ltResult = (LinearLayout) findViewById(R.id.ltResult);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        tvNoResult = (TextView) findViewById(R.id.tvNoResult);

        //Connecting to DataBase
        dbHelper = new DBHelper(this);
        dbGoTravel = dbHelper.getWritableDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*//Getting info from db
        Cursor c;
        c = dbGoTravel.query("TransTable", null, null, null, null, null, null);
        dbHelper.logCursor(c);
        c = dbGoTravel.query("HotelTable", null, null, null, null, null, null);
        dbHelper.logCursor(c);
        c.close();*/

        //Getting values from Preferences
        sPref = getSharedPreferences("GoTravel_Pref", MODE_PRIVATE);
        sCityFrom = sPref.getString("City_From", "");
        sCityTo = sPref.getString("City_To", "");
        sValute = sPref.getString("Valute", "");
        sBudget = Integer.parseInt(sPref.getString("Budget", ""));
        sPersons = Integer.parseInt(sPref.getString("Persons", ""));
        sStars = Integer.parseInt(sPref.getString("Stars", ""));
        String sPrice = "";

        String tPlane = "", tTrain = "", tBus = "", tHotel = "", tHostel = "";
        if("True".equals(sPref.getString("Plane", ""))) {
            tPlane = "Plane";
            sPlane = true;
        }
        if("True".equals(sPref.getString("Train", ""))) {
            tTrain = "Train";
            sTrain = true;
        }
        if("True".equals(sPref.getString("Bus", ""))) {
            tBus = "Bus";
            sBus = true;
        }
        if("True".equals(sPref.getString("Hotel", ""))) {
            tHotel = "Hotel";
            sHotel = true;
        }
        if("True".equals(sPref.getString("Hostel", ""))) {
            tHostel = "Hostel";
            sHostel = true;
        }

        String vEnd = "";
        switch (sValute) {
            case "Usd":
                sPrice = "priceUsd";
                vEnd = "$";
                break;
            case "Ua":
                sPrice = "priceUa";
                vEnd = "?";
                break;
            case "Eu":
                sPrice = "priceEu";
                vEnd = "ˆ";
                break;
        }

        //Formation cursor for way To
        String selection = "(typeTrans == ? or typeTrans == ? or typeTrans == ?) and cityFrom == ? and cityTo == ? and seatsFree >= ?";
        String[] selectionArgs = new String[]{tPlane, tTrain, tBus, sCityFrom, sCityTo, String.valueOf(sPersons) };
        String orderBy = sPrice;
        cTo = dbGoTravel.query("TransTable", null, selection, selectionArgs, null, null, orderBy);

        //Formation cursor for Stay
        selection = "(typeHot == ? or typeHot == ?) and cityHot == ? and spaceFree >= ?";
        selectionArgs = new String[]{tHotel, tHostel, sCityTo, String.valueOf(sPersons) };
        orderBy = sPrice;
        cStay = dbGoTravel.query("HotelTable", null, selection, selectionArgs, null, null, orderBy);

        //Formation cursor for way From
        selection = "(typeTrans == ? or typeTrans == ? or typeTrans == ?) and cityFrom == ? and cityTo == ? and seatsFree >= ?";
        selectionArgs = new String[]{tPlane, tTrain, tBus, sCityTo, sCityFrom, String.valueOf(sPersons) };
        orderBy = sPrice;
        cFrom = dbGoTravel.query("TransTable", null, selection, selectionArgs, null, null, orderBy);
        //--------------------------------------------------------------------------------
        String imvTo, tvTo, tvPriceTo, imvStay, tvStay, tvPriceStay, imvFrom, tvFrom, tvPriceFrom, tvPriceSum;

        //Generation result from cursors
        if (cTo != null && cStay != null && cFrom != null) {
            double priceSum = 0, priceTo = 0, priceStay = 0, priceFrom = 0;
            int i = 0;
            if (cTo.moveToFirst()) {
                do {
                    imvTo = cTo.getString(cTo.getColumnIndex("typeTrans"));
                    tvTo = (cTo.getString(cTo.getColumnIndex("cityFrom")) + " - " +
                            cTo.getString(cTo.getColumnIndex("cityTo")));
                    priceTo = Double.parseDouble(cTo.getString(cTo.getColumnIndex("priceUsd")));
                    priceTo *= sPersons;
                    tvPriceTo = (priceTo + vEnd);

                    if (cStay.moveToFirst()) {
                        do {
                            imvStay = cStay.getString(cStay.getColumnIndex("typeHot"));
                            tvStay = cStay.getString(cStay.getColumnIndex("nameHot"));
                            priceStay = Double.parseDouble(cStay.getString(cStay.getColumnIndex("priceUsd")));
                            priceStay *= sPersons;
                            tvPriceStay = (priceStay + vEnd);

                            if (cFrom.moveToFirst()) {
                                do {
                                    imvFrom = cFrom.getString(cFrom.getColumnIndex("typeTrans"));
                                    tvFrom = (cFrom.getString(cFrom.getColumnIndex("cityFrom")) + " - " +
                                            cFrom.getString(cFrom.getColumnIndex("cityTo")));
                                    priceFrom = Double.parseDouble(cFrom.getString(cFrom.getColumnIndex("priceUsd")));
                                    priceFrom *= sPersons;
                                    tvPriceFrom = (priceFrom + vEnd);

                                    priceSum = priceTo + priceStay + priceFrom;
                                    tvPriceSum = (String.valueOf(priceSum) + vEnd);

                                    if(priceSum <= sBudget) {
                                        ResultTable table = new ResultTable(getApplicationContext());
                                        outFragment(table, imvTo, tvTo, tvPriceTo, imvStay, tvStay,
                                                tvPriceStay, imvFrom, tvFrom, tvPriceFrom, tvPriceSum);
                                        ltResult.addView(table);
                                        table.btnLetsGo.setOnClickListener(this);
                                        i++;
                                    }
                                } while (cFrom.moveToNext() && i >= 0);
                            }
                            else {
                                tvNoResult.setVisibility(View.VISIBLE);
                                tvNoResult.setText("No search results.\n No transport From.");
                            }
                        } while (cStay.moveToNext() && i >= 0);
                    }
                    else {
                        tvNoResult.setVisibility(View.VISIBLE);
                        tvNoResult.setText("No search results.\n No Hotels.");
                    }
                } while (cTo.moveToNext() && i >= 0);
            }
            else {
                tvNoResult.setVisibility(View.VISIBLE);
                tvNoResult.setText("No search results.\n No transport To.");
            }

            cTo.close();
            cStay.close();
            cFrom.close();
        }
        else {
            tvNoResult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ltTable1:
                Intent intent = new Intent(this, BookActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_next, R.animator.out_next);
                break;
            case R.id.btnEdit:
                intent = new Intent(this, BookActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_next, R.animator.out_next);
                break;
            case R.id.btnBack:
                intent = new Intent(this, OptionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_prev, R.animator.out_prev);
                break;
            case R.id.btnLetsGo:

                intent = new Intent(this, BookActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_next, R.animator.out_next);
                break;
            default:
                break;
        }
    }

    public  void outFragment(ResultTable tbFrag, String simvTo, String stvTo, String stvPriceTo, String simvStay, String stvStay,
                             String stvPriceStay, String simvFrom, String stvFrom, String stvPriceFrom, String stvPriceSum) {

        ImageView imvTo = (ImageView) tbFrag.findViewById(R.id.imvTo);
        ImageView imvStay = (ImageView) tbFrag.findViewById(R.id.imvStay);
        ImageView imvFrom = (ImageView) tbFrag.findViewById(R.id.imvFrom);

        TextView tvTo = (TextView) tbFrag.findViewById(R.id.tvTo);
        TextView tvStay = (TextView) tbFrag.findViewById(R.id.tvStay);
        TextView tvFrom = (TextView) tbFrag.findViewById(R.id.tvFrom);
        TextView tvPriceTo = (TextView) tbFrag.findViewById(R.id.tvPriceTo);
        TextView tvPriceStay = (TextView) tbFrag.findViewById(R.id.tvPriceStay);
        TextView tvPriceFrom = (TextView) tbFrag.findViewById(R.id.tvPriceFrom);
        TextView tvPriceSum = (TextView) tbFrag.findViewById(R.id.tvPriceSum);

        switch (simvTo) {
            case "Plane":
                imvTo.setImageResource(R.drawable.plane);
                break;
            case "Train":
                imvTo.setImageResource(R.drawable.train);
                break;
            case "Bus":
                imvTo.setImageResource(R.drawable.bus);
                break;
        }
        tvTo.setText(stvTo);
        tvPriceTo.setText(stvPriceTo);
        //---------------------------------------------------------------
        String typeH = "";
        switch (simvStay) {
            case "Hotel":
                imvStay.setImageResource(R.drawable.hotel);
                typeH = "Hotel ";
                break;
            case "Hostel":
                imvStay.setImageResource(R.drawable.hostel);
                typeH = "Hostel ";
                break;
        }
            tvStay.setText(typeH + stvStay);
            tvPriceStay.setText(stvPriceStay);
        //-------------------------------------------------------------------
        switch (simvFrom) {
            case "Plane":
                imvFrom.setImageResource(R.drawable.plane);
                break;
            case "Train":
                imvFrom.setImageResource(R.drawable.train);
                break;
            case "Bus":
                imvFrom.setImageResource(R.drawable.bus);
                break;
        }
        tvFrom.setText(stvFrom);
        tvPriceFrom.setText(stvPriceFrom);

        tvPriceSum.setText(stvPriceSum);
    }
}
