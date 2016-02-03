package com.example.administrator.demo.Fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.demo.Adapter.ListViewAdapter;
import com.example.administrator.demo.R;
import com.example.administrator.demo.Volley.RequestManager;
import com.example.administrator.demo.Volley.ServiceListener;
import com.example.administrator.demo.entry.ListViewInfo;
import com.example.administrator.demo.view.pullable.PullableListView;
import com.example.administrator.demo.view.pullable.PullableListViewLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListView_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListView_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListView_Fragment extends BaseFragment implements PullableListViewLayout.PullableListDataLoader{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private PullableListViewLayout pullableListViewLayout;
    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private List<ListViewInfo.ListViewInfoEntity> listItems;
    private PullableListViewLayout.PullableListDataLoader pullableListDataLoader;
    protected int pageNumber = 1;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment ListView_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListView_Fragment newInstance(String param1) {
        ListView_Fragment fragment = new ListView_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ListView_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_common_layout_pullable_list, container, false);
        pullableListViewLayout = (PullableListViewLayout) view
                .findViewById(R.id.pullable_list_view_layout);
        listView= (ListView) inflater.inflate(R.layout.fragment_list_view, container, false);
        return view;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void initView(){
        pullableListViewLayout.init(this, PullableListView.Mode.BOTH, PullableListView.Mode.PULL_FROM_END, PullableListView.Mode.PULL_FROM_START);
        pullableListViewLayout.setAdapter(listViewAdapter);
        listViewAdapter=new ListViewAdapter(getActivity());
//        listView = pullableListViewLayout.getPullableListView();
//        listView.setDivider(null);
//        listView.setDividerHeight(0);
//        listView.setAdapter(listViewAdapter);
        pullableListViewLayout.startLoading();

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
//    private List<Map<String,Object>> getListItems(){
//        List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
//        for(int i=0;i<time.length;i++){
//            Map<String,Object> map=new HashMap<String, Object>();
//            map.put("time",time[i]);
//            map.put("money",money[i]);
//            map.put("remark",remark[i]);
//            listItems.add(map);
//        }
//        return listItems;
//    }
@Override
   public void getDataList(final boolean refresh, boolean pullToRefresh){
       if(refresh||pullToRefresh){
           pageNumber=1;
       }else{
           pageNumber++;
       }
       HashMap<String,String> maps= RequestManager.getCommonMap();
       maps.put("currentPage", pageNumber+"");
       maps.put("borrowType", 301 + "");
       RequestManager.goRquest(getActivity(), "http://123.56.106.181/product/getBorrowList.html", maps, new ServiceListener() {
           @Override
               public void onResult(String result) throws JSONException {
               String section=getArguments().getString(ARG_PARAM1);
               ListViewInfo listViewInfo = JSONObject.parseObject(result, ListViewInfo.class);

               if (listViewInfo.getCode() == 0) {
                   listItems = listViewInfo.getDataList();
//                   listView.setAdapter(listViewAdapter);
                   if("1".equals(section+"")){
                           if(refresh){
                               listViewAdapter.setItems(listItems);
                           }else{
                               listViewAdapter.addItems(listItems);
                           }
                           listViewAdapter.notifyDataSetChanged();
                           if(pageNumber>=listViewInfo.getTotalPage())
                               pullableListViewLayout.onLoadSuccess(false);
                           else
                               pullableListViewLayout.onLoadSuccess(true);
                   }
                   else if("2".equals(section+"")){
                       if(refresh){
                           listViewAdapter.setItems(listItems);
                       }else{
                           listViewAdapter.addItems(listItems);
                       }
                       listViewAdapter.notifyDataSetChanged();
                       if(pageNumber>=listViewInfo.getTotalPage())
                           pullableListViewLayout.onLoadSuccess(false);
                       else
                           pullableListViewLayout.onLoadSuccess(true);

                   }else if("3".equals(section+"")){
                       if(refresh){
                           listViewAdapter.setItems(listItems);
                       }else{
                           listViewAdapter.addItems(listItems);
                       }
                       listViewAdapter.notifyDataSetChanged();
                       if(pageNumber>=listViewInfo.getTotalPage())
                           pullableListViewLayout.onLoadSuccess(false);
                       else
                           pullableListViewLayout.onLoadSuccess(true);

                   }

               }

           }

           @Override
           public void onException(Exception e) {
               pullableListViewLayout.onLoadFailed();
               Toast.makeText(getActivity(), "请求发送错误", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onFinish() {

           }

           @Override
           public void onNetworkError() {
               pullableListViewLayout.onNetworkError();
               Toast.makeText(getActivity(), "服务调用错误", Toast.LENGTH_SHORT).show();
           }
       });

   }

    @Override
    public void clearData() {

    }


}
