package com.xyt.hwms.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class SyncDialogFragment extends DialogFragment {

    public static SyncDialogFragment newInstance() {
        SyncDialogFragment fragment = new SyncDialogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("转移单数据未同步，点击同步按钮进行同步!")
                .setPositiveButton("同步", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (getActivity() instanceof AffirmActivity) {
                            ((AffirmActivity) getActivity()).syncRequest();
                        } else if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).syncRequest();
                        }
                    }
                });
        return builder.create();
    }
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//    }
}
