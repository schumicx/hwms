package com.xyt.hwms.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.xyt.hwms.R;
import com.xyt.hwms.bean.InboundQuery;

public class ReasonDialogFragment extends DialogFragment {

    int index;
    private String a[];
    private InboundQuery inboundQuery;

    public static ReasonDialogFragment newInstance(InboundQuery inboundQuery) {
        ReasonDialogFragment fragment = new ReasonDialogFragment();
        fragment.inboundQuery = inboundQuery;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        a = getResources().getStringArray(R.array.reason);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退回原因").setSingleChoiceItems(a, 0, new DialogInterface.OnClickListener() {
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

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        this.getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                switch (keyCode) {
//                    case KeyEvent.KEYCODE_F4:
//                        ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("back_reason", a[iii]);
//                        ((Map) ((List) ((Map) Constants.AFFIRM_LIST.get(applyIndex)).get("detail")).get(wasteIndex)).put("back_reason_index", iii);
//                        PreferencesUtils.putString(getActivity(), "affirm", new Gson().toJson(Constants.AFFIRM_LIST));
//                        dialog.dismiss();
//                        break;
//                }
//                dismiss();
//                return false;
//            }
//        });
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        ((AffirmItemsActivity) getActivity()).reasonDialog = null;
//    }

//    @Override
//    public void dismiss() {
//        super.dismiss();
//        if (getActivity() != null) {
//            ((AffirmItemsActivity) getActivity()).updateView();
//            ((AffirmItemsActivity) getActivity()).dialog = null;
//        }
//    }
}
