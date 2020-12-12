package com.abra.homework_four_dot_one;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditContactActivity extends AppCompatActivity {
    private EditText name, info;
    private Button btRemove;
    private ImageButton imageButtonBack;
    private ContactItem contactItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        Toolbar toolbar = findViewById(R.id.toolBarEdit);
        setSupportActionBar(toolbar);
        name = findViewById(R.id.editTextName1);
        info = findViewById(R.id.editTextInfo1);
        btRemove = findViewById(R.id.buttonRemove);
        imageButtonBack = findViewById(R.id.imgButtonBack);
        Intent intent = getIntent();
        if (intent != null) {
            int position = intent.getIntExtra("position", 0);
            contactItem = intent.getParcelableExtra("contactItem");
            switch (contactItem.getTypeInfo()) {
                case "phone": {
                    info.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                }
                case "email": {
                    info.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                }
            }
            setListeners(position);
            name.setText(contactItem.getName());
            info.setText(contactItem.getInfo());
        }

    }

    @SuppressLint("NonConstantResourceId")
    private void setListeners(int position) {
        imageButtonBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            int iconId = contactItem.getIconId();
            String typeInfo = contactItem.getTypeInfo();
            String textName = name.getText().toString();
            String textInfo = info.getText().toString();
            if (!textName.equals("") && !textInfo.equals("")) {
                intent.putExtra("contactItem", new ContactItem(iconId, textName, textInfo, typeInfo));
                intent.putExtra("position", position);
                intent.putExtra("isRemoved", false);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });
        btRemove.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("isRemoved", true);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}