package magicaltechteam.com.mbaasautenticator.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import magicaltechteam.com.mbaasautenticator.Request;

/**
 * Created by socheat on 4/6/16.
 */
public class OtpRequest extends Request {
    @Expose
    @SerializedName("secret")
    private String secret;

    @Expose
    @SerializedName("otp")
    private String otp;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
