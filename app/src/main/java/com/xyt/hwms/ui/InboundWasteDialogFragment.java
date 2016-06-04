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
import com.xyt.hwms.bean.InboundQuery;
import com.xyt.hwms.bean.RecycleDetail;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.DateUtils;
import com.xyt.hwms.support.utils.PreferencesUtils;

public class InboundWasteDialogFragment extends DialogFragment {

    public TextView detail;
    private InboundQuery inboundQuery;

    public static InboundWasteDialogFragment newInstance(InboundQuery inboundQuery) {
        InboundWasteDialogFragment fragment = new InboundWasteDialogFragment();
        fragment.inboundQuery = inboundQuery;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_affirm_details, null);
        detail = (TextView) view.findViewById(R.id.detail);
        detail.setText(new Gson().toJson(inboundQuery));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("退回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((InboundActivity) getActivity()).showReasonDialog(inboundQuery);
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
                        ((InboundActivity) getActivity()).showReasonDialog(inboundQuery);
                        break;
                }
                dismiss();
                return false;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        if (getActivity() != null) {
//            ((BaseActivity) getActivity()).closeDialog();
//        }
//    }
//
//    @Override
//    public void dismiss() {
//        super.dismiss();
//        if (getActivity() != null) {
//            ((BaseActivity) getActivity()).closeDialog();
//        }
//    }
}
