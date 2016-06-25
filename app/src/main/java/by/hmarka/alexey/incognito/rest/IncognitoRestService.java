package by.hmarka.alexey.incognito.rest;

import by.hmarka.alexey.incognito.entities.requests.PostsListRequest;
import by.hmarka.alexey.incognito.entities.requests.RegisterDeviceRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lashket on 25.6.16.
 */
public interface IncognitoRestService {
    /**
     * For example:
     * @POST("postList.xml")
     * Call<ResponseBody> getPostsList(@Body GetPostListRequest request);
     *
     * Using in any part of application:
     * Call<ResponesBody> call = RestClient.getServiceInstance.METHOD_NAME(MATHOD_APARAMETERS);
     * call.enqueque(new Callback);
     */

    @POST("registerDevice.xml")
    Call<ResponseBody> registerDevice(@Body RegisterDeviceRequest registerDeviceRequest);

    @POST("postList.xml")
    Call<ResponseBody> getPostsList(@Body PostsListRequest postsListRequest);
}