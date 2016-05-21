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

import com.google.gson.Gson;
import com.xyt.hwms.R;

import java.util.List;
import java.util.Map;

public class AffirmDetailsDialogFragment extends DialogFragment {

    public TextView detail;

    private List object;
    private int applyIndex;
    private int wasteIndex;

    public static AffirmDetailsDialogFragment newInstance(List list, int applyIndex, int wasteIndex) {
        AffirmDetailsDialogFragment fragment = new AffirmDetailsDialogFragment();
        fragment.object = list;
        fragment.applyIndex = applyIndex;
        fragment.wasteIndex = wasteIndex;
        if (TextUtils.isEmpty((String)((Map) ((List) ((Map) list.get(applyIndex)).get("detail")).get(wasteIndex)).get("status"))) {
            ((Map) ((List) ((Map) list.get(applyIndex)).get("detail")).get(wasteIndex)).put("status", "ssss");
        }
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.activity_affirm_details, null);
        detail = (TextView) view.findViewById(R.id.detail);
        detail.setText(new Gson().toJson(((Map) ((List) ((Map) object.get(applyIndex)).get("detail")).get(wasteIndex))));

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("sssss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((Map) ((List) ((Map) object.get(applyIndex)).get("detail")).get(wasteIndex)).put("status", "xxxx");
                        detail.setText(new Gson().toJson(((Map) ((List) ((Map) object.get(applyIndex)).get("detail")).get(wasteIndex))));
                    }
                });
//                .setNegativeButton("aaaaa", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        AffirmDetailsDialogFragment.this.getDialog().cancel();
//                    }
//                });
        return builder.create();
    }
}
