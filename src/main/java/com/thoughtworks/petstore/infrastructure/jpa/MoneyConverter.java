package com.thoughtworks.petstore.infrastructure.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<org.joda.money.Money, String> {

    @Override
    public String convertToDatabaseColumn(org.joda.money.Money attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public org.joda.money.Money convertToEntityAttribute(String dbData) {
        return dbData == null ? null : org.joda.money.Money.parse(dbData);
    }
}
