package net.cpollet.maven.plugins.postman.frontend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.types.AnySchema;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.fasterxml.jackson.module.jsonSchema.types.BooleanSchema;
import com.fasterxml.jackson.module.jsonSchema.types.IntegerSchema;
import com.fasterxml.jackson.module.jsonSchema.types.NullSchema;
import com.fasterxml.jackson.module.jsonSchema.types.NumberSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ReferenceSchema;
import com.fasterxml.jackson.module.jsonSchema.types.StringSchema;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                        "  \"privateKey\": \"string\",",
                        "  \"publicKey\": \"string\"",
                        "}"
                ));
    }

    @Test
    public void generate_whenReferenceSchema() {
        // GIVEN
        JsonSchema namedSchema = new StringSchema();
        namedSchema.setId("id");

        JsonSchema schema = new ObjectSchema();
        ((ObjectSchema) schema).setProperties(new HashMap<String, JsonSchema>() {{
            put("prop1", namedSchema);
            put("prop2", new ReferenceSchema("id"));
        }});

        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"prop1\": \"string\"")
                .contains("\"prop2\": \"string\"");
    }

    @Test
    public void generate_whenRecursiveReference() {
        // GIVEN
        JsonSchema schema = new ReferenceSchema("id");
        schema.setId("id");

        JsonExample example = new JsonExample(schema);

        // WHEN
        String result = example.generate();

        // THEN
        Assertions.assertThat(result)
        .isEqualTo("infinite recursion: id");
    }

    @Test
    public void generate_full() throws JsonProcessingException {
        // GIVEN
        JsonExample jsonExample = JsonExample.from(User.class);

        // WHEN
        String result = jsonExample.generate();

        // THEN
        Assertions.assertThat(result)
                .isEqualTo(String.join("\n",
                        "{",
                        "  \"username\": \"string\",",
                        "  \"rights\": [",
                        "    \"string\",",
                        "    ...",
                        "  ],",
                        "  \"key\": {",
                        "    \"privateKey\": \"string\",",
                        "    \"publicKey\": \"string\"",
                        "  },",
                        "  \"altKeys\": [",
                        "    {",
                        "      \"privateKey\": \"string\",",
                        "      \"publicKey\": \"string\"",
                        "    },",
                        "    ...",
                        "  ],",
                        "  \"parent\": infinite recursion: urn:jsonschema:net:cpollet:maven:plugins:postman:frontend:TestJsonExample:User",
                        "}"
                ));
    }

    @Data
    public static class Key {
        private String privateKey;
        private String publicKey;
    }

    @Data
    public static class User {
        private String username;
        private ArrayList<String> rights;
        private Key key;
        private List<Key> altKeys;
        private User parent;
    }
}
