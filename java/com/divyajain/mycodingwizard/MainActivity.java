package com.divyajain.mycodingwizard;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {

    static String tableName = "mytable";
    private SQLiteDatabase db;
    String text;
    EditText questionText;
    Button getQuesButton;
    TextView questionRetrieved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        questionText = (EditText) findViewById(R.id.QuestionEditText);
        Button submitButton = (Button) findViewById(R.id.submitButton);
        getQuesButton = (Button) findViewById(R.id.GetQuestionButton);
        questionRetrieved = (TextView) findViewById(R.id.getQuestion);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = questionText.getText().toString();
                insertIntoDB(text);

            }
        });

        getQuesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                  getQuestionFromDb();
//                Intent dbmanager = new Intent(getApplicationContext(),AndroidDatabaseManager.class);
//                startActivity(dbmanager);

            }
        });
    }

    public void  getQuestionFromDb()
    {
        System.out.println("Divya..");
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        db = mDbHelper.getReadableDatabase();
        String countQuery = "SELECT  * FROM mytable";
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        System.out.println("Divya.." + cnt);
        Random r = new Random();
        int i = r.nextInt(cnt)+1;
        System.out.println("Divya.." + i);
        Cursor c = db.rawQuery("SELECT id,question FROM mytable WHERE id = "+i, null);
        if(c.moveToFirst()){
            System.out.println("Divya..testing");
            do{
                //assing values
                String question = c.getString(1);
                System.out.println("Divya.." + question);
                if(question != null)
                questionRetrieved.setText(question);
                //Do something Here with values

            }while(c.moveToNext());
        }
        c.close();

    }
    public void insertIntoDB(String question) {

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        db = mDbHelper.getWritableDatabase();

        Cursor mCursor =

                db.query(tableName, new String[]{"question"}, "question" + "='" + question + "'", null,
                        null, null, null, null);

        if (mCursor != null && mCursor.moveToFirst()) {
            NoticeDialogFragment newFragment = new NoticeDialogFragment();
            newFragment.show(getFragmentManager(), "over-write");
        } else {
            ContentValues values = new ContentValues();
            values.put("question", question);
            db.insert(tableName, null, values);
            Toast t = Toast.makeText(getApplicationContext(), "Question Saved!", Toast.LENGTH_SHORT);
            t.setGravity(0, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL);
            t.show();
            questionText.setText("");


//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
