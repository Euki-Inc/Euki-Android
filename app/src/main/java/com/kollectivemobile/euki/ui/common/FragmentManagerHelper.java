package com.kollectivemobile.euki.ui.common;

import androidx.annotation.IdRes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kollectivemobile.euki.R;

/**
 * Created by pAk on 21/12/2015.
 */
public class FragmentManagerHelper {

    private FragmentManager mFragmentManager;

    public FragmentManagerHelper(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void replace(Fragment fragment, boolean addToBackStack) {
        replace(R.id.flContent, fragment, addToBackStack);
    }

    public void replace(@IdRes int containerId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction replaceTransaction = mFragmentManager.beginTransaction();
        replaceTransaction.replace(containerId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            replaceTransaction.addToBackStack(fragment.getClass().getName());
        }
        replaceTransaction.commit();
    }

}
