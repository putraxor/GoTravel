package vladimir.gotravel;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class BookActivity extends ActionBarActivity implements View.OnClickListener {

    ImageButton btnBack, btnOk;
    EditText etName, etSername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        etName = (EditText) findViewById(R.id.etName);
        etSername = (EditText) findViewById(R.id.etSername);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnOk = (ImageButton) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book, menu);
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
                if (etName.getText().toString().isEmpty()) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Input your name, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                if(etSername.getText().toString().isEmpty()) {
                    toast = Toast.makeText(getApplicationContext(),
                            "Input your sername, please!", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                }
                intent = new Intent(this, CardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.in_next, R.animator.out_next);
                break;
            default:
                break;
        }
    }
}
