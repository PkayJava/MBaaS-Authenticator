package magicaltechteam.com.mbaasautenticator;

import magicaltechteam.com.mbaasautenticator.request.OtpRequest;
import magicaltechteam.com.mbaasautenticator.response.OtpResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by socheat on 4/6/16.
 */
public interface Client {

    @POST("api/otp")
    Call<OtpResponse> otp(@Body OtpRequest request);

}
