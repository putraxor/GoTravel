package vladimir.gotravel;

import android.app.Instrumentation;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;


public class CardActivity extends ActionBarActivity implements View.OnClickListener {

    ImageButton btnBack, btnOk;
    EditText etNomber, etMY, etCVV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        etNomber = (EditText) findViewById(R.id.etNomber);
        etMY = (EditText) findViewById(R.id.etMY);
        etCVV = (EditText) findViewById(R.id.etCVV);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnOk = (ImageButton) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        etNomber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (etNomber.getText().toString().length() == 4 ||
                        etNomber.getText().toString().length() == 9 ||
                        etNomber.getText().toString().length() == 14) {
                    etNomber.setTextKeepState(etNomber.getText().toString() + " ");
                }
                return false;
            }
        });

        etMY.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (etMY.getText().toString().length() == 2) {
                    etMY.setTextKeepState(etMY.getText().toString() + "/");
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card, menu);
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
            case R.id.btnBack:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_prev, R.animator.out_prev);
                break;
            case R.id.btnOk:
                Toast toast;
                if (etNomber.getText().toString().isEmpty()) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Input the card nomber, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                if(etMY.getText().toString().isEmpty()) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Input the MM/YY, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                if(etCVV.getText().toString().isEmpty()) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Input the CVV, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                toast = Toast.makeText(getApplicationContext(),
                        "The booking is done. We wish you a good trip!", Toast.LENGTH_SHORT);
                toast.show();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_next, R.animator.out_next);
                break;
            default:
                break;
        }
    }
}
