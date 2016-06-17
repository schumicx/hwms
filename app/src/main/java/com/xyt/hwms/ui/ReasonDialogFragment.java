package com.xyt.hwms.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.xyt.hwms.R;
import com.xyt.hwms.bean.InboundQuery;

public class ReasonDialogFragment extends DialogFragment {

    int index;
    private String reasonArray[];
    private InboundQuery inboundQuery;

    public static ReasonDialogFragment newInstance(InboundQuery inboundQuery) {
        ReasonDialogFragment fragment = new ReasonDialogFragment();
        fragment.inboundQuery = inboundQuery;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        reasonArray = getResources().getStringArray(R.array.reason);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退回原因").setSingleChoiceItems(reasonArray, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                index = which;
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() != null) {
            ((InboundActivity) getActivity()).backWaste(inboundQuery, index);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (getActivity() != null) {
            ((InboundActivity) getActivity()).backWaste(inboundQuery, index);
        }
    }
}
