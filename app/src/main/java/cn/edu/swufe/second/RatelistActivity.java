package cn.edu.swufe.second;

import android.app.ListActivity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RatelistActivity extends ListActivity implements Runnable{
    String data[] = {"wait..........."};
    Handler handler;
    private final String TAG = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ratelist);   不能用，再父类ListActivity时。
        List<String> list1 = new ArrayList<String>();
        for (int i = 1; i < 100; i++) {
            list1.add("item" + i);
        }
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter);
        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==9) {
                List<String> list2 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RatelistActivity.this, android.R.layout.simple_list_item_1, list2);
                    setListAdapter(adapter);
                }
                    super.handleMessage(msg);
            }
        };
    }
    @Override
    public void run() {
                      //获取数据放入List
        List<String> retList = new ArrayList<String>();
        Document doc = null;
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                retList.add("run:" + td1.text()+"==="+td2.text());

            }
            /*for(Element td : tds){
                Log.i(TAG, "run:td=" + td);
                Log.i(TAG, "run:test=" + td.text());
                Log.i(TAG, "run:html=" + td.html());
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg2 = handler.obtainMessage(9);
        msg2.obj  = retList;
        handler.sendMessage(msg2);
    }
}
