package com.example.gamebacklog.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

public class EditActivity extends AppCompatActivity {
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
        setTitle("Edit Game");

        Intent intent = getIntent();
        final GameBacklog gameBacklog = intent.getParcelableExtra(MainActivity.EXTRA_GAMEBACKLOG);
        mGameTitle.setText(gameBacklog.getTitle());
        mGamePlatform.setText(gameBacklog.getPlatform());
//        mGameStatus.setSelection(dataAdapter.getPosition(gameBacklog.getStatus()));

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        FloatingActionButton savebutton = findViewById(R.id.fabsave);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mGameTitle.getText().toString();
                String platform = mGamePlatform.getText().toString();
                String status = mGameStatus.getSelectedItem().toString();

                Calendar cal = Calendar.getInstance();
                Date tempdate = cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = dateFormat.format(tempdate);
                //Check if some text has been added
                if (!(TextUtils.isEmpty(title.trim())) && !(TextUtils.isEmpty(platform.trim()))) {
                    GameBacklog editGameBacklog = new GameBacklog(title, platform, status, date);
                    int id = gameBacklog.getId();
                    editGameBacklog.setId(id);
                    mMainViewModel.update(editGameBacklog);
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                } else
                    Snackbar.make(view, "Please insert a title and platform", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {
        mGameStatus = (Spinner) findViewById(R.id.editStatus_addedit);
        List<String> list = new ArrayList<String>();
//        list.add(" ");
        list.add("Want to Play");
        list.add("Playing");
        list.add("Stalled");
        list.add("Dropped");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGameStatus.setAdapter(dataAdapter);
    }

}
