package cn.edu.swufe.second;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class secondActivity extends AppCompatActivity {
    TextView score;
    TextView score2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
         score = (TextView) findViewById(R.id.textView2);
        score2 = (TextView) findViewById(R.id.textView4);
    }

    public void btnadd1(View btn) {
        if(btn.getId()==R.id.btn)
        {showScore(1);}
        else{
            showScore2(1);
        }
    }
    public void btnadd2(View btn) {
        if(btn.getId()==R.id.btn1)
        {showScore(2);}
        else{
            showScore2(2);
        }
    }
    public void btnadd3(View btn) {
        if(btn.getId()==R.id.btn2)
        {showScore(3);}
        else{
            showScore2(3);
        }
    }
    public void btnreset(View btn) {
        score.setText("0");
        score2.setText("0");
    }
    public void showScore(int inc) {
           String oldscore = (String) score.getText();
           int newscore = Integer.parseInt(oldscore)+inc;
           score.setText(""+newscore);
    }
    public void showScore2(int inc) {
        String oldscore = (String) score2.getText();
        int newscore = Integer.parseInt(oldscore)+inc;
        score2.setText(""+newscore);
    }
}
