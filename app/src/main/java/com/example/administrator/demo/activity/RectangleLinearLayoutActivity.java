package com.example.administrator.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.administrator.demo.R;
import com.example.administrator.demo.activity.base.SquareLinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RectangleLinearLayoutActivity extends Activity {

    @Bind(R.id.real_name)
    SquareLinearLayout realName;
    @Bind(R.id.bank_card)
    SquareLinearLayout bankCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectangle_linear_layout);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.real_name, R.id.bank_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.real_name:
                Intent intent = new Intent();
                intent.setClass(this, Map.class);
                startActivity(intent);
                break;
            case R.id.bank_card:
                Intent intent1 = new Intent();
                intent1.setClass(this, ExpandTextview.class);
                startActivity(intent1);
                break;
        }
    }
}
