package com.example.administrator.demo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.demo.R;
import com.example.administrator.demo.entry.ListViewInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/4.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    ViewHolder viewHolder = null;
    private List<ListViewInfo.ListViewInfoEntity> listItems=new ArrayList<ListViewInfo.ListViewInfoEntity>();
//    public static Map<Integer,Object> map1=new ArrayMap<Integer,Object>();
//    public static Map<String,Boolean> map2=new ArrayMap<String,Boolean>();
    private LayoutInflater listContainer;
//    private boolean[] hasChecked;
    public ListViewAdapter(Context context){
        this.context=context;
        listContainer =LayoutInflater.from(context);
        this.listItems = listItems;
//        hasChecked = new boolean[getCount()];
    }
    public final class ViewHolder {
        public TextView name;
        public TextView yield;
        public TextView process;
//        public CheckBox checkItem;
    }
    public void setItems(List<ListViewInfo.ListViewInfoEntity> items){
        this.listItems.clear();
        this.listItems.addAll(items);

    }
    public void addItems(List<ListViewInfo.ListViewInfoEntity> items) {
        this.listItems.addAll(items);
    }
    @Override
    public int getCount() {
        return listItems.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }


//    private void saveMoney(int clickID,boolean isChecked) {
//        if(isChecked==true){
//            map1.put(clickID,listItems.get(clickID).get("money"));
//        }else{
//            map1.put(clickID,0);
//        }
//
//
//    }

//    private void saveCheckbox(int clickID,boolean status){
//        map2.put(clickID+"",status);
//    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("position", position + "");
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = listContainer.inflate(R.layout.list_item,null);  //创建list_item.xml布局文件的视图
            viewHolder.name= (TextView) convertView.findViewById(R.id.name_tv);
            viewHolder.yield= (TextView) convertView.findViewById(R.id.yield_tv);
            viewHolder.process= (TextView) convertView.findViewById(R.id.process_tv);
//            viewHolder.checkItem= (CheckBox) convertView.findViewById(R.id.checkItem);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ListViewInfo.ListViewInfoEntity  lv= listItems.get(position);
        viewHolder.name.setText(lv.getName("name"));
        viewHolder.yield.setText(lv.getYield("yield")+"%");
        viewHolder.process.setText(lv.getProcess("process")+"");

//        if(map2.size()!=0){
//            if(map2.get(position+"")!=null)
//               viewHolder.checkItem.setChecked(map2.get(position+""));
//            else
//                viewHolder.checkItem.setChecked(false);
//        }

//        viewHolder.checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                checkedChange(position);
//                saveMoney(position,isChecked);
//                saveCheckbox(position,isChecked);
//            }
//        });
        return convertView;
    }

//    private void checkedChange(int checkedID){
//
//        hasChecked[checkedID] = !hasChecked[checkedID];
//
//    }


}
