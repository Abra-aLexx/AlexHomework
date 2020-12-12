package com.abra.homework_four_dot_one;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactItemAdapter contactItemAdapter;
    private FloatingActionButton button;
    private SearchView searchView;
    private Toolbar toolbar;
    private static final int ACTIVITY_ADD = 1;
    private static final int ACTIVITY_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBarSearch);
        setSupportActionBar(toolbar);
        button = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        if (savedInstanceState != null) {
            contactItemAdapter = new ContactItemAdapter(this, savedInstanceState.getParcelableArrayList("list"));
        } else {
            contactItemAdapter = new ContactItemAdapter(this, new ArrayList<>());
        }
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
            startActivityForResult(intent, ACTIVITY_ADD);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ContactItem contactItem;
            if (requestCode == ACTIVITY_ADD) {
                contactItem = data.getParcelableExtra("contactItem");
                if (!data.getBooleanExtra("flag", false) && contactItem != null) {
                    contactItemAdapter.add(contactItem);
                }
            } else if (requestCode == ACTIVITY_EDIT) {
                if (!data.getBooleanExtra("isRemoved", false)) {
                    contactItem = data.getParcelableExtra("contactItem");
                    int position = data.getIntExtra("position", 0);
                    if (contactItem != null) contactItemAdapter.edit(position, contactItem);
                } else {
                    contactItemAdapter.remove(data.getIntExtra("position", 0));
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (contactItemAdapter.getContactItemList().size() != 0) {
            outState.putParcelableArrayList("list", contactItemAdapter.getContactItemList());
        } else {
            outState.putParcelableArrayList("list", new ArrayList<>());
        }
    }
}