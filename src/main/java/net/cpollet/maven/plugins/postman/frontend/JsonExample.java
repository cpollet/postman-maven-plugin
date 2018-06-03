package net.cpollet.maven.plugins.postman.frontend;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.fasterxml.jackson.module.jsonSchema.types.NullSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonExample {
    private final JsonSchema schema;
    private final int level;
    private final Map<String, JsonSchema> schemas; // TODO static member?
    private final List<JsonSchema> parents; // TODO push/pop in static member?

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
        this(schema, 0, new HashMap<>(), new ArrayList<>());
    }

    private JsonExample(JsonSchema schema, int level, Map<String, JsonSchema> schemas, List<JsonSchema> parents) {
        this.schema = schema;
        this.level = level;
        this.schemas = schemas;
        this.parents = new ArrayList<>(parents);
        this.parents.add(schema);

        // only process from root schema, in order to avoid useless passes when processing sub-schemas later on
        if (level == 0) {
            fillSchemas(schema);
        }
    }

    private void fillSchemas(JsonSchema schema) {
        if (schema.getId() != null && !schemas.containsKey(schema.getId())) {
            schemas.put(schema.getId(), schema);
        }

        if (schema.isObjectSchema()) {
            Map<String, JsonSchema> properties = ((ObjectSchema) schema).getProperties();

            for (JsonSchema innerSchema : properties.values()) {
                fillSchemas(innerSchema);
            }
        }
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
                JsonExample content = child(((ArraySchema.SingleItems) items).getSchema());

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
                            child(e.getValue()).generate().trim() // trim because it does not start on its own line
                    ))
                    .map(s -> indent(s, 1))
                    .collect(Collectors.toList());

            return String.format("%s\n%s\n%s",
                    indent("{"),
                    String.join("\n", props),
                    indent("}"));
        }

        if (!schema.get$ref().isEmpty()) {
            JsonSchema referencedSchema = schemas.getOrDefault(schema.get$ref(), new NullSchema());

            if (parents.contains(referencedSchema)) {
                return "infinite recursion: " + schema.get$ref();
            }

            return new JsonExample(referencedSchema, level, schemas, parents).generate();
        }

        throw new IllegalArgumentException(schema.getClass().getCanonicalName() + " is not supported");
    }

    private JsonExample child(JsonSchema schema) {
        return new JsonExample(schema, level + 1, schemas, parents);
    }

    private String indent(String str) {
        return indent(str, 0);
    }

    private String indent(String str, int additionalLevels) {
        String spaces = String.join("", Collections.nCopies(level + additionalLevels, "  "));
        return spaces + str;
    }
}
