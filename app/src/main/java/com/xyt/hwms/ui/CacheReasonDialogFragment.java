package com.xyt.hwms.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

public class CacheReasonDialogFragment extends DialogFragment {

    int reasonIndex = 0;
    private int applyIndex;
    private int wasteIndex;
    private String reasonArray[];

    public static CacheReasonDialogFragment newInstance(int applyIndex, int wasteIndex) {
        CacheReasonDialogFragment fragment = new CacheReasonDialogFragment();
        fragment.applyIndex = applyIndex;
        fragment.wasteIndex = wasteIndex;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        reasonArray = getResources().getStringArray(R.array.reason);
        try {
            reasonIndex = Integer.valueOf(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getBack_reason_index());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getBack_reason())) {
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason(reasonArray[(int)reasonIndex]);
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason_index(String.valueOf(reasonIndex));
            PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退回原因").setSingleChoiceItems(reasonArray, (int)reasonIndex, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason(reasonArray[which]);
                Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason_index(String.valueOf(which));
                PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
