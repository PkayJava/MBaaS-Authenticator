package magicaltechteam.com.mbaasautenticator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String OTP_KEY = "key";
    private static final String OTP_SECRET = "secret";
    private static final String OTP_HASH = "hash";

    private ZXingScannerView mScannerView;

    private String hash;
    private String secret;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this); 
        setContentView(mScannerView);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        // get key from SharePreferences
        key = preferences.getString(OTP_KEY, "");
        hash = preferences.getString(OTP_HASH, "");
        secret = preferences.getString(OTP_SECRET, "");

        if(!key.equals("") && key != null){
            Intent intent = new Intent(MainActivity.this, DisplayCodeActivity.class);
            intent.putExtra("key", key);
            intent.putExtra("hash", hash);
            intent.putExtra("secret", secret);
            startActivity(intent);
            finish();
        }

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
        secret = texts[0];
        hash = texts[1];
        
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
        key = response.body().getData().getHash();

        if(!key.equals("") && key !=null){
            Log.i("key", key);
            Log.i("hash", hash);
            Log.i("secret", secret);
            editor.putString(OTP_KEY, key);
            editor.putString(OTP_SECRET, secret);
            editor.putString(OTP_HASH, hash);
            editor.commit();

            Intent intent = new Intent(MainActivity.this, DisplayCodeActivity.class);
            intent.putExtra("key", key);
            intent.putExtra("hash", hash);
            intent.putExtra("secret", secret);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(MainActivity.this, "QR Code is expired", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<OtpResponse> call, Throwable t) {
        Application application = (Application) getApplication();
        Gson gson = application.getGson();
        Log.i("GSON", t.getMessage());
    }
}

