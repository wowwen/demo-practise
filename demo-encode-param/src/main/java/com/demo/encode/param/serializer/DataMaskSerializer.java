package com.demo.encode.param.serializer;

import com.alibaba.fastjson.asm.Type;
import com.demo.encode.param.annotation.DataMask;
import com.demo.encode.param.enums.DataMaskingEnum;
import com.demo.encode.param.interf.DataMaskingOperation;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.util.Objects;

public final class DataMaskSerializer extends StdScalarSerializer {

    private final DataMaskingOperation operation;

    public DataMaskSerializer() {
        super(String.class, false);
        this.operation = null;
    }

    public DataMaskSerializer(DataMaskingOperation operation) {
        super(String.class);
        this.operation = operation;
    }

    public boolean isEmpty(SerializerProvider provider, Object value){
        String str = (String) value;
        return str.isEmpty();
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (Objects.isNull(provider)){
            String content = DataMaskingEnum.ALL_MASK.getOperation().mask((String) value, null);
            gen.writeString(content);
        }else {
            String content = operation.mask((String) value, null);
            gen.writeString(content);
        }
    }

    public final void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        this.serialize(value, gen, provider);
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint){
        return this.createSchemaNode("string", true);
    }

    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitorWrapper, JavaType typeHint) throws JsonMappingException {
        this.visitStringFormat(visitorWrapper, typeHint);
    }
}
