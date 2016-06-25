package by.hmarka.alexey.incognito.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import by.hmarka.alexey.incognito.utils.Constants;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static IncognitoRestService serviceInstance;

    public static IncognitoRestService getServiceInstance()  {
        if (serviceInstance == null) {
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request request = chain.request();
                    request = request.newBuilder().build();
                    Response response = chain.proceed(request);
                    return response;
                }
            };

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.interceptors().add(interceptor);
            builder.readTimeout(10, TimeUnit.MINUTES);
            builder.writeTimeout(10, TimeUnit.MINUTES);
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            builder.interceptors().add(logging);

            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            serviceInstance = retrofit.create(IncognitoRestService.class);
        }
        return serviceInstance;
    }
}
