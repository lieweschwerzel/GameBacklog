package com.example.gamebacklog.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.gamebacklog.R;
import com.example.gamebacklog.model.GameBacklog;
import com.example.gamebacklog.model.GameBacklogAdapter;
import com.example.gamebacklog.model.MainViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements GameBacklogAdapter.OnItemClickListener {

    //instance variables
    public static final int EDIT_REQUEST = 252;
    private List<GameBacklog> mGameBacklogs;
    private GameBacklogAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MainViewModel mMainViewModel;

//    private GestureDetector mGestureDetector;
    //Constants used when calling the update activity
    public static final String EXTRA_GAMEBACKLOG = "Gamebacklog";
    public static final int REQUESTCODE = 1234;
    private int mModifyPosition;

//    private GameBacklogRoomDatabase db;
//    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        db = GameBacklogRoomDatabase.getDatabase(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize the instance variables
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mGameBacklogs = new ArrayList<>();

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getmGameBacklogs().observe(this, new Observer<List<GameBacklog>>() {
            @Override
            public void onChanged(@Nullable List<GameBacklog> gameBacklogs) {
                mGameBacklogs = gameBacklogs;
                updateUI();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

//        mRecyclerView.setBackgroundColor(ContextCompat.getColor(this, R.color.design_default_color_primary_dark));

//
//        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return true;
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
//                startActivityForResult(intent, ADD_LOG_REQUEST);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mMainViewModel.delete(mAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "GameBacklog deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new GameBacklogAdapter(mGameBacklogs, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mGameBacklogs);
            //dataset changed
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
        if (id == R.id.action_remove) {
            deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteAll() {
        for (int i = 0; i<mGameBacklogs.size(); i++ ){
            GameBacklog deleteGameBacklog = mGameBacklogs.get(i);
            mMainViewModel.delete(deleteGameBacklog);
        }
    }


    @Override
    public void onItemClick(int position) {
        GameBacklog gameBacklog = mGameBacklogs.get(position);
//        Toast.makeText(this,gameBacklog.getTitle(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(MainActivity.EXTRA_GAMEBACKLOG, gameBacklog);
        startActivityForResult(intent, EDIT_REQUEST);
    }
}
