package com.alps.common.oauth2.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
@Slf4j
public class AlpsOAuth2ExceptionSerializer extends StdSerializer<AlpsOAuth2Exception> {

    public AlpsOAuth2ExceptionSerializer() {
        super(AlpsOAuth2Exception.class);
    }

    @Override
    public void serialize(AlpsOAuth2Exception ex, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("message", ex.getMessage());
        gen.writeStringField("data", "");
        gen.writeNumberField("timestamp", System.currentTimeMillis());
        if (ex.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : ex.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                if ("code".equals(key)) {
                    gen.writeNumberField(key, new BigDecimal(add));
                } else {
                    gen.writeStringField(key, add);
                }
            }
        }
        gen.writeEndObject();
    }
}