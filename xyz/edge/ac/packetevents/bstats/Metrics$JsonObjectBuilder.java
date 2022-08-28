package xyz.edge.ac.packetevents.bstats;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;

public static class JsonObjectBuilder
{
    private StringBuilder builder;
    private boolean hasAtLeastOneField;
    
    public JsonObjectBuilder() {
        this.builder = new StringBuilder();
        this.hasAtLeastOneField = false;
        this.builder.append("{");
    }
    
    private static String escape(final String value) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); ++i) {
            final char c = value.charAt(i);
            if (c == '\"') {
                builder.append("\\\"");
            }
            else if (c == '\\') {
                builder.append("\\\\");
            }
            else if (c <= '\u000f') {
                builder.append("\\u000").append(Integer.toHexString(c));
            }
            else if (c <= '\u001f') {
                builder.append("\\u00").append(Integer.toHexString(c));
            }
            else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
    
    public JsonObjectBuilder appendNull(final String key) {
        this.appendFieldUnescaped(key, "null");
        return this;
    }
    
    public JsonObjectBuilder appendField(final String key, final String value) {
        if (value == null) {
            throw new IllegalArgumentException("JSON value must not be null");
        }
        this.appendFieldUnescaped(key, "\"" + escape(value) + "\"");
        return this;
    }
    
    public JsonObjectBuilder appendField(final String key, final int value) {
        this.appendFieldUnescaped(key, String.valueOf(value));
        return this;
    }
    
    public JsonObjectBuilder appendField(final String key, final JsonObject object) {
        if (object == null) {
            throw new IllegalArgumentException("JSON object must not be null");
        }
        this.appendFieldUnescaped(key, object.toString());
        return this;
    }
    
    public JsonObjectBuilder appendField(final String key, final String[] values) {
        if (values == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        final String escapedValues = Arrays.stream(values).map(value -> "\"" + escape(value) + "\"").collect((Collector<? super Object, ?, String>)Collectors.joining(","));
        this.appendFieldUnescaped(key, "[" + escapedValues + "]");
        return this;
    }
    
    public JsonObjectBuilder appendField(final String key, final int[] values) {
        if (values == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        final String escapedValues = Arrays.stream(values).mapToObj((IntFunction<?>)String::valueOf).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
        this.appendFieldUnescaped(key, "[" + escapedValues + "]");
        return this;
    }
    
    public JsonObjectBuilder appendField(final String key, final JsonObject[] values) {
        if (values == null) {
            throw new IllegalArgumentException("JSON values must not be null");
        }
        final String escapedValues = Arrays.stream(values).map((Function<? super JsonObject, ?>)JsonObject::toString).collect((Collector<? super Object, ?, String>)Collectors.joining(","));
        this.appendFieldUnescaped(key, "[" + escapedValues + "]");
        return this;
    }
    
    private void appendFieldUnescaped(final String key, final String escapedValue) {
        if (this.builder == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        if (key == null) {
            throw new IllegalArgumentException("JSON key must not be null");
        }
        if (this.hasAtLeastOneField) {
            this.builder.append(",");
        }
        this.builder.append("\"").append(escape(key)).append("\":").append(escapedValue);
        this.hasAtLeastOneField = true;
    }
    
    public JsonObject build() {
        if (this.builder == null) {
            throw new IllegalStateException("JSON has already been built");
        }
        final JsonObject object = new JsonObject(this.builder.append("}").toString());
        this.builder = null;
        return object;
    }
    
    public static class JsonObject
    {
        private final String value;
        
        private JsonObject(final String value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return this.value;
        }
    }
}
