package com.roni.library.contracts.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Universal RPC response envelope for NativeRuntime ecosystem.
 *
 * @author infoadmstudio2020-cyber
 */
public final class NativeApiResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String requestId;
    private final int statusCode;
    private final boolean success;
    private final Map<String, Object> data;
    private final NativeApiError error;
    private final long timestamp;

    public NativeApiResponse(@NonNull String requestId,
                             int statusCode,
                             boolean success,
                             @Nullable Map<String, Object> data,
                             @Nullable NativeApiError error,
                             long timestamp) {
        this.requestId = requestId != null ? requestId : "";
        this.statusCode = statusCode;
        this.success = success;
        this.data = data != null ? Collections.unmodifiableMap(new HashMap<>(data)) : Collections.emptyMap();
        this.error = error;
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
    }

    @NonNull
    public static NativeApiResponse success(@NonNull String requestId, @Nullable Map<String, Object> data) {
        return new NativeApiResponse(requestId, 200, true, data, null, System.currentTimeMillis());
    }

    @NonNull
    public static NativeApiResponse success(@NonNull String requestId) {
        return success(requestId, Collections.<String, Object>emptyMap());
    }

    @NonNull
    public static NativeApiResponse error(@NonNull String requestId, int statusCode, @NonNull NativeApiError error) {
        return new NativeApiResponse(requestId, statusCode, false, null, error, System.currentTimeMillis());
    }

    @NonNull
    public static NativeApiResponse error(@NonNull String requestId, @NonNull NativeApiError error) {
        return error(requestId, 500, error);
    }

    @NonNull
    public String getRequestId() {
        return requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    @NonNull
    public Map<String, Object> getData() {
        return data;
    }

    @Nullable
    public NativeApiError getError() {
        return error;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "NativeApiResponse{requestId='" + requestId + "', statusCode=" + statusCode + ", success=" + success + ", data=" + data + ", error=" + error + "}";
    }
}
