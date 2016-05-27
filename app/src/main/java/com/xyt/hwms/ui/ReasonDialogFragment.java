package com.xyt.hwms.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.List;
import java.util.Map;

public class ReasonDialogFragment extends DialogFragment {

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
        String xxx = ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).get("note").toString();
        int iii = 0;
        if (!TextUtils.isEmpty(xxx)) {
            iii = Integer.valueOf(xxx.split(",")[1]);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退回原因").setSingleChoiceItems(a, iii, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("note", a[which] + "," + which);
                PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (TextUtils.isEmpty(((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).get("note").toString())) {
            ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("note", a[0] + "," + 0);
            PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
        }
        Toast.makeText(getContext(), new Gson().toJson(Constants.AFFIRM_LIST), Toast.LENGTH_LONG).show();
    }
//
//    @Override
//    public void dismiss() {
//        super.dismiss();
//        if (getActivity() != null) {
//            ((AffirmItemsActivity) getActivity()).updateView();
//            ((AffirmItemsActivity) getActivity()).dialog = null;
//        }
//    }
}
