package cz.cuni.mff.recodex.api.v1;

import com.fasterxml.jackson.databind.JsonNode;

public class ReCodExApiException extends RuntimeException {
    public boolean success;
    public long code;
    public Error error;

    public static class Error {
        public String message;
        public String code;
        public JsonNode parameters;
    }

    @Override
    public String getMessage() {
        return error.code + ": " + error.message;
    }
}
