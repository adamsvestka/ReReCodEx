package cz.cuni.mff.recodex.api.v1;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Exception thrown when the ReCodExAPI returns an error.
 * This class extends the RuntimeException and is used to represent
 * errors that occur during the interaction with the ReCodEx API.
 *
 * @see cz.cuni.mff.recodex.api.v1.ReCodExApiDeserializer
 *      ReCodExApiDeserializer
 */
public class ReCodExApiException extends RuntimeException {
    /** Indicates whether the request was successful. */
    public boolean success;
    /** The HTTP status code. */
    public long code;
    /** The error object containing the error information. */
    public Error error;

    /**
     * The inner class Error represents an error object containing
     * information about the error that occurred.
     */
    public static class Error {
        /** The error message, providing a description of the error. */
        public String message;
        /** The error code, indicating the specific error that occurred. */
        public String code;
        /** Any additional parameters related to the error. */
        public JsonNode parameters;
    }

    /**
     * Returns a message describing the error.
     *
     * @return a string containing the error code and error message.
     */
    @Override
    public String getMessage() {
        return error.code + ": " + error.message;
    }
}