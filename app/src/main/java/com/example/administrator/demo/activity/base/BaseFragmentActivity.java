package com.example.administrator.demo.activity.base;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.example.administrator.demo.G;
import com.example.administrator.demo.R;


/**
 * Created by yult on 2015/12/14.
 */
public class BaseFragmentActivity extends FragmentActivity {

    @Override
    public void finish() {
        super.finish();
        if (G.isActivityAnimationEnabled && android.os.Build.VERSION.SDK_INT >= 5) {
            overridePendingTransition(
                    R.anim.translate_between_interface_left_in,
                    R.anim.translate_between_interface_right_out);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (G.isActivityAnimationEnabled && android.os.Build.VERSION.SDK_INT >= 5) {
            overridePendingTransition(
                    R.anim.translate_between_interface_right_in,
                    R.anim.translate_between_interface_left_out);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (G.isActivityAnimationEnabled && android.os.Build.VERSION.SDK_INT >= 5) {
            overridePendingTransition(
                    R.anim.translate_between_interface_right_in,
                    R.anim.translate_between_interface_left_out);
        }
    }
}
