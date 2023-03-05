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

public class ReCodExApiDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
    private JavaType targetType;

    @Override
    public JsonDeserializer<Object> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        targetType = ctxt.getContextualType();
        return this;
    }

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
