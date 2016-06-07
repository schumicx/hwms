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
import com.xyt.hwms.support.utils.Constants;

public class InboundWasteDialogFragment extends DialogFragment {

    private InboundQuery inboundQuery;

    public static InboundWasteDialogFragment newInstance(InboundQuery inboundQuery) {
        InboundWasteDialogFragment fragment = new InboundWasteDialogFragment();
        fragment.inboundQuery = inboundQuery;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_waste, null);

        TextView wasteName = (TextView) view.findViewById(R.id.waste_name);
        TextView categoryCode = (TextView) view.findViewById(R.id.category_code);
        TextView labelCode = (TextView) view.findViewById(R.id.label_code);
        TextView packageType = (TextView) view.findViewById(R.id.package_type);
        TextView status = (TextView) view.findViewById(R.id.status);
        TextView isKeyWaste = (TextView) view.findViewById(R.id.is_key_waste);
        TextView harmfulIngredient = (TextView) view.findViewById(R.id.harmful_ingredient);
        TextView produceSource = (TextView) view.findViewById(R.id.produce_source);

        wasteName.setText(inboundQuery.getWaste_name());
        categoryCode.setText(inboundQuery.getCategory_code());
        labelCode.setText(inboundQuery.getLabel_code());
        packageType.setText(inboundQuery.getPackage_type());
        status.setText(Constants.WASTE_PASS.equals(inboundQuery.getStatus()) ? "已验证" : Constants.WASTE_BACK.equals(inboundQuery.getStatus()) ? "退回" : "未验证");
        isKeyWaste.setText("1".equals(inboundQuery.getIs_key_waste()) ? "是" : "否");
        harmfulIngredient.setText(inboundQuery.getHarmful_ingredient());
        produceSource.setText(inboundQuery.getProduce_source());

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
