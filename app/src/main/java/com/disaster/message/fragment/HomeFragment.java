package com.disaster.message.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.disaster.message.R;

import network.RetroCallback;
import network.RetroClient;
import network.model.BaseResponse;
import network.model.MemberListData;

public class HomeFragment extends Fragment {

    TextView getMemeberResultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMemeberResultTextView = view.findViewById(R.id.getMemeberResultTextView);
        getMemberList();
    }

    private void getMemberList() {
        RetroClient.getApiInterface().getMemberList().enqueue(new RetroCallback<MemberListData>() {
            @Override
            public void sendRequestSuccess(BaseResponse receivedData) {
                MemberListData result = (MemberListData) receivedData;
                getMemeberResultTextView.setText("get memberList result : " + result.memberList.size());
            }

            @Override
            public void sendRequestFail(BaseResponse response) {
            }
        });
    }
}
