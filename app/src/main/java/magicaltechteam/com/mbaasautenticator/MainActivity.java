package magicaltechteam.com.mbaasautenticator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.zxing.Result;

import org.apache.commons.lang3.StringUtils;

import magicaltechteam.com.mbaasautenticator.request.OtpRequest;
import magicaltechteam.com.mbaasautenticator.response.OtpResponse;
import magicaltechteam.com.mbaasautenticator.security.otp.Totp;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.*;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, Callback<OtpResponse> {

    private final String TAG = "MainActivity";
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this); 
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        String texts[] = StringUtils.split(result.getText(), "||");
        String secret = texts[0];
        String hash = texts[1];
        Totp totp = new Totp(hash);
        Application application = (Application) getApplication();
        OtpRequest request = new OtpRequest();
        request.setSecret(secret);
        request.setOtp(totp.now());
        Call<OtpResponse> call = application.getClient().otp(request);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<OtpResponse> call, retrofit2.Response<OtpResponse> response) {
        Application application = (Application) getApplication();
        Gson gson = application.getGson();
        String hash = response.body().getData().getHash();
        Log.i("GSON", hash);
    }

    @Override
    public void onFailure(Call<OtpResponse> call, Throwable t) {
        Application application = (Application) getApplication();
        Gson gson = application.getGson();
        Log.i("GSON", t.getMessage());
    }
}
