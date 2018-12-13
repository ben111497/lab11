package com.example.user.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener{
    ListView l1;
    Button b1,b2,b3,b4;
    EditText e1,e2;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    private SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);
        b1=findViewById(R.id.b1);
        b2=findViewById(R.id.b2);
        b3=findViewById(R.id.b3);
        b4=findViewById(R.id.b4);
        l1=findViewById(R.id.l1);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, items);
        l1.setAdapter(adapter);
        dbrw = new MyDBHelper(this).getWritableDatabase();
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        dbrw.close();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b1:
                Cursor c;
                if (e1.length() < 1) c = dbrw.rawQuery("SELECT * FROM myTable", null);
                else
                    c = dbrw.rawQuery("SELECT * FROM myTable WHERE book LIKE '" + e1.getText().toString() + "'", null);
                c.moveToFirst();
                items.clear();
                Toast.makeText(MainActivity.this, "共有" + c.getCount() + "筆資料", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < c.getCount(); i++) {
                    items.add("書名:" + c.getString(0) + "\t\t\t\t 價格:" + c.getString(1));
                    c.moveToNext();
                }
                adapter.notifyDataSetChanged();
                c.close();
                break;

            case R.id.b2:
                if (e1.length() < 1 || e2.length() < 1)
                    Toast.makeText(MainActivity.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        dbrw.execSQL("INSERT INTO myTable(book, price)VALUES(?,?)", new Object[]{e1.getText().toString(), e2.getText().toString()});
                        Toast.makeText(MainActivity.this, "新增書名" + e1.getText().toString() + " 價格" + e2.getText().toString(), Toast.LENGTH_SHORT).show();
                        e1.setText("");
                        e2.setText("");
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "新增失敗:" +
                                e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.b3:
                if (e1.length() < 1 || e2.length() < 1)
                    Toast.makeText(MainActivity.this, "欄位請勿留空", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        dbrw.execSQL("UPDATE myTable SET price = " + e2.getText().toString() + " WHERE book LIKE '" + e1.getText().toString() + "'");
                        Toast.makeText(MainActivity.this, "更新書名" + e1.getText().toString() + " 價格" + e2.getText().toString(), Toast.LENGTH_SHORT).show();
                        e1.setText("");
                        e2.setText("");
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "更新失敗:" + e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

                break;

            case R.id.b4:
                if (e1.length() < 1)
                    Toast.makeText(MainActivity.this, "書名請勿留空", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '" + e1.getText().toString() + "'");
                        Toast.makeText(MainActivity.this, "刪除書名" + e1.getText().toString(), Toast.LENGTH_SHORT).show();
                        e1.setText("");
                        e2.setText("");
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "刪除失敗:" +
                                e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}


