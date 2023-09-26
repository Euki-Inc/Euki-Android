package com.kollectivemobile.euki.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.customview.DialogCustomViewExtKt;
import com.kollectivemobile.euki.App;
import com.kollectivemobile.euki.R;
import com.kollectivemobile.euki.networking.EukiCallback;

import java.util.List;

/**
 * Created by gyosida on 22/12/2015.
 */
public class Dialogs {

    public static AlertDialog createListDialog(Context context, List<Object> items, DialogInterface.OnClickListener itemClickListener) {
        ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item, items);
        AlertDialog listDialog = new AlertDialog.Builder(context).setAdapter(arrayAdapter, itemClickListener).create();
        listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return listDialog;
    }

    public static AlertDialog createSimpleErrorDialog(Context context, String description) {
        return createSimpleDialog(context, App.getContext().getString(R.string.error_message_title), description, null);
    }

    public static AlertDialog createSimpleDialog(Context context, String title, String description, DialogInterface.OnClickListener positiveListener) {
        return createSimpleDialog(context, title, description, context.getString(R.string.ok), positiveListener);
    }

    public static AlertDialog createSimpleDialog(Context context, String title, String description, String positiveText, DialogInterface.OnClickListener positiveListener) {
        return createSimpleDialog(context, title, description, positiveText, false, positiveListener);
    }

    public static AlertDialog createSimpleDialog(Context context, String title, String description, String positiveText, Boolean showCancel, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = getDialogBuilder(context, title, description).setPositiveButton(positiveText, positiveListener);

        if (showCancel) {
            builder.setNegativeButton(context.getString(R.string.cancel), null);
        }

        return builder.create();
    }

    public static AlertDialog createTwoOptionsDialog(Activity activity, String title, String description, String positive, String negative, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = getDialogBuilder(activity, title, description).setPositiveButton(positive, positiveListener);
        builder.setNegativeButton(negative, cancelListener);
        return builder.create();
    }

    private static AlertDialog.Builder getDialogBuilder(Context context, String title, String description) {
        return new AlertDialog.Builder(context).setTitle(title).setMessage(description);
    }

    public static void showTextDialog(Context context, int titleRes, int hintRes, String text, EukiCallback<String> callback) {
        String title = App.getContext().getString(titleRes);

        View customView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_text, null, false);

        final EditText etText = customView.findViewById(R.id.et_text);
        etText.setHint(hintRes);
        etText.setText(text);
        if (text != null && !text.isEmpty()) {
            etText.setSelection(text.length());
        }

        MaterialDialog dialog = new MaterialDialog(context, MaterialDialog.getDEFAULT_BEHAVIOR());
        DialogCustomViewExtKt.customView(dialog, null, customView, false, false, true, true);

        dialog.title(null, title);
        dialog.positiveButton(null , App.getContext().getString(R.string.ok), materialDialog -> {
            callback.onSuccess(etText.getText().toString());
            return null;
        });
        dialog.negativeButton(null, App.getContext().getString(R.string.cancel), null);
        dialog.show();
    }

}
