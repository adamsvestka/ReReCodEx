package cz.cuni.mff.recodex.api.v1;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.Deserializers;

/**
 * ReCodExApiMapper class is an extension of the Jackson ObjectMapper, intended
 * to provide custom deserialization for the ReCodEx API. It's a singleton
 * class, so it has one instance only.
 * 
 * @see com.fasterxml.jackson.databind.ObjectMapper ObjectMapper
 * @see com.fasterxml.jackson.databind.deser.Deserializers Deserializers
 */
public class ReCodExApiMapper extends ObjectMapper {
    private static final ReCodExApiMapper instance = new ReCodExApiMapper();

    /**
     * Private constructor to prevent multiple instances of the class and constructs
     * an ObjectMapper
     * with custom deserializers for the ReCodEx API.
     */
    private ReCodExApiMapper() {
        registerModule(new Module() {
            /**
             * Returns the name of this module.
             *
             * @return the name of the module
             */
            @Override
            public String getModuleName() {
                return "ReCodExApiModule";
            }

            /**
             * Returns the version of this module.
             *
             * @return the version of the module
             */
            @Override
            public Version version() {
                return Version.unknownVersion();
            }

            /**
             * Configures this module for ReCodEx API with custom deserializers.
             *
             * @param context Setup context used for configuration
             */
            @Override
            public void setupModule(SetupContext context) {
                context.addDeserializers(new Deserializers.Base() {
                    /**
                     * Returns a custom deserializer for the ReCodEx API.
                     *
                     * @param type     the expected type of the deserialized value
                     * @param config   the deserialization configuration
                     * @param beanDesc the bean description for the type
                     * @return a custom deserializer for the ReCodEx API
                     * @throws JsonMappingException if the deserializer cannot be found
                     */
                    @Override
                    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config,
                            BeanDescription beanDesc) throws JsonMappingException {
                        return new ReCodExApiDeserializer();
                    };
                });
            }
        });
    }

    /**
     * Returns the singleton instance of the ReCodExApiMapper.
     *
     * @return the singleton instance
     */
    public static ReCodExApiMapper getInstance() {
        return instance;
    }
}