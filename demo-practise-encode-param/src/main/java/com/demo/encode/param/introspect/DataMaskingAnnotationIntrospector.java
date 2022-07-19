package com.demo.encode.param.introspect;


import com.demo.encode.param.annotation.DataMask;
import com.demo.encode.param.serializer.DataMaskSerializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;

public class DataMaskingAnnotationIntrospector extends NopAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated am) {
        DataMask annotation = am.getAnnotation(DataMask.class);
        if (annotation != null) {
            return new DataMaskSerializer(annotation.maskFunc().getOperation());
        }
        return null;
    }
}
