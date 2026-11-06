package com.roni.library.router;

import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiResponse;

import java.io.Serializable;
import java.util.Map;

/**
 * High-level routing response model.
 *
 * @author infoadmstudio2020-cyber
 */
public class RouteResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String requestId;
    private final int statusCode;
    private final boolean success;
    private final Map<String, Object> data;
    private final NativeApiError error;
    private final long timestamp;

    public RouteResponse(NativeApiResponse apiResponse) {
        if (apiResponse != null) {
            this.requestId = apiResponse.getRequestId();
            this.statusCode = apiResponse.getStatusCode();
            this.success = apiResponse.isSuccess();
            this.data = apiResponse.getData();
            this.error = apiResponse.getError();
            this.timestamp = apiResponse.getTimestamp();
        } else {
            this.requestId = "";
            this.statusCode = 500;
            this.success = false;
            this.data = null;
            this.error = new NativeApiError("NULL_RESPONSE", "Response was null");
            this.timestamp = System.currentTimeMillis();
        }
    }

    public static RouteResponse from(NativeApiResponse apiResponse) {
        return new RouteResponse(apiResponse);
    }

    public String getRequestId() {
        return requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public NativeApiError getError() {
        return error;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
