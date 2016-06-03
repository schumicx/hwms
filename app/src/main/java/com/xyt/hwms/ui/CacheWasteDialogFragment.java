package com.xyt.hwms.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.DateUtils;
import com.xyt.hwms.support.utils.PreferencesUtils;

public class CacheWasteDialogFragment extends DialogFragment {

    public TextView detail;

    private int applyIndex;
    private int wasteIndex;

    public static CacheWasteDialogFragment newInstance(int applyIndex, int wasteIndex) {
        CacheWasteDialogFragment fragment = new CacheWasteDialogFragment();
        fragment.applyIndex = applyIndex;
        fragment.wasteIndex = wasteIndex;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (!Constants.WASTE_BACK.equals(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getStatus())) {
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_PASS);
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
            PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
            PreferencesUtils.putBoolean(getActivity(), "isSync", false);
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_affirm_details, null);
        detail = (TextView) view.findViewById(R.id.detail);
        detail.setText(new Gson().toJson(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex)));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("退回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_BACK);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setContainer_label_code(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                        PreferencesUtils.putBoolean(getActivity(), "isSync", false);
                        ((BaseActivity) getActivity()).showReasonDialog(applyIndex, wasteIndex);
                    }
                })
                .setNegativeButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_PASS);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason_index(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                        PreferencesUtils.putBoolean(getActivity(), "isSync", false);
                    }
                });
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_F4:
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_PASS);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason_index(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                        PreferencesUtils.putBoolean(getActivity(), "isSync", false);
                        break;
                    case KeyEvent.KEYCODE_DEL:
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_BACK);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setContainer_label_code(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                        PreferencesUtils.putBoolean(getActivity(), "isSync", false);
                        ((BaseActivity) getActivity()).showReasonDialog(applyIndex, wasteIndex);
                        break;
                }
                dismiss();
                return false;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).closeDialog();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).closeDialog();
        }
    }
}
