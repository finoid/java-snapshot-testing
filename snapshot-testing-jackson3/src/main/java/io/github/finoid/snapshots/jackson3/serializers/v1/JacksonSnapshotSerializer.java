package io.github.finoid.snapshots.jackson3.serializers.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.Jackson3JsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.Jackson3MappingProvider;
import io.github.finoid.snapshots.Snapshot;
import io.github.finoid.snapshots.SnapshotSerializerContext;
import io.github.finoid.snapshots.annotations.Mask;
import io.github.finoid.snapshots.exceptions.SnapshotExtensionException;
import io.github.finoid.snapshots.serializers.SerializerType;
import io.github.finoid.snapshots.serializers.SnapshotSerializer;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.core.util.Separators;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.NullNode;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.node.StringNode;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("checkstyle:all") // TODO (nw) rewrite
public class JacksonSnapshotSerializer implements SnapshotSerializer {
    static DefaultPrettyPrinter.Indenter lfOnlyIndenter = new DefaultIndenter("  ", "\n");

    private static final DefaultPrettyPrinter pp = new DefaultPrettyPrinter() {
        {
            this.indentArraysWith(lfOnlyIndenter);
            this.indentObjectsWith(lfOnlyIndenter);

            final Separators separators = Separators.createDefaultInstance()
                .withRootSeparator("");
            this.withSeparators(separators);
        }

        // It's a requirement
        // @see https://github.com/FasterXML/jackson-databind/issues/2203
        public DefaultPrettyPrinter createInstance() {
            return new DefaultPrettyPrinter(this);
        }
    };

    private final JsonMapper jsonMapper = createMapper();

    private JsonMapper createMapper() {
        JsonMapper.Builder builder =
            JsonMapper.builder()
                .defaultPrettyPrinter(pp)
                .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
                .enable(DateTimeFeature.WRITE_DATES_WITH_ZONE_ID)
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .changeDefaultPropertyInclusion(incl -> incl.withContentInclusion(JsonInclude.Include.NON_NULL))
                .changeDefaultVisibility(visibility ->
                    visibility.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

        if (shouldFindAndRegisterModules()) {
            builder.findAndAddModules();
        }

        configure(builder);

        return builder.build();
    }

    /**
     * Override to customize the Jackson jsonMapper
     *
     * @param builder the builder to be built
     */
    public void configure(final JsonMapper.Builder builder) {
    }

    /**
     * Override to control the registration of all available jackson2 modules within the classpath
     * which are locatable via JDK ServiceLoader facility, along with module-provided SPI.
     */
    protected boolean shouldFindAndRegisterModules() {
        return true;
    }

    @Override
    public Snapshot apply(final Object object, final SnapshotSerializerContext gen) {
        try {
            final JsonNode rootNode = jsonMapper.valueToTree(Collections.singletonList(object));
            final JsonNode maskedNode = applyMasking(object, rootNode, gen);

            final String body = jsonMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(maskedNode);
            return gen.toSnapshot(body);
        } catch (final Exception e) {
            if (isCycleError(e)) {
                try {
                    final JsonNode safeNode = toSafeJsonNode(object, new IdentityHashMap<>());
                    final JsonNode rootNode = jsonMapper.valueToTree(Collections.singletonList(safeNode));
                    final JsonNode maskedNode = applyMasking(object, rootNode, gen);
                    final String body = jsonMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(maskedNode);
                    return gen.toSnapshot(body);
                } catch (Exception ex) {
                    throw new SnapshotExtensionException("Jackson Serialization failed even with circular reference handling", ex);
                }
            }
            throw new SnapshotExtensionException("Jackson Serialization failed", e);
        }
    }

    private JsonNode applyMasking(Object object, JsonNode root, SnapshotSerializerContext gen) {
        Map<String, Object> masks = new LinkedHashMap<>();
        masks.putAll(findMasks(object));
        if (gen.getMasks() != null) {
            for (Map.Entry<String, Object> entry : gen.getMasks().entrySet()) {
                masks.put(ensurePathPrefix(entry.getKey()), entry.getValue());
            }
        }

        if (masks.isEmpty()) {
            return root;
        }

        Configuration conf = Configuration.builder()
            .jsonProvider(new Jackson3JsonNodeJsonProvider())
            .mappingProvider(new Jackson3MappingProvider())
            .build();

        DocumentContext ctx = JsonPath.using(conf).parse(root);

        for (Map.Entry<String, Object> entry : masks.entrySet()) {
            try {
                ctx.set(entry.getKey(), entry.getValue());
            } catch (Exception ex) {
                // ignore if path not found
            }
        }

        return (JsonNode) ctx.json();
    }

    private Map<String, Object> findMasks(Object object) {
        Map<String, Object> masks = new LinkedHashMap<>();
        if (object == null) {
            return masks;
        }

        Class<?> clazz = object.getClass();
        
        // Class level
        for (Mask m : clazz.getAnnotationsByType(Mask.class)) {
            masks.put(ensurePathPrefix(m.value()), m.maskValue());
        }

        // Field level
        for (Field field : clazz.getDeclaredFields()) {
            for (Mask m : field.getAnnotationsByType(Mask.class)) {
                String path = m.value();
                if (path.isEmpty()) {
                    path = "$.." + field.getName();
                } else {
                    path = ensurePathPrefix(path);
                }
                masks.put(path, m.maskValue());
            }
        }

        return masks;
    }

    private String ensurePathPrefix(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }
        if (path.startsWith("$[")) {
            return path;
        }
        if (path.startsWith("$..")) {
            return path;
        }
        if (path.startsWith("$.")) {
            return "$[0]." + path.substring(2);
        }
        if (path.startsWith("$")) {
            if (path.length() == 1) return "$[0]";
            return "$[0]." + path.substring(1);
        }
        return "$[0]." + (path.startsWith(".") ? path.substring(1) : path);
    }

