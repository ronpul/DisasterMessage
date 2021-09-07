package network;

import network.model.BaseResponse;
import network.model.MemberListData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("member/")
    Call<MemberListData> getMemberList();
}
