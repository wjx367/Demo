package com.example.administrator.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.demo.R;
import com.example.administrator.demo.Volley.RequestManager;
import com.example.administrator.demo.Volley.ServiceListener;
import com.example.administrator.demo.entry.LoanItemDetailInfo;
import com.example.administrator.demo.view.pullable.PullToRefreshLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;

import java.util.HashMap;

public class SecondActivity extends Activity implements PullToRefreshLayout.OnRefreshListener  {
    @ViewInject(R.id.loanitem_detail_totalamount)//总额
    private TextView loanitem_detail_totalamount;
    @ViewInject(R.id.loanitem_detail_leftamount)//剩余可投金额
    private TextView loanitem_detail_leftamount;
    @ViewInject(R.id.loanitem_detail_rate)//预期年化利率
    private TextView loanitem_detail_rate;
    @ViewInject(R.id.loanitem_detail_name)
    private TextView loanitem_detail_name;//产品名称
    @ViewInject(R.id.loanitem_detail_period)
    private TextView loanitem_detail_period;//产品期限
    @ViewInject(R.id.loanitem_detail_leastamount)
    private TextView loanitem_detail_leastamount;//最小投标
    @ViewInject(R.id.loanitem_detail_repayment)
    private TextView loanitem_detail_repayment;//还款方式
    @ViewInject(R.id.loanitem_detail_profittime)
    private TextView loanitem_detail_profittime;//计息时间
    @ViewInject(R.id.loan_item_layout)
    private TextView linearLayout;
    @ViewInject(R.id.loan_item_layout1)
    private TextView linearLayout1;
    @ViewInject(R.id.refresh_root)
    private PullToRefreshLayout pullToRefreshLayout;
    private long borrowId=137;
    private LoanItemDetailInfo.LoanItemDetailEntity loanItemDetailEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.project_layout);
        ViewUtils.inject(this);
        pullToRefreshLayout.setOnRefreshListener(this);
//        Person person= (Person) getIntent().getSerializableExtra("person_data");
//        Person person = null;
//
//        try {
//            person = deSerialization(getObject());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        GetData();
    }

    @OnClick({R.id.loan_item_layout,R.id.loan_item_layout1,R.id.submit_btn})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loan_item_layout:
                Intent intent=new Intent();
                intent.setClass(SecondActivity.this, WebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.loan_item_layout1:
                Uri uri = Uri.parse("tel:" + 1111111111);
                Intent intent1=new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intent1);
                break;
            case R.id.submit_btn:
                Intent intent2=new Intent();
                intent2.setClass(SecondActivity.this,RectangleLinearLayoutActivity.class);
                startActivity(intent2);
                break;
        }
    }
