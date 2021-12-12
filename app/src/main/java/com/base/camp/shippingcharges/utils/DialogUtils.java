package com.base.camp.shippingcharges.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.base.camp.shippingcharges.R;


public class DialogUtils {


    private Activity activity;


    public DialogUtils(Activity activity) {
        this.activity = activity;
    }

    private Dialog buildDialogView(@LayoutRes int layout) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        return dialog;
    }


    public Dialog buildDialogInfo(@StringRes int title, @StringRes int content, @StringRes int bt_text_pos, @DrawableRes int icon, final CallbackDialog callback) {
        return buildDialogInfo(activity.getString(title), activity.getString(content), activity.getString(bt_text_pos), icon, callback);
    }

    // dialog info
    public Dialog buildDialogInfo(String title, String content, String bt_text_pos, @DrawableRes int icon, final CallbackDialog callback) {
        final Dialog dialog = buildDialogView(R.layout.dialog_info);

        ((TextView) dialog.findViewById(R.id.title)).setText(title);
        ((TextView) dialog.findViewById(R.id.content)).setText(content);
        ((Button) dialog.findViewById(R.id.bt_positive)).setText(bt_text_pos);
        ((ImageView) dialog.findViewById(R.id.icon)).setImageResource(icon);

        ((Button) dialog.findViewById(R.id.bt_positive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onPositiveClick(dialog);
            }
        });
        return dialog;
    }

    public static boolean needRequestPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static void rateAction(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

    public Dialog buildDialogWarning(@StringRes int title, @StringRes int content, @StringRes int bt_text_pos, @StringRes int bt_text_neg, @DrawableRes int icon, final CallbackDialog callback) {
        String _title = null;
        String _content = null;
        String _bt_text_neg = null;

        if (title != -1) _title = activity.getString(title);
        if (content != -1) _content = activity.getString(content);
        if (bt_text_neg != -1) _bt_text_neg = activity.getString(bt_text_neg);

        return buildDialogWarning(_title, _content, activity.getString(bt_text_pos), _bt_text_neg, icon, callback);
    }

    public Dialog buildDialogWarning(@StringRes int title, @StringRes int content, @StringRes int bt_text_pos, @DrawableRes int icon, final CallbackDialog callback) {
        String _title = null;
        String _content = null;

        if (title != -1) _title = activity.getString(title);
        if (content != -1) _content = activity.getString(content);

        return buildDialogWarning(_title, _content, activity.getString(bt_text_pos), null, icon, callback);
    }

    // dialog warning
    public Dialog buildDialogWarning(String title, String content, String bt_text_pos, String bt_text_neg, @DrawableRes int icon, final CallbackDialog callback) {
        final Dialog dialog = buildDialogView(R.layout.dialog_warning);

        // if id = -1 view will gone
        if (title != null) {
            ((TextView) dialog.findViewById(R.id.title)).setText(title);
        } else {
            ((TextView) dialog.findViewById(R.id.title)).setVisibility(View.GONE);
        }

        // if id = -1 view will gone
        if (content != null) {
            ((TextView) dialog.findViewById(R.id.content)).setText(content);
        } else {
            ((TextView) dialog.findViewById(R.id.content)).setVisibility(View.GONE);
        }
        ((Button) dialog.findViewById(R.id.bt_positive)).setText(bt_text_pos);
        if (bt_text_neg != null) {
            ((Button) dialog.findViewById(R.id.bt_negative)).setText(bt_text_neg);
        } else {
            ((Button) dialog.findViewById(R.id.bt_negative)).setVisibility(View.GONE);
        }
        ((ImageView) dialog.findViewById(R.id.icon)).setImageResource(icon);

        ((Button) dialog.findViewById(R.id.bt_positive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onPositiveClick(dialog);
            }
        });

        ((Button) dialog.findViewById(R.id.bt_negative)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onNegativeClick(dialog);
            }
        });
        return dialog;
    }

    public interface CallbackDialog {

        void onPositiveClick(Dialog dialog);

        void onNegativeClick(Dialog dialog);
    }

}
