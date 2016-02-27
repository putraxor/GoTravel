package vladimir.gotravel;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    Button btnCrJ;
    ViewFlipper vwFlp;
    float initialX;
    Fragment frg1, frg2, frg3;

    DBHelper dbHelper;
    SQLiteDatabase dbGoTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCrJ = (Button) findViewById(R.id.btnCrJ);
        btnCrJ.setOnClickListener(this);

        frg1 = getFragmentManager().findFragmentById(R.id.frgMain1);
        frg2 = getFragmentManager().findFragmentById(R.id.frgMain2);
        frg3 = getFragmentManager().findFragmentById(R.id.frgMain3);
        vwFlp = (ViewFlipper) findViewById(R.id.vwFlp);
        vwFlp.setInAnimation(this, R.animator.in_next);
        vwFlp.setOutAnimation(this, R.animator.out_next);

        //Connecting to DataBase
        dbHelper = new DBHelper(this);
        dbGoTravel = dbHelper.getWritableDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();

        vwFlp.startFlipping();
        getTrip(frg1, "Lvov", "London");
        getTrip(frg2, "Lvov", "Paris");
        getTrip(frg3, "Lvov", "Rome");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                if (initialX > finalX) {
                    vwFlp.setInAnimation(this, R.animator.in_next);
                    vwFlp.setOutAnimation(this, R.animator.out_next);
                    vwFlp.showNext();
                    vwFlp.stopFlipping();
                    vwFlp.startFlipping();
                } else {
                    vwFlp.setInAnimation(this, R.animator.in_prev);
                    vwFlp.setOutAnimation(this, R.animator.out_prev);
                    vwFlp.showPrevious();
                    vwFlp.stopFlipping();
                    vwFlp.startFlipping();
                }
                break;
        }
        return false;
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
        switch (v.getId()){
            case R.id.btnCrJ:
                Intent intent = new Intent(this, CitiesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_next, R.animator.out_next);
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

    public void getTrip(Fragment frg, String sCityFrom, String sCityTo) {
        Button  btnLetsGo = (Button) frg.getView().findViewById(R.id.btnLetsGo);
        btnLetsGo.setOnClickListener(this);

        ImageView imvTo = (ImageView)frg.getView().findViewById(R.id.imvTo);
        ImageView imvStay = (ImageView)frg.getView().findViewById(R.id.imvStay);
        ImageView imvFrom = (ImageView)frg.getView().findViewById(R.id.imvFrom);

        TextView tvTo = (TextView)frg.getView().findViewById(R.id.tvTo);
        TextView tvStay = (TextView)frg.getView().findViewById(R.id.tvStay);
        TextView tvFrom = (TextView)frg.getView().findViewById(R.id.tvFrom);
        TextView tvPriceTo = (TextView)frg.getView().findViewById(R.id.tvPriceTo);
        TextView tvPriceStay = (TextView)frg.getView().findViewById(R.id.tvPriceStay);
        TextView tvPriceFrom = (TextView)frg.getView().findViewById(R.id.tvPriceFrom);
        TextView tvPriceSum = (TextView)frg.getView().findViewById(R.id.tvPriceSum);

        Cursor cTo, cStay, cFrom;

        //Formation cursor for way To
        String selection = "cityFrom == ? and cityTo == ?";
        String[] selectionArgs = new String[]{ sCityFrom, sCityTo };
        String orderBy = "priceUsd";
        cTo = dbGoTravel.query("TransTable", null, selection, selectionArgs, null, null, orderBy);

        //Formation cursor for Stay
        selection = "cityHot == ?";
        selectionArgs = new String[]{ sCityTo };
        orderBy = "priceUsd";
        cStay = dbGoTravel.query("HotelTable", null, selection, selectionArgs, null, null, orderBy);

        //Formation cursor for way From
        selection = "cityFrom == ? and cityTo == ?";
        selectionArgs = new String[]{ sCityTo, sCityFrom };
        orderBy = "priceUsd";
        cFrom = dbGoTravel.query("TransTable", null, selection, selectionArgs, null, null, orderBy);

        //Generation result from cursors
        if (cTo != null && cStay != null && cFrom != null) {
            double priceSum = 0, priceTo = 0, priceStay = 0, priceFrom = 0;
            if (cTo.moveToFirst()) {
                switch (cTo.getString(cTo.getColumnIndex("typeTrans"))) {
                    case "Plane": imvTo.setImageResource(R.drawable.plane); break;
                    case "Train": imvTo.setImageResource(R.drawable.train); break;
                    case "Bus": imvTo.setImageResource(R.drawable.bus); break;
                }

                tvTo.setText(cTo.getString(cTo.getColumnIndex("cityFrom")) + " - " +
                        cTo.getString(cTo.getColumnIndex("cityTo")));
                priceTo = Double.parseDouble(cTo.getString(cTo.getColumnIndex("priceUsd")));
                tvPriceTo.setText(priceTo + "$");
            }

            if (cStay.moveToFirst()) {
                String typeH = "";
                switch (cStay.getString(cStay.getColumnIndex("typeHot"))) {
                    case "Hotel":
                        imvStay.setImageResource(R.drawable.hotel);
                        typeH = "Hotel ";
                        break;
                    case "Hostel":
                        imvStay.setImageResource(R.drawable.hotel);
                        typeH = "Hostel ";
                        break;
                }

                tvStay.setText(typeH + cStay.getString(cStay.getColumnIndex("nameHot")));
                priceStay = Double.parseDouble(cStay.getString(cStay.getColumnIndex("priceUsd")));
                tvPriceStay.setText(priceStay + "$");
            }

            if (cFrom.moveToFirst()) {
                switch (cFrom.getString(cFrom.getColumnIndex("typeTrans"))) {
                    case "Plane": imvFrom.setImageResource(R.drawable.plane); break;
                    case "Train": imvFrom.setImageResource(R.drawable.train); break;
                    case "Bus": imvFrom.setImageResource(R.drawable.bus); break;
                }

                tvFrom.setText(cFrom.getString(cFrom.getColumnIndex("cityFrom")) + " - " +
                        cFrom.getString(cFrom.getColumnIndex("cityTo")));
                priceFrom = Double.parseDouble(cFrom.getString(cFrom.getColumnIndex("priceUsd")));
                tvPriceFrom.setText(priceFrom + "$");
            }

            priceSum = priceTo + priceStay + priceFrom;
            tvPriceSum.setText(String.valueOf(priceSum) + "$");

            cTo.close();
            cStay.close();
            cFrom.close();
        }
    }
}
