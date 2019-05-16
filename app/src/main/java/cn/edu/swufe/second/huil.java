package cn.edu.swufe.second;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class huil extends AppCompatActivity implements Runnable{
    TextView show;
    EditText rmb;
    Handler handler;
    private float doller_rate = 0.1f;
    private float euro_rate = 0.2f;
    private float won_rate = 0.3f;
    private final String TAG = "Rate";
    private String updatadata = "";
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huil);
        rmb = (EditText) findViewById(R.id.inp);
        show = (TextView) findViewById(R.id.out);
        SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        doller_rate =  sharedPreferences.getFloat("key doller",0.0f);
        euro_rate =  sharedPreferences.getFloat("key euro",0.0f);
        won_rate =  sharedPreferences.getFloat("key won",0.0f);
        updatadata = sharedPreferences.getString("updata_data","");

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);
        Log.i(TAG,"updatadata:"+updatadata);
        Log.i(TAG,"todayStr:"+todayStr);
        if(!todayStr.equals(updatadata)){
            Log.i(TAG,"todayStr:"+todayStr);
            Thread t = new Thread(this);
            t.start();
        }else{
            Log.i(TAG,"oncreate:不需要更新");
        }


         handler = new Handler(){
            public void handleMessage(Message msg1) {
                if (msg1.what==4)
                {
                    Bundle bd1= (Bundle) msg1.obj;
                    doller_rate = bd1.getFloat("dollar_rate");
                    euro_rate = bd1.getFloat("euro_rate");
                    won_rate = bd1.getFloat("won_rate");
                    Log.i(TAG,"handlermessage:doller"+doller_rate);
                    Log.i(TAG,"handlermessage:euro"+euro_rate);
                    Log.i(TAG,"handlermessage:won"+won_rate);

                    SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("updata_data",todayStr);
                    editor.putFloat("key dollar",doller_rate);
                    editor.putFloat("key euro",euro_rate);
                    editor.putFloat("key won",won_rate);
                    editor.apply();

                    Toast.makeText(huil.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg1);
            }
        };


    }
    public void onClick(View btn) {
    String str = rmb.getText().toString();
    float r = 0;
    if(str.length()>0){
        r = Float.parseFloat(str);
    }else{
        Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
    }
    Log.i(TAG,"onClick:r"+r);
    if(btn.getId()==R.id.dooller){
        show.setText(String.format("%.2f",r*doller_rate));
    }
    else if(btn.getId()==R.id.euro){
        show.setText(String.format("%.2f",r*euro_rate));
    }
    else if(btn.getId()==R.id.won)
        {
            show.setText(String.format("%.2f",r*won_rate));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_set) {
            Intent config = new Intent(this, config.class);
            config.putExtra("dollar_rate_key", doller_rate);
            config.putExtra("euro_rate_key", euro_rate);
            config.putExtra("won_rate_key", won_rate);

            Log.i(TAG, "openOne:dollar_rate=" + doller_rate);
            Log.i(TAG, "openOne:euro_rate=" + euro_rate);
            Log.i(TAG, "openOne:won_rate=" + won_rate);
            startActivityForResult(config, 1);
        }else if(item.getItemId()==R.id.open_list){
            Intent list= new Intent(this,Mylist2.class);

            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    public void openOne(View btn){
        openConfig();
    }
    public void openConfig(){
        Intent config= new Intent(this,config.class);
        config.putExtra("dollar_rate_key",doller_rate);
        config.putExtra("euro_rate_key",euro_rate);
        config.putExtra("won_rate_key",won_rate);

        Log.i(TAG,"openOne:dollar_rate="+doller_rate);
        Log.i(TAG,"openOne:euro_rate="+euro_rate);
        Log.i(TAG,"openOne:won_rate="+won_rate);
        startActivityForResult(config,1);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==2){
            Bundle bul = data.getExtras();
            doller_rate = bul.getFloat("key dollar",0.1f);
            euro_rate = bul.getFloat("key euro",0.2f);
            won_rate = bul.getFloat("key won",0.3f);
            Log.i(TAG,"onActivityResult:dollarRate="+doller_rate);
            Log.i(TAG,"onActivityResult:euroRate="+euro_rate);
            Log.i(TAG,"onActivityResult:wonRate="+won_rate);
            SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("key dollar",doller_rate);
            editor.putFloat("key euro",euro_rate);
            editor.putFloat("key won",won_rate);
            editor.apply();
            Log.i(TAG,"onActivityResult:数据已保存");
        }
    }

    @Override
    public void run() {
        Log.i(TAG,"run:start");
        /*try {
            Thread.sleep(500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run:i=" + 2);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run:i=" + 3);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run:i=" + 4);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run:i=" + 5);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
       /* Message msg = handler.obtainMessage();
           msg.what = 5;
           msg.obj  = "hello";
           handler.sendMessage(msg);

      /* try {
            URL url = new URL("http://www.usd-cny.com/icbc.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();
            String html = InputStream2String(in);
            Log.i(TAG, "run: html=" + html);
            Document doc = Jsoup.parse(html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } */

        Bundle bundle = new Bundle();
        getfromBOC(bundle);
        Message msg1 = handler.obtainMessage();
        msg1.what = 4;
        msg1.obj  = bundle;
        handler.sendMessage(msg1);
    }
    private void getfromBOC7(Bundle bundle) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            //doc = Jsoup.parse(html);
            Log.i(TAG,"run:"+doc.title());
            Elements tables =  doc.getElementsByTag("table");
            int i = 1;
            for(Element table :tables){
                Log.i(TAG, "run:table["+i+"]=" + tables);
                i++;
            }
            Element table2 = tables.get(1);
            Log.i(TAG,"run:table1="+table2);
            Elements tds = table2.getElementsByTag("td");
            for( i = 0;i<tds.size();i+=8){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                Log.i(TAG, "run:" + td1.text()+"==="+td2.text());
                String str1 = td1.text();
                String val = td2.text();
                if("美元".equals(td1.text())){
                    bundle.putFloat("dollar_rate",100f/Float.parseFloat(td2.text()));
                }
                if("韩元".equals(td1.text())){
                    bundle.putFloat("won_rate",100f/Float.parseFloat(td2.text()));
                }
                if("欧元".equals(td1.text())){
                    bundle.putFloat("euro_rate",100f/Float.parseFloat(td2.text()));
                }
            }
            /*for(Element td : tds){
                Log.i(TAG, "run:td=" + td);
                Log.i(TAG, "run:test=" + td.text());
                Log.i(TAG, "run:html=" + td.html());
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getfromBOC(Bundle bundle) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc = Jsoup.parse(html);
            Log.i(TAG,"run:"+doc.title());
            Elements tables =  doc.getElementsByTag("table");
            int i = 1;
            for(Element table :tables){
                Log.i(TAG, "run:table["+i+"]=" + tables);
                i++;
            }
            Element table1 = tables.get(0);
            Log.i(TAG,"run:table1="+table1);
            Elements tds = table1.getElementsByTag("td");
            for( i = 0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                Log.i(TAG, "run:" + td1.text()+"==="+td2.text());
                String str1 = td1.text();
                String val = td2.text();
                if("美元".equals(td1.text())){
                            bundle.putFloat("dollar_rate",100f/Float.parseFloat(td2.text()));
                }
                if("韩元".equals(td1.text())){
                    bundle.putFloat("won_rate",100f/Float.parseFloat(td2.text()));
                }
                if("欧元".equals(td1.text())){
                    bundle.putFloat("euro_rate",100f/Float.parseFloat(td2.text()));
                }
            }
            /*for(Element td : tds){
                Log.i(TAG, "run:td=" + td);
                Log.i(TAG, "run:test=" + td.text());
                Log.i(TAG, "run:html=" + td.html());
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String InputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize =1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        for(;;){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz<0){
                break;
            }
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}

