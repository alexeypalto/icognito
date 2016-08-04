package by.hmarka.alexey.incognito.entities.requests;

/**
 * Created by Alexey on 04.08.2016.
 */
public class ConfigurationChangeRequest {

    private String imei;
    private String paramName;
    private String paramValue;


    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
