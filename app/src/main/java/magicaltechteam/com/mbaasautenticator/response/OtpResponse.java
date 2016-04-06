package magicaltechteam.com.mbaasautenticator.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import magicaltechteam.com.mbaasautenticator.Response;

public class OtpResponse extends Response<OtpResponse.Body> {

    public OtpResponse() {
        this.data = new Body();
    }

    public static class Body {

        @Expose
        @SerializedName("hash")
        private String hash;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }

}
