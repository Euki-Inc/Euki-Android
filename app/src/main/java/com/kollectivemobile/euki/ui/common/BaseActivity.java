package com.kollectivemobile.euki.ui.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.manager.AppSettingsManager;
import com.kollectivemobile.euki.ui.main.CirclePromptBackground;
import com.kollectivemobile.euki.ui.pin.CheckPinActivity;
import com.kollectivemobile.euki.utils.Utils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Created by pAk on 6/17/16.
 */
public class BaseActivity extends AppCompatActivity implements BaseFragment.InteractionListener{
    @Inject AppSettingsManager mAppSettingsManager;

    private FragmentManagerHelper mFragmentManagerHelper;
    protected Toolbar tbToolbar;
    protected ProgressBar pbProgress;
    protected ProgressDialog mProgressDialog;
    protected Boolean mShowEukiLogo = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFragmentManagerHelper = new FragmentManagerHelper(getSupportFragmentManager());
        ((App)getApplication()).getAppComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Boolean wasInBackground = ((App)getApplication()).getWasBackground();
        if (this instanceof CheckPinActivity) {
        } else {
            if (wasInBackground && mAppSettingsManager.getPinCode() != null) {
                startActivity(CheckPinActivity.makeIntent(this, false));
            }
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        tbToolbar = findViewById(R.id.tb_toolbar);
        pbProgress = findViewById(R.id.pb_loading);

//        tbToolbar = ButterKnife.findById(this, R.id.tb_toolbar);
//        pbProgress = ButterKnife.findById(this, R.id.pb_loading);
        if (tbToolbar != null) {
            setSupportActionBar(tbToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        mFragmentManagerHelper.replace(fragment, addToBackStack);
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showProgressBar() {
        if (pbProgress != null) {
            pbProgress.bringToFront();
            pbProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (pbProgress != null) {
            pbProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String message) {
        dismissProgressDialog();
        hideProgressBar();
        Dialogs.createSimpleErrorDialog(this, message).show();
    }

    protected boolean willTheActivityHandleTheToolbar() {
        return false;
    }

    @Override
    public void updateTitle(String title, boolean showBack) {
        if (!willTheActivityHandleTheToolbar()) {
            updateToolbar(title, showBack);
        }
    }

    @Override
    public void showTutorial(String title, String text, View view, MaterialTapTargetPrompt.PromptStateChangeListener listener) {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(view)
                .setPrimaryText(title)
                .setSecondaryText(text)
                .setPrimaryTextColour(ContextCompat.getColor(this, R.color.euki_main))
                .setSecondaryTextColour(ContextCompat.getColor(this, R.color.euki_main))
                .setBackgroundColour(ContextCompat.getColor(this, R.color.light_mint))
                .setFocalPadding(R.dimen.dimen_x8_5)
                .setPromptStateChangeListener(listener)
                .setPromptBackground(new CirclePromptBackground())
                .setCaptureTouchEventOnFocal(true)
                .setFocalRadius(R.dimen.dimen_x7_5)
                .show();
    }

    @Override
    public void showTutorial(String title, String text, Integer resId, MaterialTapTargetPrompt.PromptStateChangeListener listener) {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(resId)
                .setPrimaryText(title)
                .setSecondaryText(text)
                .setPrimaryTextColour(ContextCompat.getColor(this, R.color.euki_main))
                .setSecondaryTextColour(ContextCompat.getColor(this, R.color.euki_main))
                .setBackgroundColour(ContextCompat.getColor(this, R.color.light_mint))
                .setFocalPadding(R.dimen.dimen_x8_5)
                .setPromptStateChangeListener(listener)
                .setPromptBackground(new CirclePromptBackground())
                .setCaptureTouchEventOnFocal(true)
                .setFocalRadius(R.dimen.dimen_x7_5)
                .show();
    }

    protected void updateToolbar(String title, boolean showBack) {
        if (tbToolbar != null && getSupportActionBar() != null) {
            tbToolbar.setTitle("");

            TextView tvTitle = tbToolbar.findViewById(R.id.tv_title);
//            TextView tvTitle = ButterKnife.findById(tbToolbar, R.id.tv_title);
            if (tvTitle != null) {
                tvTitle.setText(title);
            }
            if (showBack) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_purple);
                tbToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);

            if (mShowEukiLogo && title.isEmpty()) {
                getSupportActionBar().setLogo(Utils.getImageId("ic_nav_logo"));
            } else {
                getSupportActionBar().setLogo(null);
            }
        }
    }

    public AppSettingsManager getAppSettingsManager() {
        return mAppSettingsManager;
    }

    public void openURL(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    protected void showEukiLogo() {
        getSupportActionBar().setLogo(Utils.getImageId("ic_nav_logo"));
    }
}
