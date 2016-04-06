package magicaltechteam.com.mbaasautenticator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import magicaltechteam.com.mbaasautenticator.request.OtpRequest;
import magicaltechteam.com.mbaasautenticator.response.OtpResponse;
import magicaltechteam.com.mbaasautenticator.security.otp.Totp;
import retrofit2.*;

public class DisplayCodeActivity extends ActionBarActivity implements Callback<OtpResponse> {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String hash;
    private String secret;
    private String key;

    private TextView tvKey;
    private Button btnRevoke;

    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_code);

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");
        hash = bundle.getString("hash");
        secret = bundle.getString("secret");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        tvKey = (TextView) findViewById(R.id.tvKey);
        tvKey.setText(key);

        btnRevoke = (Button) findViewById(R.id.btnRevoke);
        btnRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(!isExit){
                    Log.i("timer", " running ... key is : "+key);
                    Log.i("hash", "hash : " + hash);

                    DisplayCodeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Totp totp = new Totp(hash);
                            tvKey.setText(totp.now());
                            Log.i("code", "code : " + totp.now());
                        }
                    });
                }

            }
        }, 0, 1000);
    }

    @Override
    public void onResponse(Call<OtpResponse> call, retrofit2.Response<OtpResponse> response) {
        Application application = (Application) getApplication();
        Gson gson = application.getGson();
        tvKey.setText(response.body().getData().getHash());
    }

    @Override
    public void onFailure(Call<OtpResponse> call, Throwable t) {
        Application application = (Application) getApplication();
        Gson gson = application.getGson();
        Log.i("GSON", t.getMessage());
    }

    @Override
    protected void onPause() {
        super.onPause();
        isExit = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isExit = false;
    }
}
