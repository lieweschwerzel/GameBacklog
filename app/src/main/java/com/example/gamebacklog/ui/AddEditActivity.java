package com.example.gamebacklog.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gamebacklog.R;
import com.example.gamebacklog.model.GameBacklog;
import com.example.gamebacklog.model.GameBacklogAdapter;
import com.example.gamebacklog.model.MainViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEditActivity extends AppCompatActivity  {
    public static final String EXTRA_ID =
            "com.codinginflow.architectureexample.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.codinginflow.architectureexample.EXTRA_TITLE";
    public static final String EXTRA_PLATFORM =
            "com.codinginflow.architectureexample.EXTRA_EXTRA_PLATFORM";
    public static final String EXTRA_STATUS =
            "com.codinginflow.architectureexample.EXTRA_STATUS";
    public static final String EXTRA_DATE =
            "com.codinginflow.architectureexample.EXTRA_DATE";


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

        addItemsOnSpinner2();

//        addListenerOnSpinnerItemSelection();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Game");
            mGameTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            mGamePlatform.setText(intent.getStringExtra(EXTRA_PLATFORM));
//            mGameStatus.setAdapter(intent.getIntExtra(EXTRA_STATUS, 1));
        } else {
            setTitle("Create Game");
        }
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Initialize the instance variables
        mGameTitle = findViewById(R.id.editTitle_addedit);
        mGamePlatform = findViewById(R.id.editPlatform_addedit);
        mGameStatus = findViewById(R.id.editStatus_addedit);
        mGameBacklogs = new ArrayList<>();

        FloatingActionButton savebutton = findViewById(R.id.fabsave);

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mGameTitle.getText().toString();
                String platform = mGamePlatform.getText().toString();
                String status = String.valueOf(mGameStatus.getSelectedItem());

                Calendar cal = Calendar.getInstance();
                Date tempdate=cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date=dateFormat.format(tempdate);
                //Check if some text has been added
                if (!(TextUtils.isEmpty(title)) && !(TextUtils.isEmpty(platform))) {
                    GameBacklog newGameBacklog = new GameBacklog(title, platform, status, date);
                  mMainViewModel.insert(newGameBacklog);
                    Intent intent = new Intent(AddEditActivity.this, MainActivity.class);
                    startActivity(intent);
                } else
                    Snackbar.make(view, "Please insert a title and platform", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        mGameStatus = (Spinner) findViewById(R.id.editStatus_addedit);
        List<String> list = new ArrayList<String>();
        list.add("Want to Play");
        list.add("Playing");
        list.add("Stalled");
        list.add("Dropped");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGameStatus.setAdapter(dataAdapter);
    }

//    private void saveGameBacklog() {
//        String title = mGameTitle.getText().toString();
//        String platform = mGamePlatform.getText().toString();
//        String status = mGameStatus.getSelectedItem().toString();
//
//        if (title.trim().isEmpty() || platform.trim().isEmpty()) {
//            Toast.makeText(this, "Please insert a title and platform", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Intent data = new Intent();
//        data.putExtra(EXTRA_TITLE, title);
//        data.putExtra(EXTRA_PLATFORM, platform);
//        data.putExtra(EXTRA_STATUS, status);
//
//        int id = getIntent().getIntExtra(EXTRA_ID, -1);
//        if (id != -1) {
//            data.putExtra(EXTRA_ID, id);
//        }
//
//        setResult(RESULT_OK, data);
//        finish();
//    }

//    // get the selected dropdown list value
//    public void addListenerOnButton() {
//
//        mGameStatus = (Spinner) findViewById(R.id.editStatus_addedit);
//        savebutton = (FloatingActionButton) findViewById(R.id.fabsave);
//
//        savebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(AddEditActivity.this,
//                        "OnClickListener : " +
//                                "\nSpinner 2 : "+ String.valueOf(mGameStatus.getSelectedItem()),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}
