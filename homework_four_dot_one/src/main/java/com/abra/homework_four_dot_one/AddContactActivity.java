package com.abra.homework_four_dot_one;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {
    private EditText name, info;
    private RadioGroup group;
    private ImageButton imgBack, imgAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = findViewById(R.id.toolBarAdd);
        setSupportActionBar(toolbar);
        imgBack = findViewById(R.id.imgButtonBack1);
        imgAdd = findViewById(R.id.imgButtonApply);
        name = findViewById(R.id.editTextName);
        info = findViewById(R.id.editTextInfo);
        group = findViewById(R.id.radioGroup);
        setListeners();
    }

    @SuppressLint("NonConstantResourceId")
    private void setListeners() {
        group.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radioButtonPhone: {
                    info.setText("");
                    info.setHint(getString(R.string.phone_number));
                    info.setInputType(InputType.TYPE_CLASS_PHONE);
                    break;
                }
                case R.id.radioButtonEmail: {
                    info.setText("");
                    info.setHint(getString(R.string.email));
                    info.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    break;
                }
            }
        });
        imgBack.setOnClickListener(view -> {
            showActivity(null, true);
        });
        imgAdd.setOnClickListener(view -> {
            int iconId = 0;
            String typeInfo = null;
            switch (group.getCheckedRadioButtonId()) {
                case R.id.radioButtonPhone: {
                    iconId = R.drawable.icon_contact;
                    typeInfo = "phone";
                    break;
                }
                case R.id.radioButtonEmail: {
                    iconId = R.drawable.icon_email;
                    typeInfo = "email";
                    break;
                }
            }
            String textName = name.getText().toString();
            String textInfo = info.getText().toString();
            if (!textName.equals("") && !textInfo.equals("")) {
                showActivity(new ContactItem(iconId, textName, textInfo, typeInfo), false);
            } else {
                Toast.makeText(this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showActivity(ContactItem contactItem, boolean flag) {
        Intent intent = new Intent();
        if (!flag) intent.putExtra("contactItem", contactItem);
        intent.putExtra("flag", flag);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}