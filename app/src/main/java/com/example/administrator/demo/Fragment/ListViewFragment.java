package com.example.administrator.demo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.demo.Adapter.ListViewAdapter;
import com.example.administrator.demo.Adapter.Listview_FragmentPagerAdapter;
import com.example.administrator.demo.Fragment.BaseFragment;
import com.example.administrator.demo.Fragment.ListView_Fragment;
import com.example.administrator.demo.R;
import com.example.administrator.demo.activity.TitleView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

public class ListViewFragment extends BaseFragment {
    @ViewInject(R.id.tv1)
    private TextView tv1;
    @ViewInject(R.id.tv2)
    private TextView tv2;
    @ViewInject(R.id.tv3)
    private TextView tv3;
    @ViewInject(R.id.iv_bottom_line)
    private ImageView iv_bottom_line;
    @ViewInject(R.id.check_btn)
    private Button check_btn;
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.title)
    private TitleView title;
    private ArrayList<Fragment> fragmentList;
    private int currIndex;//当前页卡编号
    private int offset;//图片移动的偏移量
    private int screenWidth;
    private int tabcount=3;
    private ListViewAdapter listViewAdapter;
    private FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_list_view,container,false);
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //        Map<String,Object> map=new HashMap<String,Object>();
//        List<Map<String, String>> data = getSource();
        mActivity = getActivity();
        ViewUtils.inject(this,getView());
        initTitle();
//        InitImage();
        initTabLineWidth();
        InitViewPager();
//        listView= (ListView) findViewById(R.id.list);

//        check_btn.setOnClickListener(new Listener(4));

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
        title.setBTitle("产品列表");
    }

    @OnClick(R.id.check_btn)
    public void onclick(View view){
//        Intent intent=new Intent();
//        Set keySet = listViewAdapter.map1.keySet();
//        Iterator it = keySet.iterator();
//        String str="";
//        while(it.hasNext()){
//            Object k = it.next();
//            Object p = listViewAdapter.map1.get(k);
//            str=str+p.toString()+".";
//        }
//            Bundle bundle=new Bundle();
//            bundle.putStringArray("money_list",money_list);
//            intent.putExtras(bundle);
//        intent.putExtra("money",str);
//        setResult(1, intent);
//        finish();
    }
    @OnClick({R.id.tv1,R.id.tv2,R.id.tv3})
    public void onclick1(View view){
        switch (view.getId()){
            case R.id.tv1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tv3:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }

    }

    public void InitViewPager(){
        fragmentList=new ArrayList<Fragment>();
        Fragment fragment1= ListView_Fragment.newInstance("1");
        Fragment fragment2=ListView_Fragment.newInstance("2");
        Fragment fragment3= ListView_Fragment.newInstance("3");
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        viewPager.setAdapter(new Listview_FragmentPagerAdapter(mActivity.getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);//设置当前显示标签页为第一页
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv_bottom_line
                        .getLayoutParams();

                /**
                 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
                 * 设置mTabLineIv的左边距 滑动场景：
                 * 记3个页面,
                 * 从左到右分别为0,1,2
                 * 0->1; 1->2; 2->1; 1->0
                 */
                if (currIndex <= position) {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabcount) + currIndex
                            * (screenWidth / tabcount));
                } else {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / tabcount) + currIndex
                            * (screenWidth / tabcount));
                }
                iv_bottom_line.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                currIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        this.getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv_bottom_line
                .getLayoutParams();
        lp.width = screenWidth / tabcount;
        iv_bottom_line.setLayoutParams(lp);
    }
//    public void SaveCheckBox(int clickID,String step){
//            SharedPreferences sp;
//            SharedPreferences.Editor editor;
//            sp=getSharedPreferences(clickID+"",0);
//            editor=sp.edit();
//            editor.putBoolean("ischecked",false);
//            editor.commit();
//
//
//        }

    }


