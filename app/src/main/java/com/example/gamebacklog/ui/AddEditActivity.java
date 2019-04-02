package com.example.gamebacklog.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebacklog.R;
import com.example.gamebacklog.model.GameBacklog;
import com.example.gamebacklog.model.GameBacklogAdapter;
import com.example.gamebacklog.model.MainViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEditActivity extends AppCompatActivity {
    //instance variables
    private List<GameBacklog> mGameBacklogs;
    private GameBacklogAdapter mAdapter;
    private MainViewModel mMainViewModel;

    private EditText mGameTitle;
    private EditText mGamePlatform;
    private Spinner mGameStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit);
        //Initialize the instance variables
        mGameTitle = findViewById(R.id.editTitle_addedit);
        mGamePlatform = findViewById(R.id.editPlatform_addedit);
        mGameStatus = findViewById(R.id.editStatus_addedit);
        mGameBacklogs = new ArrayList<>();

        addItemsOnSpinner();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final GameBacklog tmpGameBacklog = intent.getParcelableExtra(MainActivity.EXTRA_GAMEBACKLOG);
        if (tmpGameBacklog != null) {
            setTitle("Edit Game");
            mGameTitle.setText(tmpGameBacklog.getTitle());
            mGamePlatform.setText(tmpGameBacklog.getPlatform());
            //Spinner instellen, krijg positie van status in de lijst met spinner elementen
            String tmpstatus = tmpGameBacklog.getStatus();
            mGameStatus.setSelection(Integer.valueOf(getmGameStatusPos(tmpstatus)));
        } else
            setTitle("Create Game");

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        FloatingActionButton savebutton = findViewById(R.id.fabsave);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mGameTitle.getText().toString();
                String platform = mGamePlatform.getText().toString();
                String status = mGameStatus.getSelectedItem().toString();
                String date = getDate();

                //Check if some text has been added
                if (!(TextUtils.isEmpty(title.trim())) && !(TextUtils.isEmpty(platform.trim())) && status != "Select a status...") {
                    if (tmpGameBacklog != null) {
                        GameBacklog editGameBacklog = new GameBacklog(title, platform, status, date);
                        int id = tmpGameBacklog.getId();
                        editGameBacklog.setId(id);
                        mMainViewModel.update(editGameBacklog);
                        Intent intent = new Intent(AddEditActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        GameBacklog newGameBacklog = new GameBacklog(title, platform, status, date);
                        mMainViewModel.insert(newGameBacklog);
                        Intent intent = new Intent(AddEditActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else
                    Snackbar.make(view, "Please insert a title and platform", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }
    // add items into spinner dynamically
    public void addItemsOnSpinner() {
        mGameStatus = (Spinner) findViewById(R.id.editStatus_addedit);
        List<String> list = new ArrayList<String>();
        list.add("Select a status...");
        list.add("Want to Play");
        list.add("Playing");
        list.add("Stalled");
        list.add("Dropped");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list){
            //grijs maken van de voorselectie op die spinner nadat erop is geklikt
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGameStatus.setAdapter(dataAdapter);

    }

    // get position of existing Status in spinner
    public int getmGameStatusPos(String status) {
        List<String> list = new ArrayList<>();
        list.add("Select a status...");
        list.add("Want to Play");
        list.add("Playing");
        list.add("Stalled");
        list.add("Dropped");
        return list.indexOf(status);
    }

    //get date and format it
    public String getDate() {
        Calendar cal = Calendar.getInstance();
        Date tempdate = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(tempdate);
        return date;
    }

}
