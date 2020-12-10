package com.abra.homework_four_dot_one;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ContactItemAdapter contactItemAdapter;
    FloatingActionButton button;
    SearchView searchView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBarSearch);
        setSupportActionBar(toolbar);
        button = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        contactItemAdapter = ContactItemAdapter.getContactItemAdapter();
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        recyclerView.setAdapter(contactItemAdapter);
        // почему-то работает наоборот, я так и не понял почему
        if (rotation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactItemAdapter.getFilter().filter(newText);
                return false;
            }
        });
        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            startActivity(intent);
        });
    }

}