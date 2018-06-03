package net.cpollet.maven.plugins.postman.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.AnySchema;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.fasterxml.jackson.module.jsonSchema.types.BooleanSchema;
import com.fasterxml.jackson.module.jsonSchema.types.IntegerSchema;
import com.fasterxml.jackson.module.jsonSchema.types.NullSchema;
import com.fasterxml.jackson.module.jsonSchema.types.NumberSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.fasterxml.jackson.module.jsonSchema.types.StringSchema;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class TestJsonExample {
    @Test
    public void generate_whenAnySchema() {
        // GIVEN
        JsonSchema schema = new AnySchema();
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo("{}");
    }

    @Test
    public void generate_whenStringSchema() {
        // GIVEN
        JsonSchema schema = new StringSchema();
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo("\"string\"");
    }

    @Test
    public void generate_whenNumberSchema() {
        // GIVEN
        JsonSchema schema = new NumberSchema();
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo("0");
    }

    @Test
    public void generate_whenIntegerSchema() {
        // GIVEN
        JsonSchema schema = new IntegerSchema();
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo("0");
    }

    @Test
    public void generate_whenBooleanSchema() {
        // GIVEN
        JsonSchema schema = new BooleanSchema();
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo("true");
    }

    @Test
    public void generate_whenNullSchema() {
        // GIVEN
        JsonSchema schema = new NullSchema();
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo("null");
    }

    @Test
    public void generate_whenArraySchema() {
        // GIVEN
        JsonSchema schema = new ArraySchema();
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo("[]");
    }

    @Test
    public void generate_whenArraySchemaOfString() {
        // GIVEN
        JsonSchema schema = new ArraySchema();
        ((ArraySchema) schema).setItemsSchema(new StringSchema());
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo(String.join("\n",
                        "[",
                        "  \"string\",",
                        "  ...",
                        "]"
                ));
    }

    @Test
    public void generate_whenArraySchemaOfArrayOfInteger() {
        // GIVEN
        JsonSchema innerSchema = new ArraySchema();
        ((ArraySchema) innerSchema).setItemsSchema(new IntegerSchema());

        JsonSchema schema = new ArraySchema();
        ((ArraySchema) schema).setItemsSchema(innerSchema);

        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo(String.join("\n",
                        "[",
                        "  [",
                        "    0,",
                        "    ...",
                        "  ],",
                        "  ...",
                        "]"
                ));
    }

    @Test
    public void generate_whenObjectSchema() {
        // GIVEN
        JsonSchema schema = new ObjectSchema();
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo("{}");
    }

    @Test
    public void generate_whenObjectSchemaWithStringProperty() {
        // GIVEN
        JsonSchema schema = new ObjectSchema();
        ((ObjectSchema) schema).setProperties(new HashMap<String, JsonSchema>() {{
            put("myString", new StringSchema());
        }});
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo(String.join("\n",
                        "{",
                        "  \"myString\": \"string\"",
                        "}"
                ));
    }

    @Test
    public void generate_whenObjectSchemaWithNestedStringProperty() {
        // GIVEN
        JsonSchema innerSchema = new ArraySchema();
        ((ArraySchema) innerSchema).setItemsSchema(new IntegerSchema());

        JsonSchema schema = new ObjectSchema();
        ((ObjectSchema) schema).setProperties(new HashMap<String, JsonSchema>() {{
            put("myIntArray", innerSchema);
        }});
        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo(String.join("\n",
                        "{",
                        "  \"myIntArray\": [",
                        "    0,",
                        "    ...",
                        "  ]",
                        "}"
                ));
    }

    @Test
    public void from_createsNewJsonExample_fromClass() {
        // GIVEN

        // WHEN
        JsonExample jsonExample = JsonExample.from(Key.class);

        // THEN
        Assertions.assertThat(jsonExample.generate())
                .contains("privateKey")
                .contains("publicKey");
    }

    @Test
    public void generate_fromClass() {
        // GIVEN
        JsonExample jsonExample = JsonExample.from(Key.class);

        // WHEN
        String result = jsonExample.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo(String.join("\n",
                        "{",
                        "  \"privateKey\": \"string\"",
                        "  \"publicKey\": \"string\"",
                        "}"
                ));
    }

    @Data
    public static class Key {
        private String privateKey;
        private String publicKey;
    }
}
