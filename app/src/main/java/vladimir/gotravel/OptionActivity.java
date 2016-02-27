package vladimir.gotravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

public class OptionActivity extends ActionBarActivity implements View.OnClickListener {
    ImageButton btnBack, btnOk, btnStar1, btnStar2, btnStar3, btnStar4, btnStar5;
    CheckBox cbPlane, cbTrain, cbBus, cbHotel, cbHostel;
    int Stars = 1;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        cbPlane = (CheckBox) findViewById(R.id.cbPlane);
        cbTrain = (CheckBox) findViewById(R.id.cbTrane);
        cbBus = (CheckBox) findViewById(R.id.cbBus);
        cbHotel = (CheckBox) findViewById(R.id.cbHotel);
        cbHostel = (CheckBox) findViewById(R.id.cbHostel);

        btnStar1 = (ImageButton) findViewById(R.id.btnStar1);
        btnStar1.setOnClickListener(this);

        btnStar2 = (ImageButton) findViewById(R.id.btnStar2);
        btnStar2.setOnClickListener(this);

        btnStar3 = (ImageButton) findViewById(R.id.btnStar3);
        btnStar3.setOnClickListener(this);

        btnStar4 = (ImageButton) findViewById(R.id.btnStar4);
        btnStar4.setOnClickListener(this);

        btnStar5 = (ImageButton) findViewById(R.id.btnStar5);
        btnStar5.setOnClickListener(this);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnOk = (ImageButton) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        sPref = getSharedPreferences("GoTravel_Pref", MODE_PRIVATE);
        if("True".equals(sPref.getString("Plane", "")))
            cbPlane.setChecked(true);
        if("True".equals(sPref.getString("Train", "")))
            cbTrain.setChecked(true);
        if("True".equals(sPref.getString("Bus", "")))
            cbBus.setChecked(true);
        if("True".equals(sPref.getString("Hotel", ""))) {
            cbHotel.setChecked(true);

        }
        if("True".equals(sPref.getString("Hostel", "")))
            cbHostel.setChecked(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option, menu);
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
        switch (v.getId()){
            case R.id.btnStar1:
                if(cbHotel.isChecked()) {
                    cbHotel.setChecked(true);
                    btnStar1.setImageResource(R.drawable.star_ok);
                    btnStar2.setImageResource(R.drawable.star);
                    btnStar3.setImageResource(R.drawable.star);
                    btnStar4.setImageResource(R.drawable.star);
                    btnStar5.setImageResource(R.drawable.star);
                    Stars = 1;
                }
                break;
            case R.id.btnStar2:
                if(cbHotel.isChecked()) {
                    btnStar1.setImageResource(R.drawable.star_ok);
                    btnStar2.setImageResource(R.drawable.star_ok);
                    btnStar3.setImageResource(R.drawable.star);
                    btnStar4.setImageResource(R.drawable.star);
                    btnStar5.setImageResource(R.drawable.star);
                    Stars = 2;
                }
                break;
            case R.id.btnStar3:
                if(cbHotel.isChecked()) {
                    btnStar1.setImageResource(R.drawable.star_ok);
                    btnStar2.setImageResource(R.drawable.star_ok);
                    btnStar3.setImageResource(R.drawable.star_ok);
                    btnStar4.setImageResource(R.drawable.star);
                    btnStar5.setImageResource(R.drawable.star);
                    Stars = 3;
                }
                break;
            case R.id.btnStar4:
                if(cbHotel.isChecked()) {
                    btnStar1.setImageResource(R.drawable.star_ok);
                    btnStar2.setImageResource(R.drawable.star_ok);
                    btnStar3.setImageResource(R.drawable.star_ok);
                    btnStar4.setImageResource(R.drawable.star_ok);
                    btnStar5.setImageResource(R.drawable.star);
                    Stars = 4;
                }
                break;
            case R.id.btnStar5:
                if(cbHotel.isChecked()) {
                    btnStar1.setImageResource(R.drawable.star_ok);
                    btnStar2.setImageResource(R.drawable.star_ok);
                    btnStar3.setImageResource(R.drawable.star_ok);
                    btnStar4.setImageResource(R.drawable.star_ok);
                    btnStar5.setImageResource(R.drawable.star_ok);
                    Stars = 5;
                }
                break;
            case R.id.btnOk:
                if(!cbPlane.isChecked() && !cbTrain.isChecked() && !cbBus.isChecked()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Check something from Transfer!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                if(!cbHotel.isChecked() && !cbHostel.isChecked()) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Check something from Stay in!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                saveData();
                Intent intent = new Intent(this, ResultsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_next, R.animator.out_next);
                break;
            case R.id.btnBack:
                intent = new Intent(this, CitiesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_prev, R.animator.out_prev);
                break;
            default:
                break;
        }
    }

    void saveData() {
        sPref = getSharedPreferences("GoTravel_Pref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("Stars", String.valueOf(Stars));

        if (cbPlane.isChecked())
            ed.putString("Plane", "True");
        else
            ed.putString("Plane", "False");
        if (cbTrain.isChecked())
            ed.putString("Train", "True");
        else
            ed.putString("Train", "False");
        if (cbBus.isChecked())
            ed.putString("Bus", "True");
        else
            ed.putString("Bus", "False");

        if (cbHotel.isChecked())
            ed.putString("Hotel", "True");
        else
            ed.putString("Hotel", "False");
        if (cbHostel.isChecked())
            ed.putString("Hostel", "True");
        else
            ed.putString("Hostel", "False");
        ed.apply();
    }
}
