package com.xyt.hwms.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.List;
import java.util.Map;

public class AffirmDetailsDialogFragment extends DialogFragment {

    public TextView detail;

    private int applyIndex;
    private int wasteIndex;

    public static AffirmDetailsDialogFragment newInstance(int applyIndex, int wasteIndex) {
        AffirmDetailsDialogFragment fragment = new AffirmDetailsDialogFragment();
        fragment.applyIndex = applyIndex;
        fragment.wasteIndex = wasteIndex;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (TextUtils.isEmpty((String) ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).get("status"))) {
            ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("status", "已转移");
            PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_affirm_details, null);
        detail = (TextView) view.findViewById(R.id.detail);
        detail.setText(new Gson().toJson(((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex))));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("退回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("status", "退回");
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                    }
                })
                .setNegativeButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("status", "已转移");
                        ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("note", "");
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                    }
                });
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() != null) {
            ((AffirmItemsActivity) getActivity()).updateView();
            ((AffirmItemsActivity) getActivity()).dialog = null;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (getActivity() != null) {
            ((AffirmItemsActivity) getActivity()).updateView();
            ((AffirmItemsActivity) getActivity()).dialog = null;
        }
    }
}
