package com.example.mdk_17_plotnikov_pr_21_102;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    final String table_name = "mytable2ads";

    String video_name[] = {
            "rickroll",
            "bad pigs",
            "killed n...",
            "night witches",
            "rush E",
            "delta alpha alpha '",
            "polish toilet"
    };
    String video_link[] = {
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            "https://www.youtube.com/watch?v=xjrOLLeMQx4&list=RDxjrOLLeMQx4&start_radio=1&rv=xjrOLLeMQx4&t=5",
            "https://www.youtube.com/watch?v=bIcKKa2bVvg&list=RDMM&index=23",
            "https://www.youtube.com/watch?v=jcemHIqmkYI&list=RDMM&index=40",
            "https://www.youtube.com/watch?v=Qskm9MTz2V4&list=RDMM&index=27",
            "https://www.youtube.com/watch?v=PapnBKnPA8s",
            "https://www.youtube.com/watch?v=F1l8W2Ncix8"

    };
    int likes[] = {
        10000000,
            34000,
            90000,
            120345,
            1000000,
            30000,
            56000
    };
    int dislikes[] = {
            1000000,
            1600,
            100000,
            100,
            90000,
            200,
            741
    };

    EditText etName, etLink, etLikes, etDislikes;

    RecyclerView list_output;
    DBHelper dbHelper;
    SQLiteDatabase db;

    /** Called when the activity is first created. */

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btnSort)).setOnClickListener(view -> onClickSort(view));
        ((Button) findViewById(R.id.btnDelete)).setOnClickListener(view -> onClickDelete(view));
        ((Button) findViewById(R.id.btnInsert)).setOnClickListener(view -> onClickInsert(view));
        ((Button) findViewById(R.id.btnUpdate)).setOnClickListener(view -> onClickUpdate(view));
        ((Button) findViewById(R.id.btnSelect)).setOnClickListener(view -> onClickSelect(view));

        etName = (EditText) findViewById(R.id.etName);
        etLink = (EditText) findViewById(R.id.etLink);
        etLikes = (EditText) findViewById(R.id.etLikes);
        etDislikes = (EditText) findViewById(R.id.etDislikes);


        dbHelper = new DBHelper(this);

        db = dbHelper.getWritableDatabase();

        Cursor c = db.query(table_name, null, null, null, null, null, null);
        if (c.getCount() == 0) {
            ContentValues cv = new ContentValues();

            List<VideoForAdapter> myDataset = new ArrayList<>();

            // заполним таблицу
            for (int i = 0; i <  video_link.length; i++) {

                cv.put("name", video_name[i]);
                cv.put("link", video_link[i]);
                cv.put("likes", likes[i]);
                cv.put("dislikes", dislikes[i]);

                Log.d(LOG_TAG, "id = " + db.insert(table_name, null, cv));

                VideoForAdapter vd = new VideoForAdapter(video_name[i], video_link[i], likes[i], dislikes[i]);
                myDataset.add(vd);
            }

            list_output.setAdapter(new AdapterVide(myDataset));
        }
        c.close();
        dbHelper.close();
    }

    private String
            d_name = "",
            d_link = "";
    private int d_like = 0,
            d_dislikes = 0;

    // переменные для query
    String[] columns = null;
    String selection = null;
    String[] selectionArgs = null;
    String groupBy = null;
    String having = null;
    String orderBy = null;
    Cursor c = null;

    private void base_onclick(View v) {
        db = dbHelper.getWritableDatabase();

        // данные с экрана
        d_name = etName.getText().toString();
        d_link = etLink.getText().toString();

        try {
            d_like = Integer.parseInt(etLikes.getText().toString());
            d_dislikes = Integer.parseInt(etDislikes.getText().toString());
        }
        catch (Exception e) {
            Log.d(LOG_TAG, e.getMessage());

            d_like = 0;
            d_dislikes = 0;

            etLikes.setText("0");
            etDislikes.setText("0");
        }

    }
    private void upd_by_cursor(Cursor c) {
        if(c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idName = c.getColumnIndex("name");
            int idLink = c.getColumnIndex("link");
            int idLikes = c.getColumnIndex("likes");
            int idDislikes = c.getColumnIndex("dislikes");

            List<VideoForAdapter> myDataset = new ArrayList<>();

            do{
                VideoForAdapter vd = new VideoForAdapter(c.getString(idName), c.getString(idLink),
                c.getInt(idLikes), c.getInt(idDislikes));

                myDataset.add(vd);

            } while(c.moveToNext());

            list_output.setAdapter(new AdapterVide(myDataset));

        } else
            Log.d(LOG_TAG, "0 rows");

    }

    int id_sort = 0;
    private void onClickInsert(View v) {
        base_onclick(v);

        ContentValues cv = new ContentValues();

        cv.put("name", d_name);
        cv.put("link", d_link);
        cv.put("likes", d_like);
        cv.put("dislikes", d_dislikes);

        Log.d(LOG_TAG, "id = " + db.insert(table_name, null, cv));
        dbHelper.close();
    }

    private void onClickUpdate(View v) {
        base_onclick(v);

        ContentValues cv = new ContentValues();

        cv.put("link", d_link);
        cv.put("likes", d_like);
        cv.put("dislikes", d_dislikes);

        db.update(table_name, cv, "name = ?", new String[]{d_name});
        db.close();

        dbHelper.close();
    }

    private void onClickDelete(View v) {
        base_onclick(v);

        Log.d(LOG_TAG, "");
        db.delete(table_name, "name = ?", new String[] {d_name});
        dbHelper.close();
    }

    private void onClickSort(View v) {
        base_onclick(v);


        Log.d(LOG_TAG, "Sorting");

        // сортировкапо
        switch (id_sort) {
            // наименование
            case 0:
                orderBy = "name";
                id_sort = 1;
                break;
            // население
            case 1:
                orderBy = "likes";
                id_sort = 2;
                break;
            case 2:
                orderBy = "dislikes";
                id_sort = 0;
                break;
        }
        c = db.query(table_name, null, null, null, null, null, orderBy);

        upd_by_cursor(c);

        dbHelper.close();
    }

    private void onClickSelect(View v) {
        base_onclick(v);

        Log.d(LOG_TAG, "--- Все записи ---");
        c = db.query(table_name, null, null, null, null, null, null);

        upd_by_cursor(c);

        dbHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструкторсуперкласса
            super(context, "myDB2", null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаемтаблицусполями
            db.execSQL("create table " + table_name + " ("
                    + "id integer primary key autoincrement," + "name text,"
                    + "link text," + "likes integer,"
                    + "dislikes integer" + ");");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(LOG_TAG, "Must be updated. on upgrade is empty");
        }
    }

}