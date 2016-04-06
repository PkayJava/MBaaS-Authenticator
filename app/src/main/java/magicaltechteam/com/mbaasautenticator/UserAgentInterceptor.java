package magicaltechteam.com.mbaasautenticator;

import java.io.IOException;

import okhttp3.*;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by socheat on 4/6/16.
 */
public class UserAgentInterceptor implements Interceptor {

    private final String userAgent;

    public UserAgentInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestWithUserAgent = originalRequest.newBuilder()
                .header("User-Agent", userAgent)
                .build();
        return chain.proceed(requestWithUserAgent);
    }
}
