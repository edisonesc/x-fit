package com.example.edison.x_fit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class AccountSettingActivity extends AppCompatActivity {
        LinearLayout mPasswordSetting, mEmailSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        mPasswordSetting = findViewById(R.id.editPassword);
        mEmailSetting = findViewById(R.id.editEmail);
    }
}
