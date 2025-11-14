package org.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class JsonNullableMapper{
    public <T> JsonNullable<T> wrap(T obj){
        return JsonNullable.of(obj);
    }

    public <T> T unwrap(JsonNullable<T> value){
        return value != null && value.isPresent() ? value.get() : null;
    }
}
