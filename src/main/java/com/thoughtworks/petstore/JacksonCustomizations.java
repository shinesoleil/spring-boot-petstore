package com.thoughtworks.petstore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.thoughtworks.petstore.core.order.LineItem;
import com.thoughtworks.petstore.core.order.Order;
import com.thoughtworks.petstore.core.pet.Pet;
import com.thoughtworks.petstore.core.user.User;
import com.thoughtworks.petstore.core.user.UserType;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.json.JsonSchema;
import org.springframework.data.rest.webmvc.json.JsonSchemaPropertyCustomizer;
import org.springframework.data.util.TypeInformation;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Configuration
public class JacksonCustomizations {

    @Bean
    public Module petStoreModules() {
        return new PetStoreModules();
    }

    @SuppressWarnings("serial")
    public static class PetStoreModules extends SimpleModule {

        public PetStoreModules() {
            addSerializer(Money.class, new MoneySerializer());
            addSerializer(DateTime.class, new DateTimeSerializer());
            addValueInstantiator(Money.class, new MoneyInstantiator());

            setMixInAnnotation(Pet.class, PetMixin.class);
            setMixInAnnotation(Order.class, OrderMixin.class);
            setMixInAnnotation(LineItem.class, LineItemMixin.class);
            setMixInAnnotation(User.class, UserMixin.class);
        }

        public interface UserMixin {
            @JsonProperty("type")
            public UserType getUserType();
        }

        @JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
        public interface LineItemMixin {
            @JsonSerialize(using = MoneySerializer.class)
            Money getPrice();

            @JsonIgnore
            long getId();
        }

        @JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
        static abstract class PetMixin {

            @JsonCreator
            public PetMixin(@JsonProperty("name") String name,
                            @JsonProperty("description") String description,
                            @JsonProperty("price") Money price,
                            @JsonProperty("quantity") int quantity) {
            }
        }

        @JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
        interface OrderMixin {

            @JsonProperty("price")
            Money getPrice();

            @JsonProperty("items")
            List<LineItem> getLineItems();

        }

        public static class DateTimeSerializer extends StdSerializer<DateTime> {

            protected DateTimeSerializer() {
                super(DateTime.class);
            }

            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                if (value == null) {
                    gen.writeNull();
                } else {
                    gen.writeString(value.toString());
                }
            }
        }

        public static class MoneySerializer extends StdSerializer<Money>
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

            public MoneySerializer() {
                super(Money.class);
            }

            @Override
            public void serialize(Money value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

                if (value != null) {
                    jgen.writeString(value.toString());
                } else {
                    jgen.writeNull();
                }
            }

            @Override
            public JsonSchema.JsonSchemaProperty customize(JsonSchema.JsonSchemaProperty property, TypeInformation<?> type) {
                return property.withType(String.class).withPattern(MONEY_PATTERN);
            }
        }

        static class MoneyInstantiator extends ValueInstantiator {

            @Override
            public String getValueTypeDesc() {
                return Money.class.toString();
            }

            @Override
            public boolean canCreateFromString() {
                return true;
            }

            @Override
            public Object createFromString(DeserializationContext context, String value) throws IOException {
                return Money.parse(value);
            }
        }
    }
}