    private boolean isCycleError(Throwable e) {
        while (e != null) {
            if (e instanceof tools.jackson.core.exc.StreamConstraintsException) {
                if (e.getMessage() != null && e.getMessage().contains("Document nesting depth")) {
                    return true;
                }
            }
            if (e.getMessage() != null && (e.getMessage().contains("Direct self-reference") || e.getMessage().contains("Infinite recursion"))) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }

    private JsonNode toSafeJsonNode(Object obj, IdentityHashMap<Object, JsonNode> visited) {
        if (obj == null) {
            return NullNode.instance;
        }

        if (isSimpleType(obj)) {
            return jsonMapper.valueToTree(obj);
        }

        if (obj instanceof Optional) {
            return toSafeJsonNode(((Optional<?>) obj).orElse(null), visited);
        }

        // Avoid infinite recursion: check visited map
        if (visited.containsKey(obj)) {
            return new StringNode("[CIRCULAR_REF]");
        }

        // Mark as visited with a placeholder
        visited.put(obj, NullNode.instance);

        // Collections / arrays
        if (obj instanceof Collection) {
            ArrayNode arrayNode = jsonMapper.createArrayNode();
            visited.put(obj, arrayNode);
            
            Collection<?> col = (Collection<?>) obj;
            if (shouldSortCollections()) {
                col = sort(col);
            }
            
            for (Object item : col) {
                arrayNode.add(toSafeJsonNode(item, visited));
            }
            return arrayNode;
        }

        if (obj.getClass().isArray()) {
            ArrayNode arrayNode = jsonMapper.createArrayNode();
            visited.put(obj, arrayNode);
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                arrayNode.add(toSafeJsonNode(Array.get(obj, i), visited));
            }
            return arrayNode;
        }

        if (obj instanceof Map) {
            ObjectNode mapNode = jsonMapper.createObjectNode();
            visited.put(obj, mapNode);
            Map<?, ?> map = (Map<?, ?>) obj;
            // Use TreeMap to ensure deterministic order if keys are comparable
            Map<String, Object> sortedMap = new TreeMap<>();
            for (Map.Entry<?, ?> e : map.entrySet()) {
                sortedMap.put(String.valueOf(e.getKey()), e.getValue());
            }
            for (Map.Entry<String, Object> e : sortedMap.entrySet()) {
                mapNode.set(e.getKey(), toSafeJsonNode(e.getValue(), visited));
            }
            return mapNode;
        }

        // Fallback: treat as bean — use reflection to get fields (matching visibility configuration)
        ObjectNode node = jsonMapper.createObjectNode();
        visited.put(obj, node);
        
        // If it's a JDK class but not handled above, avoid reflection and use valueToTree
        if (obj.getClass().getName().startsWith("java.") || obj.getClass().getName().startsWith("javax.")) {
            try {
                return jsonMapper.valueToTree(obj);
            } catch (Exception e) {
                return new StringNode(obj.toString());
            }
        }

        Map<String, JsonNode> fields = new TreeMap<>();
        Class<?> current = obj.getClass();
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }
                if (fields.containsKey(field.getName())) {
                    continue; // Skip shadowed fields
                }
                
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    // Handle JsonInclude.Include.NON_NULL
                    if (value != null) {
                        fields.put(field.getName(), toSafeJsonNode(value, visited));
                    }
                } catch (IllegalAccessException e) {
                    // if field can't be read, skip it
                }
            }
            current = current.getSuperclass();
        }
        
        for (Map.Entry<String, JsonNode> entry : fields.entrySet()) {
            node.set(entry.getKey(), entry.getValue());
        }
        
        return node;
    }

    private boolean isSimpleType(Object obj) {
        return obj instanceof String ||
               obj instanceof Number ||
               obj instanceof Boolean ||
               obj instanceof Character ||
               obj instanceof Enum ||
               obj instanceof UUID ||
               obj instanceof Date ||
               obj instanceof Instant ||
               obj instanceof LocalDate ||
               obj instanceof LocalDateTime ||
               obj instanceof ZonedDateTime ||
               obj instanceof OffsetDateTime ||
               obj instanceof OffsetTime ||
               obj instanceof LocalTime;
    }

    protected boolean shouldSortCollections() {
        return false;
    }

    private Collection<?> sort(Collection<?> value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        try {
            return value.stream()
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());
        } catch (Exception ex) {
            return value;
        }
    }

    @Override
    public String getOutputFormat() {
        return SerializerType.JSON.name();
    }
}
