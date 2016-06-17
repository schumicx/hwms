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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.xyt.hwms.R;
import com.xyt.hwms.bean.BaseBean;
import com.xyt.hwms.bean.CardSyncBean;
import com.xyt.hwms.bean.LoginBean;
import com.xyt.hwms.bean.User;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.BaseUtils;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.GsonObjectRequest;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @NotEmpty(message = "请输入账号")
    EditText account;
    @BindView(R.id.password)
    @Password(min = 1, message = "请输入密码")
    EditText password;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.account_login_form)
    LinearLayout accountLoginForm;
    @BindView(R.id.login_form)
    ScrollView loginForm;

    @OnClick(R.id.sign_in_button)
    public void onClick(View view) {
        validator.validate();
//        startActivity(new Intent(context, MainActivity.class));
//        finish();
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

        cardSyncRequest();
        /*if (STATE_ISDECODING == false) {
            Constants.decode_start = SystemClock.elapsedRealtime();
            ApplicationController.getInstance().doDecode();
            STATE_ISDECODING = true;
        }*/
    }

    @Override
    public void onValidationSucceeded() {
        loginRequest();
    }

    @Override
    public void getTagId(String data) {
        if (!switchLogin.isChecked()) {
            nfcLogin(data);
        } else {
            Toast.makeText(context, "请切换刷卡登录!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBarcode(String data) {
    }

    @Override
    public void closeDialog() {
    }

    private void nfcLogin(String data) {
        List<User> userList = new Gson().fromJson(PreferencesUtils.getString(context, "cards"), CardSyncBean.class).getData();
        for (User user : userList) {
            if (data.equals(user.getCard_id())) {
                startActivity(new Intent(context, MainActivity.class));
                PreferencesUtils.putString(context, "user", new Gson().toJson(user));
                finish();
                return;
            }
        }
        Toast.makeText(context, "卡登录失败,请联系管理员!", Toast.LENGTH_SHORT).show();
    }

    //登录接口
    private void loginRequest() {
        loginForm.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
        String url = Constants.SERVER + "mobile/login";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", account.getText().toString());
        params.put("password", password.getText().toString());
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.POST, url, LoginBean.class, new Gson().toJson(params), new Response.Listener<LoginBean>() {
                    @Override
                    public void onResponse(LoginBean response) {
                        startActivity(new Intent(context, MainActivity.class));
                        PreferencesUtils.putString(context, "user", new Gson().toJson(response.getData()));
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginForm.setVisibility(View.VISIBLE);
                        loginProgress.setVisibility(View.GONE);
                        try {
                            Toast.makeText(context, new Gson().fromJson(new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers)), BaseBean.class).getContent(), Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            if (!BaseUtils.isNetworkConnected(context)) {
                                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        error.printStackTrace();
                    }
                }), getLocalClassName());
    }

    //卡同步接口
    private void cardSyncRequest() {
        String url = Constants.SERVER + "mobile/card/sync";
        ApplicationController.getInstance().addToRequestQueue(
                new GsonObjectRequest<>(Request.Method.GET, url, CardSyncBean.class, null, new Response.Listener<CardSyncBean>() {
                    @Override
                    public void onResponse(CardSyncBean response) {
                        PreferencesUtils.putString(context, "cards", new Gson().toJson(response));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            Toast.makeText(context, new Gson().fromJson(new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers)), BaseBean.class).getContent(), Toast.LENGTH_SHORT).show();
                        } catch (NullPointerException e) {
                            if (!BaseUtils.isNetworkConnected(context)) {
                                Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        error.printStackTrace();
                    }
                }), getLocalClassName());
    }
}

