package com.xyt.hwms.ui;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.motorolasolutions.adc.decoder.BarCodeReader;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.Constants;

import java.util.HashMap;
import java.util.Map;


public abstract class BaseActivity1 extends AppCompatActivity /*implements BarCodeReader.DecodeCallback*/ {

    protected Context context;
    protected int pageNum = Constants.STARTPAGE;
    protected int visibleLastIndex = 0;
    protected int curPageSize = 0;
    protected String NFCTagId;

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;

    static final int STATE_IDLE = 0;
    static final int STATE_DECODE = 1;
    static long decode_start = 0;
    static long decode_end = 0;

    static {
        System.loadLibrary("IAL");
        System.loadLibrary("SDL");
        System.loadLibrary("barcodereader44");
    }

    private boolean STATE_ISDECODING = false;
    private ToneGenerator tg = null;
    private BarCodeReader bcr = null;
    private boolean beepMode = true;        // decode beep enable
    private int trigMode = BarCodeReader.ParamVal.AUTO_AIM;
    private int state = STATE_IDLE;

    public abstract void getTagId(String data);
    public abstract void getBarcode(String data);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        resolveIntent(getIntent());

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(getBaseContext(), getText(R.string.no_nfc), Toast.LENGTH_SHORT).show();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        tg = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
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

        state = STATE_IDLE;

        try {
            bcr = BarCodeReader.open(getApplicationContext());
            if (bcr == null) {
                return;
            }
            bcr.setDecodeCallback(new BarCodeReader.DecodeCallback() {
                @Override
                public void onDecodeComplete(int symbology, int length, byte[] data, BarCodeReader reader) {
                    if (state == STATE_DECODE) {
                        state = STATE_IDLE;
                    }

                    if (length > 0) {
                        decode_end = SystemClock.elapsedRealtime();
                        if (isAutoAim() == false) {
                            bcr.stopDecode();
                        }

                        if (beepMode) {
                            beep();
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

//            dspData(new String(data, 0, length));
                        Toast.makeText(context, new String(data, 0, length), Toast.LENGTH_LONG).show();
                        getBarcode(new String(data, 0, length));
                    }
                    STATE_ISDECODING = false;
                }

                @Override
                public void onEvent(int event, int info, byte[] data, BarCodeReader reader) {

                }
            });
        } catch (Exception e) {
        }
        doSetParam(22, 2);
        doSetParam(23, 40);
        doSetParam(BarCodeReader.ParamNum.QR_INVERSE, BarCodeReader.ParamVal.INVERSE_AUTOD);
        doSetParam(BarCodeReader.ParamNum.DATAMATRIX_INVERSE, BarCodeReader.ParamVal.INVERSE_AUTOD);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }

        if (bcr != null) {
            setIdle();
            bcr.release();
            bcr = null;
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

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Toast.makeText(getBaseContext(), dumpTagData(tag).get("tagId"), Toast.LENGTH_SHORT).show();
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

    // display status string
//    private void dspData(String s) {
//        tvData.setText("");
//        tvData.setText(s);
//    }

    private void beep() {
        if (tg != null) {
            tg.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY_ONE_SHOT);
        }
    }

    private boolean isAutoAim() {
        return (trigMode == BarCodeReader.ParamVal.AUTO_AIM);
    }

    // set param
    private int doSetParam(int num, int val) {
        int ret = bcr.setParameter(num, val);
        if (ret != BarCodeReader.BCR_ERROR && num == BarCodeReader.ParamNum.PRIM_TRIG_MODE) {
            trigMode = val;
            if (val == BarCodeReader.ParamVal.AUTO_AIM) {
                ret = bcr.startHandsFreeDecode(BarCodeReader.ParamVal.AUTO_AIM);
            }
        }

        return ret;
    }

    // start a decode session
    private void doDecode() {
        if (setIdle() != STATE_IDLE) {
            return;
        }

//        dspData("");
        state = STATE_DECODE;
        bcr.startDecode(); // start decode (callback gets results)
    }

    private int setIdle() {
        int prevState = state;
        int ret = prevState;        //for states taking time to chg/end

        state = STATE_IDLE;
        switch (prevState) {
            case STATE_DECODE:
                bcr.stopDecode();
                break;
            default:
                ret = STATE_IDLE;
        }
        return ret;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F5:
                if (STATE_ISDECODING == false) {
                    decode_start = SystemClock.elapsedRealtime();
                    doDecode();
                    STATE_ISDECODING = true;
                }
                break;
            case KeyEvent.KEYCODE_F4:
                if (STATE_ISDECODING == false) {
                    decode_start = SystemClock.elapsedRealtime();
                    doDecode();
                    STATE_ISDECODING = true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
