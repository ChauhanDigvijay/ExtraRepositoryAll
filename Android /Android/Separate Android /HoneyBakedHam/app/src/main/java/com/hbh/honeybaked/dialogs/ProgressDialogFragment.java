package com.hbh.honeybaked.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseDialogFragment;
import java.lang.reflect.Field;

public class ProgressDialogFragment extends BaseDialogFragment {
    private boolean isCancel = true;
    private boolean isCancelOnTouch = true;

    class C17241 implements OnKeyListener {
        C17241() {
        }

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode != 4) {
                return false;
            }
            if (event.getAction() == 0 || !ProgressDialogFragment.this.isCancel) {
                return true;
            }
            ProgressDialogFragment.this.dismiss();
            return true;
        }
    }

    public static ProgressDialogFragment newInstance(boolean isCancel, boolean isCancelOnTouch) {
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        Bundle b = new Bundle();
        b.putBoolean("isCancel", isCancel);
        b.putBoolean("isCancelOnTouch", isCancelOnTouch);
        progressDialogFragment.setArguments(b);
        return progressDialogFragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isCancel = getArguments().getBoolean("isCancel");
            this.isCancelOnTouch = getArguments().getBoolean("isCancelOnTouch");
        }
        setRetainInstance(true);

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(1);
        getDialog().setCanceledOnTouchOutside(this.isCancelOnTouch);
        getDialog().setCancelable(this.isCancel);
        getDialog().getWindow().setSoftInputMode(3);
        return getActivity().getLayoutInflater().inflate(R.layout.progress_layout, container, false);
    }

    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new C17241());
    }

    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setOnDismissListener(null);
        }
        super.onDestroyView();
    }

    public void show(FragmentManager manager, String tag) {
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag);
        }
    }

    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        }
    }
}
