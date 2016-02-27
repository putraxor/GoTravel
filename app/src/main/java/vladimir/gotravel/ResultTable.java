package vladimir.gotravel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultTable extends RelativeLayout {

    Button btnLetsGo;
    ImageView imvTo, imvStay, imvFrom;
    TextView tvTo, tvStay, tvFrom, tvPriceTo, tvPriceStay, tvPriceFrom, tvPriceSum;

    public ResultTable(final Context context) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.table_layout, this);

        imvTo = (ImageView) findViewById(R.id.imvTo);
        imvStay = (ImageView) findViewById(R.id.imvStay);
        imvFrom = (ImageView) findViewById(R.id.imvFrom);

        tvTo = (TextView) findViewById(R.id.tvTo);
        tvStay = (TextView) findViewById(R.id.tvStay);
        tvFrom = (TextView) findViewById(R.id.tvFrom);
        tvPriceTo = (TextView) findViewById(R.id.tvPriceTo);
        tvPriceStay = (TextView) findViewById(R.id.tvPriceStay);
        tvPriceFrom = (TextView) findViewById(R.id.tvPriceFrom);
        tvPriceSum = (TextView) findViewById(R.id.tvPriceSum);

        btnLetsGo = (Button) findViewById(R.id.btnLetsGo);
        btnLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                }
            }
        });
    }
}
