package cn.edu.swufe.second;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
}
    public void onClick(View v) {
        EditText inp = findViewById(R.id.inp);
        String str = inp.getText().toString();
        float flo = Float.parseFloat(str);
        float flo1 = flo*9/5+32;
        TextView out = findViewById(R.id.out);
        out.setText("结果为："+flo1);
    }

};
