package com.example.administrator.demo.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.administrator.demo.R;
import com.example.administrator.demo.activity.SecondActivity;
import com.example.administrator.demo.activity.TitleView;
import com.example.administrator.demo.entry.DrawCircle;
import com.example.administrator.demo.entry.Person;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {
    @Bind(R.id.phoneNum_tv)
    TextView phoneNumTv;
    @Bind(R.id.phoneNum_edit)
    EditText phoneNumEdit;
    @Bind(R.id.password_tv)
    TextView passwordTv;
    @Bind(R.id.password_edit)
    EditText passwordEdit;
    @Bind(R.id.forgetpass_btn)
    Button forgetpassBtn;
    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.money_sum_tv)
    TextView moneySumTv;
    @Bind(R.id.rating)
    RatingBar rating;
    @Bind(R.id.draw_circle)
    DrawCircle drawCircle;
    @Bind(R.id.title)
    TitleView title;

    //    Timer timer;
//    Button btn_code;
//    int res = 10;

    private TextView money_sum_tv;
    private Person person;
    private final static int REQUESTCODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        person = new Person();
        initTitle();
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                drawCircle.setAlpha(v);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initTitle(){
        title.setBTitle("登录");
    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ButterKnife.bind(getActivity());
//        ViewUtils.inject(getActivity());//注入view和事件
//        person = new Person();
//        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                drawCircle.setAlpha(v);
//            }
//        });
////        btn_code = (Button) findViewById(R.id.code);
//
////        btn_code.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                timer = new Timer(true);
////                timer.schedule(new TimerTask() {
////
////                    @Override
////                    public void run() {
////                        Message msg = new Message();
////                        msg.what = 1;
////                        handler.sendMessage(msg);
////
////                    }
////                }, 0, 1000);
////            }
////        });
//
//    }


//    @OnClick({R.id.btn_ok, R.id.forgetpass_btn})
//    public void onclick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_ok:
//                showDialog();
//                break;
//            case R.id.forgetpass_btn:
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), ListViewFragment.class);
//                startActivityForResult(intent, REQUESTCODE);
//                break;
//            default:
//                break;
//        }
//
//
//    }

    /**
     * 序列化对象
     *
     * @param person
     * @return
     * @throws IOException
     */
    private String serialize(Person person) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(person);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = URLEncoder.encode(serStr, "UTF-8");
        Log.d("serial", "serialize str =" + serStr);
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }


//    public void saveObject(String strObject) {
//        SharedPreferences sp = getSharedPreferences("person", 0);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putString("person", strObject);
//        edit.commit();
//    }


//    Handler handler = new Handler() {
//
//        public void handlerMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//                Log.i("yao", res + "");
//                btn_code.setText(res + "");
//                res--;
//                if (res <= 0) {
//                    res = 10;
//                    btn_code.setText("获取验证码");
//                    timer.cancel();
//                }
//            }
//
//        }
//
//    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
//                int sum=0;
//                if(data!=null){
//                    String money = data.getStringExtra("money");
//                        String[] str = money.split("\\.");
//                    if(!money.equals("")) {
//                        for (int i = 0; i < str.length; i++) {
//                            int t = Integer.parseInt(str[i]);
//                            sum = sum + t;
//                        }
//                    }
//                }
//                money_sum_tv.setText("总额：" + sum);
        }
    }


    private void showDialog() {
        String num1 = phoneNumEdit.getText().toString();
        String num2 = passwordEdit.getText().toString();
        person.setName(num1);
        person.setId(1);
        person.setPass(num2);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("是否确认登录");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("person_data",person);
//                intent.putExtras(bundle);
//                try {
//                    saveObject(serialize(person));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                intent.setClass(getActivity(), SecondActivity.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.forgetpass_btn, R.id.btn_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                showDialog();
                break;
            case R.id.forgetpass_btn:
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), ListViewFragment.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
    }
}