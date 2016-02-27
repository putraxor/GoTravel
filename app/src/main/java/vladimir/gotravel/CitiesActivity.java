package vladimir.gotravel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CitiesActivity extends ActionBarActivity implements View.OnClickListener {
    ImageButton btnBack, btnMap, btnOk;
    Button btnVal1, btnVal2, btnVal3;
    EditText etFrom, etTo, etBdg, etPers;
    SharedPreferences sPref;
    String valute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        btnVal1 = (Button) findViewById(R.id.btnVal1);
        btnVal1.setOnClickListener(this);

        btnVal2 = (Button) findViewById(R.id.btnVal2);
        btnVal2.setOnClickListener(this);
        btnVal2.setVisibility(View.INVISIBLE);

        btnVal3 = (Button) findViewById(R.id.btnVal3);
        btnVal3.setOnClickListener(this);
        btnVal3.setVisibility(View.INVISIBLE);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnMap = (ImageButton) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(this);

        btnOk = (ImageButton) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        etFrom = (EditText) findViewById(R.id.etFrom);
        etTo = (EditText) findViewById(R.id.etTo);
        etBdg = (EditText) findViewById(R.id.etBdg);
        etPers = (EditText) findViewById(R.id.etPers);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sPref = getSharedPreferences("GoTravel_Pref", MODE_PRIVATE);
        etFrom.setText(sPref.getString("City_From", ""));
        etTo.setText(sPref.getString("City_To", ""));
        etBdg.setText(sPref.getString("Budget", ""));
        etPers.setText(sPref.getString("Persons", ""));

        valute = sPref.getString("Valute", "");
        if (!valute.isEmpty()) {
            switch (btnVal1.getText().toString()) {
                case "Usd": valute = "$"; break;
                case "Ua": valute = "?"; break;
                case "Eu": valute = "ˆ"; break;
                default: break;
            }
            String tmp = btnVal1.getText().toString();
            btnVal1.setText(valute);
            if(valute.equals(btnVal2.getText().toString()))
                btnVal2.setText(tmp);
            if(valute.equals(btnVal3.getText().toString()))
                btnVal3.setText(tmp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cities, menu);
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
        Toast toast;
        switch (v.getId()) {
            case R.id.btnVal1:
                if(btnVal2.getVisibility() == View.INVISIBLE) {
                    btnVal2.setVisibility(View.VISIBLE);
                    btnVal3.setVisibility(View.VISIBLE);
                }
                else {
                    btnVal2.setVisibility(View.INVISIBLE);
                    btnVal3.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btnVal2:
                String tmp = btnVal2.getText().toString();
                btnVal2.setText(btnVal1.getText().toString());
                btnVal1.setText(tmp);
                btnVal2.setVisibility(View.INVISIBLE);
                btnVal3.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnVal3:
                tmp = btnVal3.getText().toString();
                btnVal3.setText(btnVal1.getText().toString());
                btnVal1.setText(tmp);
                btnVal2.setVisibility(View.INVISIBLE);
                btnVal3.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnMap:
                saveData();
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_next, R.animator.out_next);
                break;
            case R.id.btnOk:
                if (etFrom.getText().toString().isEmpty()) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Input the cities, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                if(etTo.getText().toString().isEmpty()) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Input the cities, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                if (etBdg.getText().toString().isEmpty()) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Input the budget, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                if (etPers.getText().toString().isEmpty()) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Input the persons, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                if(btnVal2.getVisibility() == View.VISIBLE) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Choose currency, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                saveData();
                intent = new Intent(this, OptionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_next, R.animator.out_next);
                break;
            case R.id.btnBack:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_prev, R.animator.out_prev);
                break;
            default:
                break;
        }
    }

    private void saveData(){
        sPref = getSharedPreferences("GoTravel_Pref", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("City_From", etFrom.getText().toString());
        ed.putString("City_To", etTo.getText().toString());
        ed.putString("Budget", etBdg.getText().toString());
        ed.putString("Persons", etPers.getText().toString());
        switch (btnVal1.getText().toString()) {
            case "$": valute = "Usd"; break;
            case "?": valute = "Ua"; break;
            case "ˆ": valute = "Eu"; break;
            default: break;
        }
        ed.putString("Valute", valute);
        ed.apply();
    }
}
