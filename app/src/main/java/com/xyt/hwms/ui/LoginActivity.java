package com.xyt.hwms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.xyt.hwms.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_progress)
    ProgressBar loginProgress;
    @BindView(R.id.switch_login)
    SwitchCompat switchLogin;
    @BindView(R.id.nfc_login_form)
    LinearLayout nfcLoginForm;
    @BindView(R.id.account)
    EditText account;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.account_login_form)
    LinearLayout accountLoginForm;
    @BindView(R.id.login_form)
    ScrollView loginForm;

    @OnClick(R.id.sign_in_button)
    public void onClick(View view) {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

    @OnCheckedChanged(R.id.switch_login)
    public void onChecked(boolean checked) {
        if (checked) {
            nfcLoginForm.setVisibility(View.GONE);
            accountLoginForm.setVisibility(View.VISIBLE);
        } else {
            nfcLoginForm.setVisibility(View.VISIBLE);
            accountLoginForm.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void getTagId(String data) {
        if (!switchLogin.isChecked()) {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        } else {
            Toast.makeText(context, "switch login!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBarcode(String data) {
    }

    @Override
    public void closeDialog() {
    }
}

