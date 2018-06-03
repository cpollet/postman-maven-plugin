package net.cpollet.maven.plugins.postman.frontend;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class JsonExample {
    private final JsonSchema schema;
    private final int level;

    public static JsonExample from(Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);

        try {
            return new JsonExample(schemaGen.generateSchema(clazz));
        } catch (JsonMappingException e) {
            throw new IllegalArgumentException("Unable to generate schema for " + clazz.getCanonicalName(), e);
        }
    }

    public JsonExample(JsonSchema schema) {
        this(schema, 0);
    }

    public String generate() {
        if (schema.isAnySchema()) {
            return indent("{}");
        }

        if (schema.isNullSchema()) {
            return indent("null");
        }

        if (schema.isStringSchema()) {
            return indent("\"string\"");
        }

        if (schema.isNumberSchema()) {
            return indent("0");
        }

        if (schema.isBooleanSchema()) {
            return indent("true");
        }

        if (schema.isArraySchema()) {
            ArraySchema.Items items = ((ArraySchema) schema).getItems();
            if (items != null && items.isSingleItems()) {
                JsonExample content = new JsonExample(((ArraySchema.SingleItems) items).getSchema(), level + 1);

                return indent(
                        String.join("\n",
                                "[", // already indented by default
                                content.generate() + ",",
                                indent("...", 1),
                                indent("]")
                        )
                );
            }

            if (items != null && items.isArrayItems()) {
                throw new IllegalArgumentException(items.getClass().getCanonicalName() + " is not supported");
            }

            return indent("[]");
        }

        if (schema.isObjectSchema()) {
            Map<String, JsonSchema> properties = ((ObjectSchema) schema).getProperties();

            if (properties.isEmpty()) {
                return "{}";
            }

            List<String> props = properties.entrySet().stream()
                    .map(e -> String.format(
                            "\"%s\": %s",
                            e.getKey(),
                            new JsonExample(e.getValue(), level + 1).generate().trim()
                    ))
                    .map(s -> indent(s, 1))
                    .collect(Collectors.toList());


            return String.format("{\n%s\n}", String.join("\n", props));
        }

        // TODO handle ReferenceSchema

        throw new IllegalArgumentException(schema.getClass().getCanonicalName() + " is not supported");
    }

    private String indent(String str) {
        return indent(str, 0);
    }

    private String indent(String str, int additionalLevels) {
        String spaces = String.join("", Collections.nCopies(level + additionalLevels, "  "));
        return spaces + str;
    }
}
