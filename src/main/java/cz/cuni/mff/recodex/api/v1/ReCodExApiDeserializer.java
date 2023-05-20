package cz.cuni.mff.recodex.api.v1;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * A custom deserializer for the ReCodEx API that checks for the
 * <code>success</code> field in the JSON response and either deserializes the
 * payload into the specified type or raises a ReCodExApiException if the
 * <code>success</code> field is false.
 */
public class ReCodExApiDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
    private JavaType targetType;

    /**
     * Returns itself with a contextual type set to deserialize the JSON payload.
     *
     * @param ctxt     the current deserialization context
     * @param property the optional bean property for the JSON payload
     * @return a contextual deserializer for deserializing the JSON payload
     * @throws JsonMappingException if an error occurs while creating the contextual
     *                              deserializer
     */
    @Override
    public JsonDeserializer<Object> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        targetType = ctxt.getContextualType();
        return this;
    }

    /**
     * Deserializes the JSON into an object based on the contextual type, or raises
     * a ReCodExApiException if the JSON contains a <code>success</code> field with
     * a false value.
     *
     * @param p    the JSON parser holding the JSON
     * @param ctxt the current deserialization context
     * @return the deserialized object
     * @throws IOException      if there is a problem reading the JSON
     * @throws JacksonException if there is an issue with Jackson during
     *                          deserialization
     */
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.readTree(p);
        if (root.get("success").asBoolean()) {
            return mapper.treeToValue(root, targetType);
        } else {
            throw mapper.treeToValue(root, ReCodExApiException.class);
        }
    }
}