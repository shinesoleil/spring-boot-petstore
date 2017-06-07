package com.thoughtworks.petstore.infrastructure.jpa;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.money.Money;
import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;


@Converter(autoApply = true)
public class DateConverter implements AttributeConverter<DateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(DateTime attribute) {
        return attribute == null ? null : new Timestamp(attribute.getMillis());
    }

    @Override
    public DateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData == null ? null : new DateTime(dbData.getTime());
    }
}
