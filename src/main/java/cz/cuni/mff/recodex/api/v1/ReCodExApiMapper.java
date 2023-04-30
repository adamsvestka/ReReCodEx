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

public class ReCodExApiMapper extends ObjectMapper {
    private static final ReCodExApiMapper instance = new ReCodExApiMapper();

    private ReCodExApiMapper() {
        registerModule(new Module() {
            @Override
            public String getModuleName() {
                return "ReCodExApiModule";
            }

            @Override
            public Version version() {
                return Version.unknownVersion();
            }

            @Override
            public void setupModule(SetupContext context) {
                context.addDeserializers(new Deserializers.Base() {
                    @Override
                    public JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config,
                            BeanDescription beanDesc) throws JsonMappingException {
                        return new ReCodExApiDeserializer();
                    };
                });
            }
        });
    }

    public static ReCodExApiMapper getInstance() {
        return instance;
    }
}
