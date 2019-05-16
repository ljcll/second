package cn.edu.swufe.second;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Mylist2 extends ListActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    Handler handler;
    private List<HashMap<String, String>> listItems;
    private SimpleAdapter listItemAdapter;
    private final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initListView();
       // MyAdapter myAdapter = new MyAdapter(this, R.layout.list_item, listItems);

        this.setListAdapter(listItemAdapter);
        Thread t = new Thread(this);
        t.start();
    }

    @SuppressLint("HandlerLeak")
    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate:  " + i);
            map.put("ItemDetail", "" + i);
            listItems.add(map);

        }
        listItemAdapter = new SimpleAdapter(this, listItems,
                R.layout.list_item,
                new String[]{"ItemTitle", "ItemDetail"},
                new int[]{R.id.list_item, R.id.ItemDetail});
        handler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what==7)
                {
                    listItems = (List<HashMap<String,String>> ) msg.obj;
                    listItemAdapter = new SimpleAdapter(Mylist2.this,listItems,
                            R.layout.list_item,
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.list_item, R.id.ItemDetail});
                            setListAdapter(listItemAdapter) ;
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    public void run() {
        List<HashMap<String, String>> retList = new ArrayList<HashMap<String, String>>();
        Document doc = null;
        Log.i(TAG, "run:i=" + 1);
        try {
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG, "run:" + doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element table2 = tables.get(1);
            Elements tds = table2.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i=i+8) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                String str1 = td1.text();
                String val = td2.text();

                Log.i(TAG, "run" + str1 + "==" + val);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", str1);
                map.put("ItemDetail", val);
                retList.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG ,"onItemClick:parent=" + parent);
        Log.i(TAG,"onItemClick:view="+view);
        Log.i(TAG,"onItemClick:position="+position);
        Log.i(TAG,"onItemClick:id="+id);
        HashMap<String, String> map = (HashMap<String, String> ) getListView().getItemAtPosition(position);
        String titlestr = map.get("ItemTitle");
        String detailstr = map.get("ItemDetail");
        Log.i(TAG,"onItemClick:title="+titlestr);
        Log.i(TAG,"onItemClick:detail="+detailstr);

        TextView title = view.findViewById(R.id.ItemTitle);
        TextView detail = view.findViewById(R.id.ItemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG,"onItemClick:detail2="+detail2);
        Log.i(TAG,"onItemClick:title2="+title2);

        Intent rateCalc =new Intent(this,cn.edu.swufe.second.RateCalc.class);
        rateCalc.putExtra("title",titlestr);
        rateCalc.putExtra("rate",Float.parseFloat(detailstr));
        startActivity(rateCalc);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        //listItems.remove(position);
        //listItemAdapter.notifyDataSetChanged();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listItems.remove(position);
                listItemAdapter.notifyDataSetChanged();
            }
        })
                .setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}