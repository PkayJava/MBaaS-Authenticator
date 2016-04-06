package magicaltechteam.com.mbaasautenticator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by socheat on 4/6/16.
 */
public class Application extends android.app.Application {

    private Client client;

    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ").create();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new UserAgentInterceptor(System.getProperty("http.agent")))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://pkayjava.ddns.net:9080/mbaas-server/")
                .addConverterFactory(GsonConverterFactory.create(this.gson))
                .client(httpClient)
                .callbackExecutor(Executors.newFixedThreadPool(5))
                .build();
        this.client = retrofit.create(Client.class);
    }

    public final Client getClient() {
        return this.client;
    }

    public final Gson getGson() {
        return this.gson;
    }

}
