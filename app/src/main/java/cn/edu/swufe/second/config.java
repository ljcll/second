package cn.edu.swufe.second;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class config extends AppCompatActivity {
    public final String TAG = "config";
    EditText dollarText;
    EditText euroText;
    EditText wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();
        float doller2 = intent.getFloatExtra("dollar_rate_key", 0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_key", 0.0f);
        float won2 = intent.getFloatExtra("won_rate_key", 0.0f);

        Log.i(TAG, "onCreate:dollar2=" + doller2);
        Log.i(TAG, "onCreate:euro2=" + euro2);
        Log.i(TAG, "onCreate:won2=" + won2);

        dollarText = (EditText) findViewById(R.id.doller_rate);
        euroText = (EditText) findViewById(R.id.euro_rate);
        wonText = (EditText) findViewById(R.id.won_rate);

        dollarText.setText(String.valueOf(doller2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));


    }

    public void save(View btn) {
        Log.i("config", "save");
        float newdoller = Float.parseFloat(dollarText.getText().toString());
        float neweuro = Float.parseFloat(euroText.getText().toString());
        float newwon = Float.parseFloat(wonText.getText().toString());

        Log.i(TAG, "save:获取新的值");
        Log.i(TAG, "onCreate:newdoller=" + newdoller);
        Log.i(TAG, "onCreate:newdoller=" + euroText);
        Log.i(TAG, "onCreate:newdoller=" + wonText);

        Intent intent = getIntent();
        Bundle bul = new Bundle();
        bul.putFloat("key doller", newdoller);
        bul.putFloat("key euro", neweuro);
        bul.putFloat("key won", newwon);
        intent.putExtras(bul);
        setResult(2, intent);
        finish();
    }
}