//    /**
//     * 反序列化对象
//     *
//     * @param str
//     * @return
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//    public Person deSerialization(String str) throws IOException,
//            ClassNotFoundException {
//        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
//                redStr.getBytes("ISO-8859-1"));
//        ObjectInputStream objectInputStream = new ObjectInputStream(
//                byteArrayInputStream);
//        Person person = (Person) objectInputStream.readObject();
//        objectInputStream.close();
//        byteArrayInputStream.close();
//        return person;
//    }
//
//    String getObject() {
//        SharedPreferences sp = getSharedPreferences("person", 0);
//        return sp.getString("person", null);
//    }

    private void GetData() {
        HashMap<String,String> maps = RequestManager.getCommonMap();
        maps.put("borrowId", borrowId +"");
        RequestManager.goRquest(this, "http://123.56.106.181/product/getBorrowDetail.html", maps, new ServiceListener() {
            @Override
            public void onResult(String result) throws JSONException {
//                JSONTokener jsonTokener=new JSONTokener(result);
//                JSONObject  DetailInfo= (JSONObject) jsonTokener.nextValue();
//                BindTextView(DetailInfo);
               LoanItemDetailInfo loanItemDetailInfo= com.alibaba.fastjson.JSONObject.parseObject(result,LoanItemDetailInfo.class);
                if(loanItemDetailInfo.getCode()==0){
                    if(loanItemDetailInfo.getDataList()!=null){
                        loanItemDetailEntity=loanItemDetailInfo.getDataList().get(0);
                        if(loanItemDetailEntity!=null){
//                            Toast.makeText(getApplicationContext(),loanItemDetailEntity+"",Toast.LENGTH_SHORT).show();
                            BindTextView(loanItemDetailEntity);
                            pullToRefreshLayout.onLoadSuccess();
                        }else{
                            Toast.makeText(getApplicationContext(), "标详情信息加载失败", Toast.LENGTH_SHORT).show();
                            pullToRefreshLayout.onLoadFailed();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "暂无相关信息", Toast.LENGTH_SHORT).show();
                        pullToRefreshLayout.onLoadSuccess();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "标详情获取失败", Toast.LENGTH_SHORT).show();
                    pullToRefreshLayout.onLoadFailed();
                }

            }
/*
第二个接口
 */
//            private void GetData() {
//                HashMap<String,String> maps = RequestManager.getCommonMap();
//                maps.put("currentPage", 1 +"");
//                maps.put("borrowType", 301 +"");
//                RequestManager.goRquest(this, "http://123.56.106.181/product/getBorrowList.html", maps, new ServiceListener() {
//                    @Override
//                    public void onResult(String result) throws JSONException {
//                        JSONTokener jsonTokener = new JSONTokener(result);
//                        JSONObject DetailInfo = (JSONObject) jsonTokener.nextValue();
//                        BindTextView(DetailInfo);
////                LoanItemDetailInfo loanItemDetailInfo= com.alibaba.fastjson.JSONObject.parseObject(result,LoanItemDetailInfo.class);
////                if(loanItemDetailInfo.getCode()==0){
////                    loanItemDetailEntity=loanItemDetailInfo.getDataList().get(0);
////                        if(loanItemDetailEntity!=null){
////                            Toast.makeText(getApplicationContext(),loanItemDetailEntity+"",Toast.LENGTH_SHORT).show();
////                            BindTextView(loanItemDetailEntity);
////                        }
////                }
//                    }

            @Override
            public void onException(Exception e) {
                Toast.makeText(getApplicationContext(), "请求发送错误", Toast.LENGTH_SHORT).show();
                pullToRefreshLayout.onLoadFailed();
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onNetworkError() {
                Toast.makeText(getApplicationContext(), "服务调用错误", Toast.LENGTH_SHORT).show();
                pullToRefreshLayout.onLoadFailed();
            }

        });

    }
    private void BindTextView(LoanItemDetailInfo.LoanItemDetailEntity entity){
        loanitem_detail_rate.setText(entity.getYield()+"");
        loanitem_detail_totalamount.setText(entity.getAccount()+"");
        loanitem_detail_leftamount.setText(entity.getAccountYes()+"");
        loanitem_detail_name.setText(entity.getName());
        if(entity.getIsday() == 1){
            loanitem_detail_period.setText(entity.getTimeLimitDay()+"天");
        }else{
            loanitem_detail_period.setText(entity.getTimeLimit()+"个月");
        }
        loanitem_detail_leastamount.setText(entity.getEntryUnit()+"元");
        loanitem_detail_repayment.setText(entity.getBorrowStyle());
        loanitem_detail_profittime.setText(entity.getProfitTime());

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        GetData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

    }
//    private void BindTextView(JSONObject jsonObject){
//        try {
//            JSONArray array = jsonObject.getJSONArray("dataList");
//            JSONObject data = array.getJSONObject(0);
////            for(int i=0;i<array.length();i++){
////                JSONObject oj = array.getJSONObject(i);
////                Toast.makeText(getApplicationContext(),oj+"",Toast.LENGTH_SHORT).show();
////            }
//            loanitem_detail_rate.setText(data.getDouble("yield")+"");
//            loanitem_detail_totalamount.setText(data.getDouble("account")+"");
//            loanitem_detail_leftamount.setText(data.getDouble("accountYes")+"");
//            loanitem_detail_name.setText(data.getString("name"));
//            if(data.getInt("isday") == 1){
//                loanitem_detail_period.setText(data.getInt("timeLimitDay")+"天");
//            }else{
//                loanitem_detail_period.setText(data.getString("timeLimit")+"个月");
//            }
//            loanitem_detail_leastamount.setText(data.getDouble("entryUnit")+"元");
//            loanitem_detail_repayment.setText(data.getString("borrowStyle"));
//            loanitem_detail_profittime.setText(data.getString("profitTime"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }




}


