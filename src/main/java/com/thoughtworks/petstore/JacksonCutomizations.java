package com.thoughtworks.petstore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.thoughtworks.petstore.core.pet.Pet;
import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.webmvc.json.JsonSchema;
import org.springframework.data.rest.webmvc.json.JsonSchemaPropertyCustomizer;
import org.springframework.data.util.TypeInformation;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.io.IOException;
import java.util.regex.Pattern;

@Configuration
class JacksonCustomizations {

    public @Bean
    Module moneyModule() {
        return new MoneyModule();
    }

    public @Bean Module restbucksModule() {
        return new RestbucksModule();
    }

    @SuppressWarnings("serial")
    static class RestbucksModule extends SimpleModule {

        public RestbucksModule() {

            setMixInAnnotation(Pet.class, PetMixin.class);
        }

        @JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
        static abstract class PetMixin {

            @JsonCreator
            public PetMixin(@JsonProperty("name") String name,
                            @JsonProperty("description") String description,
                            @JsonProperty("price") MonetaryAmount price,
                            @JsonProperty("quantity") int quantity) {}
        }
    }

    @SuppressWarnings("serial")
    static class MoneyModule extends SimpleModule {

        public MoneyModule() {

            addSerializer(MonetaryAmount.class, new MonetaryAmountSerializer());
            addValueInstantiator(MonetaryAmount.class, new MoneyInstantiator());
        }

        /**
         * A dedicated serializer to render {@link MonetaryAmount} instances as formatted {@link String}. Also implements
         * {@link JsonSchemaPropertyCustomizer} to expose the different rendering to the schema exposed by Spring Data REST.
         *
         * @author Oliver Gierke
         */
        static class MonetaryAmountSerializer extends StdSerializer<MonetaryAmount>
            implements JsonSchemaPropertyCustomizer {

            private static final Pattern MONEY_PATTERN;

            static {

                StringBuilder builder = new StringBuilder();

                builder.append("(?=.)^"); // Start
                builder.append("[A-Z]{3}?"); // 3-digit currency code
                builder.append("\\s"); // single whitespace character
                builder.append("(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?"); // digits with optional grouping by "," every 3)
                builder.append("(\\.[0-9]{1,2})?$"); // End with a dot and two digits

                MONEY_PATTERN = Pattern.compile(builder.toString());
            }

            public MonetaryAmountSerializer() {
                super(MonetaryAmount.class);
            }

            /*
             * (non-Javadoc)
             * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
             */
            @Override
            public void serialize(MonetaryAmount value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

                if (value != null) {
                    jgen.writeString(MonetaryFormats.getAmountFormat(LocaleContextHolder.getLocale()).format(value));
                } else {
                    jgen.writeNull();
                }
            }

            /*
             * (non-Javadoc)
             * @see org.springframework.data.rest.webmvc.json.JsonSchemaPropertyCustomizer#customize(org.springframework.data.rest.webmvc.json.JsonSchema.JsonSchemaProperty, org.springframework.data.util.TypeInformation)
             */
            @Override
            public JsonSchema.JsonSchemaProperty customize(JsonSchema.JsonSchemaProperty property, TypeInformation<?> type) {
                return property.withType(String.class).withPattern(MONEY_PATTERN);
            }
        }

        static class MoneyInstantiator extends ValueInstantiator {

            /*
             * (non-Javadoc)
             * @see com.fasterxml.jackson.databind.deser.ValueInstantiator#getValueTypeDesc()
             */
            @Override
            public String getValueTypeDesc() {
                return MonetaryAmount.class.toString();
            }

            /*
             * (non-Javadoc)
             * @see com.fasterxml.jackson.databind.deser.ValueInstantiator#canCreateFromString()
             */
            @Override
            public boolean canCreateFromString() {
                return true;
            }

            /*
             * (non-Javadoc)
             * @see com.fasterxml.jackson.databind.deser.ValueInstantiator#createFromString(com.fasterxml.jackson.databind.DeserializationContext, java.lang.String)
             */
            @Override
            public Object createFromString(DeserializationContext context, String value) throws IOException {
                return Money.parse(value, MonetaryFormats.getAmountFormat(LocaleContextHolder.getLocale()));
            }
        }
    }
}

