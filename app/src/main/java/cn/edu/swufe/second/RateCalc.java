package cn.edu.swufe.second;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class RateCalc extends AppCompatActivity {
    private final String TAG = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        final float rate = getIntent().getFloatExtra("rate",0f);
        String title = getIntent().getStringExtra("title");
        Log.i(TAG,"onCreate:rate="+rate);
        Log.i(TAG,"onCreate:title="+title);
        ((TextView)findViewById(R.id.title)).setText(title);
        EditText inp2 =(EditText)findViewById(R.id.inp2);
        inp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                   TextView show = (TextView) RateCalc.this.findViewById(R.id.show2);
                   if(s.length()>0){
                       float val = Float.parseFloat(s.toString());
                       show.setText(val+"rmb==  "+(100/rate*val));
                   }else{
                           show.setText("");
                           }
            }
        });
    }
}
