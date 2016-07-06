package com.xyt.hwms.support.utils;

import android.app.Application;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.motorolasolutions.adc.decoder.BarCodeReader;

public class ApplicationController extends Application {

    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";
    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static ApplicationController sInstance;

    static {
        System.loadLibrary("IAL");
        System.loadLibrary("SDL");
        System.loadLibrary("barcodereader44");
    }

    public int trigMode = BarCodeReader.ParamVal.AUTO_AIM;
    public int state = Constants.STATE_IDLE;
    public ToneGenerator toneGenerator;
    public BarCodeReader barCodeReader;

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized ApplicationController getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize the singleton
        sInstance = this;
        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        barCodeReader = BarCodeReader.open(this);

        doSetParam(22, 2);
        doSetParam(23, 40);
        doSetParam(BarCodeReader.ParamNum.QR_INVERSE, BarCodeReader.ParamVal.INVERSE_AUTOD);
        doSetParam(BarCodeReader.ParamNum.DATAMATRIX_INVERSE, BarCodeReader.ParamVal.INVERSE_AUTOD);
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be created
        // when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified then
     * it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUTMS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);
        getRequestQueue().add(req.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUTMS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)));
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     */
    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    public void beep() {
        if (toneGenerator != null) {
            toneGenerator.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY_ONE_SHOT);
        }
    }

    public boolean isAutoAim() {
        return (trigMode == BarCodeReader.ParamVal.AUTO_AIM);
    }

    public void doDecode() {
        if (setIdle() != Constants.STATE_IDLE) {
            return;
        }
        state = Constants.STATE_DECODE;
        barCodeReader.startDecode(); // start decode (callback gets results)
    }

    private void doSetParam(int num, int val) {
        int ret = barCodeReader.setParameter(num, val);
        if (ret != BarCodeReader.BCR_ERROR && num == BarCodeReader.ParamNum.PRIM_TRIG_MODE) {
            trigMode = val;
            if (val == BarCodeReader.ParamVal.AUTO_AIM) {
                barCodeReader.startHandsFreeDecode(BarCodeReader.ParamVal.AUTO_AIM);
            }
        }
    }

    private int setIdle() {
        int prevState = state;
        int ret = prevState;        //for states taking time to chg/end
        state = Constants.STATE_IDLE;
        switch (prevState) {
            case Constants.STATE_DECODE:
                barCodeReader.stopDecode();
                break;
            default:
                ret = Constants.STATE_IDLE;
        }
        return ret;
    }
}
