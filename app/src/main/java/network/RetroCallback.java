package network;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import com.disaster.message.DMApplication;
import com.disaster.message.R;

import network.model.BaseResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.CustomLogger;

public abstract class RetroCallback<T> implements Callback<T> {

    public abstract void sendRequestSuccess(BaseResponse receivedData);
    public abstract void sendRequestFail(BaseResponse response);

    @Override
    public void onResponse(final Call<T> call, Response<T> response) {
        if (!response.isSuccessful()) {
            sendCustomResultFail(call);
            try {
                response.errorBody().close();
            } catch (Exception e) {
            }
        } else {
            final BaseResponse body = (BaseResponse) response.body();
            try {
                if (body.code == 1) {
                    sendRequestSuccess(body);
                } else {
                    if (TextUtils.isEmpty(body.message) == false) {
                        showToast(body.message);
                    }
                    sendRequestFail(body);
                }
            } catch (Exception e) {
                CustomLogger.logException(e);
            }
        }
    }

    private void showToast(String mesasge) {
        try {
            Context context = DMApplication.getContext();
            Toast.makeText(context, mesasge, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            CustomLogger.logException(e);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof NetworkNotConnectedException) {
            showToast(DMApplication.getContext().getString(R.string.check_network));
        }
//        CustomLogger.error(t.getLocalizedMessage());
        sendCustomResultFail(call);
    }

    private void sendCustomResultFail(Call<T> call) {
        sendRequestFail(new BaseResponse());
    }
}
