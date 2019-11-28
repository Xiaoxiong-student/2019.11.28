package com.cch.mobileshop.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cch.mobileshop.R;
import com.cch.mobileshop.adapter.LeftAdapter;
import com.cch.mobileshop.adapter.RightAdapter;
import com.cch.mobileshop.bean.Left_layout_response;
import com.cch.mobileshop.bean.Right_layout_reponse;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FenLeiFragment extends Fragment {


    @BindView(R.id.left_recycle)
    RecyclerView left_recycle;
    @BindView(R.id.rigth_recycle)
    RecyclerView right_recycle;
    private List<Left_layout_response.DataBean> leftDatas;
    private ArrayList<Right_layout_reponse>rightDatas;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cata, container, false);
        ButterKnife.bind(this,view);
        initView();
        initData();
        return view;

    }


    //
    private void initData() {

        OkHttpUtils.get()
                .url("")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                       Gson gson= new Gson();
                      Left_layout_response left_layout_response= gson.fromJson(response, Left_layout_response.class);
                      List<Left_layout_response.DataBean>data=left_layout_response.getData();
                      if(data!=null&&data.size()!=0){
                          leftDatas.clear();
                          leftDatas.addAll(data);

                          leftAdapter.notifyDataSetChanged();

                          getSecondList(0);



                      }


                    }
                });


    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        left_recycle.setLayoutManager(linearLayoutManager);

        leftDatas = new ArrayList<>();
        leftAdapter = new LeftAdapter(getActivity(), leftDatas);

        leftAdapter.setOnItemClickLisner(new LeftAdapter.OnItemClickLisener(){
            @Override
            public void onItemClick(int pos) {


                getSecondList(pos);

            }
        });
        left_recycle.setAdapter(leftAdapter);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, OrientationHelper.VERTICAL, false);

        right_recycle.setLayoutManager(gridLayoutManager);

        rightDatas =new ArrayList<Right_layout_reponse>();

       rightAdapter = new RightAdapter(getActivity(),rightDatas);
        right_recycle.setAdapter(rightAdapter);


    }

    private void getSecondList(int pos) {
        Left_layout_response.DataBean dataBean = leftDatas.get(pos);
        int cat_id = dataBean.getCat_id();
        String url="http://10.10.16.73:8089/MobileShop/cat/parent/"+cat_id;
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Gson gson = new Gson();
                        Right_layout_reponse secondListResponse = gson.fromJson(response, Right_layout_reponse.class);
                      List<Right_layout_reponse.DataBean> data = secondListResponse.getData();

                        if(data!=null&&data.size()!=0){
                            rightDatas.clear();
                          // rightDatas.addAll(data);
                            rightAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
        
    }


