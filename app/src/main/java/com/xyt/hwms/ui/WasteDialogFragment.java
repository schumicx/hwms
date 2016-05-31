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
import android.widget.TextView;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.PreferencesUtils;

import java.util.List;
import java.util.Map;

public class WasteDialogFragment extends DialogFragment {

    public TextView detail;

    public static WasteDialogFragment newInstance() {
        WasteDialogFragment fragment = new WasteDialogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_affirm_details, null);
        detail = (TextView) view.findViewById(R.id.detail);
        detail.setText("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("退回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((BaseActivity)getActivity()).showReasonDialog();
                    }
                })
                .setNegativeButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                        break;
                    case KeyEvent.KEYCODE_DEL:
                        ((BaseActivity)getActivity()).showReasonDialog();
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
            ((BaseActivity) getActivity()).closeAffirmDialog();
//            ((AffirmItemsActivity) getActivity()).updateView();
//            ((AffirmItemsActivity) getActivity()).affirmDialog = null;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).closeAffirmDialog();
//            ((AffirmItemsActivity) getActivity()).updateView();
//            ((AffirmItemsActivity) getActivity()).affirmDialog = null;
        }
    }
}
