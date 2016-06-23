package com.xyt.hwms.ui;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.motorolasolutions.adc.decoder.BarCodeReader;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.ApplicationController;
import com.xyt.hwms.support.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BaseActivity extends AppCompatActivity implements Validator.ValidationListener {

    public boolean STATE_ISDECODING = false;
    protected Context context;
    protected String NFCTagId;
    protected String barCodeData;
    protected Validator validator;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;

    public abstract void getTagId(String data);

    public abstract void getBarcode(String data);

    public abstract void closeDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        validator = new Validator(this);
        validator.setValidationListener(this);

        resolveIntent(getIntent());

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(context, getText(R.string.no_nfc), Toast.LENGTH_SHORT).show();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }

        ApplicationController.getInstance().barCodeReader.setDecodeCallback(new BarCodeReader.DecodeCallback() {
            @Override
            public void onDecodeComplete(int symbology, int length, byte[] data, BarCodeReader reader) {
                if (ApplicationController.getInstance().state == Constants.STATE_DECODE) {
                    ApplicationController.getInstance().state = Constants.STATE_IDLE;
                }
                if (length > 0) {
                    Constants.decode_end = SystemClock.elapsedRealtime();
                    if (ApplicationController.getInstance().isAutoAim() == false) {
                        ApplicationController.getInstance().barCodeReader.stopDecode();
                    }
                    if (Constants.BEEP_MODE) {
                        ApplicationController.getInstance().beep();
                    }
                    if (symbology == 0x99) { //type 99?
                        int n = data[1];
                        int s = 2;
                        int d = 0;
                        int len = 0;
                        byte d99[] = new byte[data.length];
                        for (int i = 0; i < n; ++i) {
                            s += 2;
                            len = data[s++];
                            System.arraycopy(data, s, d99, d, len);
                            s += len;
                            d += len;
                        }
                        d99[d] = 0;
                        data = d99;
                    }
                    if (new String(data, 0, length).trim().startsWith(Constants.LABEL_LIB) || new String(data, 0, length).trim().startsWith(Constants.LABEL_LSL) || new String(data, 0, length).trim().startsWith(Constants.LABEL_CON) || new String(data, 0, length).trim().startsWith(Constants.LABEL_HW)) {
                        getBarcode(new String(data, 0, length).trim());
                    } else {
                        Toast.makeText(context, "请扫描固废系统定义的标签!", Toast.LENGTH_SHORT).show();
                    }
                }
                STATE_ISDECODING = false;
            }

            @Override
            public void onEvent(int event, int info, byte[] data, BarCodeReader reader) {

            }
        });
    }

    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

    private void showWirelessSettingsDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.nfc_disabled)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create().show();
    }

    public void showReasonDialog(int applyIndex, int position) {
        CacheReasonDialogFragment.newInstance(applyIndex, position).show(getSupportFragmentManager(), getLocalClassName());
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            getTagId(dumpTagData(tag).get("tagId"));
        }
    }

    private Map<String, String> dumpTagData(Parcelable p) {
        Map<String, String> map = new HashMap<>();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        map.put("tagId", getHex(id));

        return map;
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(b));
        }
//        for (byte aByte : bytes) {
//            int b = aByte & 0xff;
//            if (b < 0x10){
//                sb.append('0');
//            }
//            sb.append(Integer.toHexString(b));
//        }
        return sb.toString();
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F5:
                if (STATE_ISDECODING == false) {
                    Constants.decode_start = SystemClock.elapsedRealtime();
                    ApplicationController.getInstance().doDecode();
                    STATE_ISDECODING = true;
                }
                break;
            case KeyEvent.KEYCODE_F4:
                if (STATE_ISDECODING == false) {
                    Constants.decode_start = SystemClock.elapsedRealtime();
                    ApplicationController.getInstance().doDecode();
                    STATE_ISDECODING = true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
