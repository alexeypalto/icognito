package by.hmarka.alexey.incognito.rest;

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


}
