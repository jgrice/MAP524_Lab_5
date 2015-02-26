package com.example.student.mylist;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    final static File sdCard = Environment.getExternalStorageDirectory();
    final static File file = new File(sdCard.getAbsolutePath(), "colorFile");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] colourNames;
        final String[] colourValues;
        colourNames = getResources().getStringArray(R.array.listArray);
        colourValues = getResources().getStringArray(R.array.listValues);
        ListView lv = (ListView) findViewById(R.id.listView);

        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_listview, colourNames);
        lv.setAdapter(aa);

        final View root = lv.getRootView();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String text = br.readLine();
            if (text != null) {
                root.setBackgroundColor(Color.parseColor("#" +text.substring(2)));
            }
            System.out.println("Reading:" + text);

        } catch (IOException e) {
            e.printStackTrace();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                root.setBackgroundColor(Color.parseColor("#" + colourValues[position].substring(2)));

            }
        });

        registerForContextMenu(lv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Write Colour to SDCard");
        menu.add(0, v.getId(), 0, "Read Colour from SDCard");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getTitle() == "Write Colour to SDCard") {
            try {
                FileWriter fw = new FileWriter(file);

                String[] colourValues = getResources().getStringArray(R.array.listValues);

                System.out.println("Writing: " + colourValues[info.position]);
                fw.write(colourValues[info.position]);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Writing Colour to SDCard", Toast.LENGTH_LONG).show();

        } else if (item.getTitle() == "Read Colour from SDCard") {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String text = br.readLine();

                System.out.println("Reading:" + text);
                info.targetView.getRootView().setBackgroundColor(Color.parseColor("#" +text.substring(2)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Reading Colour from SDCard", Toast.LENGTH_LONG).show();
        } else {
            return false;
        }
        return true;
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
