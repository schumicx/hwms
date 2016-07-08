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
import android.widget.Toast;

import com.google.gson.Gson;
import com.xyt.hwms.R;
import com.xyt.hwms.support.utils.Constants;
import com.xyt.hwms.support.utils.DateUtils;
import com.xyt.hwms.support.utils.PreferencesUtils;

public class CacheWasteDialogFragment extends DialogFragment {

    private int applyIndex;
    private int wasteIndex;

    public static CacheWasteDialogFragment newInstance(int applyIndex, int wasteIndex) {
        CacheWasteDialogFragment fragment = new CacheWasteDialogFragment();
        fragment.applyIndex = applyIndex;
        fragment.wasteIndex = wasteIndex;
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
//bug 数据越界(操作太快)
        if (Constants.WASTE_PASS.equals(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getStatus())) {
            view.setBackgroundColor(0xff5ea640);
        } else if (Constants.WASTE_BACK.equals(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getStatus())) {
            view.setBackgroundColor(0xffff8e00);
        }

        if (!Constants.WASTE_BACK.equals(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getStatus())) {
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_PASS);
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
            Constants.AFFIRM_LIST.getCollection().get(applyIndex).setModify(true);
            PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
            PreferencesUtils.putBoolean(getActivity(), "isSync", false);
        }

        wasteName.setText(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getWaste_name());
        categoryCode.setText(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getCategory_code());
        labelCode.setText(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getLabel_code());
        packageType.setText(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getPackage_type());
        status.setText(Constants.WASTE_PASS.equals(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getStatus()) ? "已验证" : Constants.WASTE_BACK.equals(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getStatus()) ? "退回" : "未验证");
        isKeyWaste.setText("1".equals(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getIs_key_waste()) ? "是" : "否");
        harmfulIngredient.setText(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getHarmful_ingredient());
        produceSource.setText(Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).getProduce_source());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("退回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_BACK);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setContainer_label_code(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).setModify(true);
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                        PreferencesUtils.putBoolean(getActivity(), "isSync", false);
                        ((BaseActivity) getActivity()).showReasonDialog(applyIndex, wasteIndex);
                    }
                })
                .setNegativeButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_PASS);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason_index(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).setModify(true);
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                        PreferencesUtils.putBoolean(getActivity(), "isSync", false);
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
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_PASS);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setBack_reason_index(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).setModify(true);
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                        PreferencesUtils.putBoolean(getActivity(), "isSync", false);
                        break;
                    case KeyEvent.KEYCODE_DEL:
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setStatus(Constants.WASTE_BACK);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setContainer_label_code(null);
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).getDetail().get(wasteIndex).setTransfer_time(DateUtils.getCurrentTime1());
                        Constants.AFFIRM_LIST.getCollection().get(applyIndex).setModify(true);
                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
                        PreferencesUtils.putBoolean(getActivity(), "isSync", false);
                        ((BaseActivity) getActivity()).showReasonDialog(applyIndex, wasteIndex);
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
            ((BaseActivity) getActivity()).closeDialog();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).closeDialog();
        }
    }
}
