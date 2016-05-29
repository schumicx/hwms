package com.xyt.hwms.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.List;
import java.util.Map;

public class ReasonDialogFragment extends DialogFragment {

    int iii = 0;
    private int applyIndex;
    private int wasteIndex;
    private String a[];

    public static ReasonDialogFragment newInstance(int applyIndex, int wasteIndex) {
        ReasonDialogFragment fragment = new ReasonDialogFragment();
        fragment.applyIndex = applyIndex;
        fragment.wasteIndex = wasteIndex;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        a = getResources().getStringArray(R.array.reason);
        try {
            iii = Integer.valueOf(((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).get("back_reason_index").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(TextUtils.isEmpty((String) ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).get("back_reason"))) {
            ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("back_reason", a[iii]);
            ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("back_reason_index", iii);
        }
        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退回原因").setSingleChoiceItems(a, iii, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("back_reason", a[which]);
                ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("back_reason_index", which);
                PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                dialog.dismiss();
            }
        });
        return builder.create();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                switch (keyCode) {
//                    case KeyEvent.KEYCODE_F4:
//                        ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("back_reason", a[iii]);
//                        ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("back_reason_index", iii);
//                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
//                        dialog.dismiss();
//                        break;
//                }
//                dismiss();
//                return false;
//            }
//        });
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        ((AffirmItemsActivity) getActivity()).reasonDialog = null;
//    }

//    @Override
//    public void dismiss() {
//        super.dismiss();
//        if (getActivity() != null) {
//            ((AffirmItemsActivity) getActivity()).updateView();
//            ((AffirmItemsActivity) getActivity()).dialog = null;
//        }
//    }
}
