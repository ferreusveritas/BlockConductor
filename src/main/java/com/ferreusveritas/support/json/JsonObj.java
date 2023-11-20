package com.ferreusveritas.support.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.*;

/**
 *
 * A JSON object handling library that uses {@link Optional} where appropriate.
 * This library is type-safe "enough" and not altogether less type-safe than Jackson.
 *
 * @version 2.0.0
 */
public class JsonObj implements Iterable<JsonObj> {
    
    private static final Logger LOG = LoggerFactory.getLogger(JsonObj.class);
    
    ////////////////////////////////////////////////////////////////
    // Constants
    ////////////////////////////////////////////////////////////////
    
    static final ObjectMapper mapper = new ObjectMapper();
    
    ////////////////////////////////////////////////////////////////
    // Value Creators
    ////////////////////////////////////////////////////////////////
    
    /**
     * Creates a new JsonObj with an empty string ""
     * @return A new JsonObj
     */
    public static JsonObj emptyVal() {
        return new JsonObj("");
    }
    
    /**
     * Creates a new JsonObj with a null value
     * @return A new JsonObj
     */
    public static JsonObj nullVal() {
        return new JsonObj((Object)null);
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Builders
    ////////////////////////////////////////////////////////////////
    
    /**
     * Creates a new JsonObj from a JSON string
     * @param string The JSON string to parse
     * @return A new JsonObj
     */
    public static JsonObj fromJsonString(String string) {
        try {
            return string == null || string.isBlank() ? emptyVal() : new JsonObj(mapper.readValue(string, Object.class));
        }
        catch (JsonProcessingException e) {
            throw new InvalidJsonProperty("Error processing JSON", e);
        }
    }
    
    /**
     * Creates a new JsonObj from a JSON string read from an input stream
     * @param stream The input stream to read
     * @return A new JsonObj
     */
    public static JsonObj fromInputStream(InputStream stream) {
        try {
            InputStreamReader reader = new InputStreamReader(stream);
            return new JsonObj(mapper.readValue(reader, Object.class));
        }
        catch (JsonProcessingException e) {
            throw new InvalidJsonProperty("Error processing JSON", e);
        } catch (IOException e) {
            throw new InvalidJsonProperty("Error reading JSON stream", e);
        }
    }
    
    /**
     * Creates a new JsonObj in the form of an empty list
     * @return A new JsonObj
     */
    public static JsonObj newList() {
        return new JsonObj(createListType());
    }
    
    /**
     * Creates a new JsonObj in the form of an empty list with the given initial capacity
     * @param initialCapacity The initial capacity of the list
     * @return A new JsonObj
     */
    public static JsonObj newList(int initialCapacity) {
        return new JsonObj(createListType(initialCapacity));
    }
    
    /**
     * Creates a new JsonObj in the form of a list with contents derived from the source list
     * @param list The source list
     * @return A new JsonObj
     */
    public static JsonObj newList(List<?> list) {
        JsonObj jsonList = JsonObj.newList(list.size());
        for(Object val : list) {
            Core core = transformForInsertion(val);
            switch (core.mode) {
                case MAP -> jsonList.add(JsonObj.newMap((Map<?,?>)core.val));
                case LIST -> jsonList.add(JsonObj.newList((List<?>)core.val));
                default -> jsonList.add(core.val);
            }
        }
        return jsonList;
    }
    
    /**
     * Creates a new JsonObj in the form of an empty map
     * @return A new JsonObj
     */
    public static JsonObj newMap() {
        return new JsonObj(createMapType());
    }
    
    /**
     * Creates a new JsonObj in the form of a map with contents derived from the source map
     * @param map The source map
     * @return A new JsonObj
     */
    public static JsonObj newMap(Map<?,?> map) {
        JsonObj jsonMap = JsonObj.newMap();
        for(var entry : map.entrySet()) {
            String key = entry.getKey().toString();
            Core core = transformForInsertion(entry.getValue());
            switch (core.mode) {
                case MAP -> jsonMap.set(key, JsonObj.newMap((Map<?,?>)core.val));
                case LIST -> jsonMap.set(key, JsonObj.newList((List<?>)core.val));
                default -> jsonMap.set(key, core.val);
            }
        }
        return jsonMap;
    }
    
    /**
     * Combines multiple JsonObj lists into a single list
     * @param lists The lists to combine
     * @return A new JsonObj
     */
    public static JsonObj combineLists(JsonObj ... lists) {
        int size = 0;
        for(JsonObj jsonObj : lists) {
            size += jsonObj.size();
        }
        JsonObj newList = JsonObj.newList(size);
        for(JsonObj list: lists) {
            if(list.isList()) {
                for(JsonObj item : list) {
                    newList.add(item);
                }
            }
        }
        return newList;
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Data Members
    ////////////////////////////////////////////////////////////////
    
    /**
     * A private record for storing the wrapped object and its type
     */
    private record Core(Object val, JsonType mode) {}
    
    /**
     * The wrapped object
     */
    private final Core core;
    
    
    ////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////
    
    /**
     * Creates a new JsonObj from a raw object and identifies its type
     */
    private JsonObj(Object o) {
        this.core = transformForInsertion(o);
    }
    
    /**
     * Creates a new JsonObj in the form of a string
     * @param string The string to use
     */
    public JsonObj(String string) {
        this((Object)string);
    }
    
    /**
     * Creates a new JsonObj in the form of a number
     * @param number The number to use
     */
    public JsonObj(Number number) {
        this((Object)number);
    }
    
    /**
     * Creates a new JsonObj from a raw map
     * @param map The map to use
     */
    private JsonObj(Map<String,?> map) {
        this((Object)map);
    }
    
    /**
     * Creates a new JsonObj from a raw list
     * @param list The list to use
     */
    private JsonObj(List<?> list) {
        this((Object)list);
    }
    
    /**
     * Creates a new JsonObj in the form of a boolean
     * @param bool The boolean to use
     */
    public JsonObj(Boolean bool) {
        this((Object)bool);
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Internal Type Management
    ////////////////////////////////////////////////////////////////
    
    /**
     * Casts the wrapped object as a string to object map
     * @return The wrapped object as a map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> mapData() {
        return (Map<String, Object>)core.val;
    }
    
    /**
     * Casts the wrapped object as a list of objects
     * @return The wrapped object as a list
     */
    @SuppressWarnings("unchecked")
    private List<Object> listData() {
        return (List<Object>)core.val;
    }
    
    /**
     * Creates a new empty list
     * @return A new empty list
     */
    private static List<Object> createListType() {
        return createListType(-1);
    }
    
    /**
     * Creates a new list with the given initial capacity using the
     * list type.
     * @param initialCapacity The initial capacity of the list
     * @return A new list with the given initial capacity
     */
    private static List<Object> createListType(int initialCapacity) {
        return (initialCapacity < 0) ? new ArrayList<>() : new ArrayList<>(initialCapacity);
    }
    
    /**
     * Creates a new empty map using the preferred map type.
     * A LinkedHashMap is used to preserve insertion order.
     * @return A new empty map
     */
    private static Map<String, Object> createMapType() {
        return new LinkedHashMap<>();
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Identity
    ////////////////////////////////////////////////////////////////
    
    private enum JsonType { MAP, LIST, STRING, NUMBER, BOOLEAN, NULL, INVALID }
    
    /**
     * Identifies the data type of the given object
     * @param o The object to identify
     * @return The data type of the object or null if invalid
     */
    private static JsonType identifyDataType(Object o) {
        if(o instanceof String) { return JsonType.STRING; }
        if(o instanceof Number) { return JsonType.NUMBER; }
        if(o instanceof Map) { return JsonType.MAP; }
        if(o instanceof List) { return JsonType.LIST; }
        if(o == null) { return JsonType.NULL; }
        if(o instanceof Boolean) { return JsonType.BOOLEAN; }
        return JsonType.INVALID;
    }
    
    public boolean isMap() { return core.mode == JsonType.MAP; }
    public boolean isList() { return core.mode == JsonType.LIST; }
    public boolean isString() { return core.mode == JsonType.STRING; }
    public boolean isNumber() { return core.mode == JsonType.NUMBER; }
    public boolean isBoolean() { return core.mode == JsonType.BOOLEAN; }
    public boolean isNull() { return core.mode == JsonType.NULL; }
    
    /**
     * If the object is a list then returns its size otherwise 0
     * @return The size of the list or 0
     */
    public int size() {
        return isList() ? listData().size() : 0;
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns an optional string if the map contains the given key
     * @return An optional string
     */
    public Optional<String> getString(String name) {
        return getObjInternal(name).asString();
    }
    
    /**
     * Returns an optional integer if the list contains the given key, and
     * it can be interpreted as an integer
     * @return An optional integer
     */
    public Optional<Integer> getInt(String name) {
        return getObjInternal(name).asInteger();
    }
    
    /**
     * Returns an optional long if the list contains the given key, and
     * it can be interpreted as a long
     * @return An optional long
     */
    public Optional<Long> getLong(String name) {
        return getObjInternal(name).asLong();
    }
    
    /**
     * Returns an optional double if the list contains the given key, and
     * it can be interpreted as a double
     * @return An optional double
     */
    public Optional<Double> getDouble(String name) {
        return getObjInternal(name).asDouble();
    }
    
    /**
     * Returns an optional boolean if the list contains the given key, and
     * it can be interpreted as a boolean
     * @return An optional boolean
     */
    public Optional<Boolean> getBoolean(String name) {
        return getObjInternal(name).asBoolean();
    }
    
    /**
     * Returns an object if this is a map and contains the given key
     * @return An object or null if not found
     */
    private Object get(String name) {
        return isMap() ? mapData().get(name) : null;
    }
    
    /**
     * Returns a JsonObj if this is a map and contains the given key
     * @return A JsonObj if found or NULL type JsonObj if not found
     */
    private JsonObj getObjInternal(String name) {
        return new JsonObj(get(name));
    }
    
    /**
     * Returns an optional object if this is a map and contains the given key
     * @return An optional object
     */
    public Optional<JsonObj> getObj(String name) {
        Object obj = get(name);
        return obj != null ? Optional.of(new JsonObj(obj)) : Optional.empty();
    }
    
    /**
     * Returns an object if the list contains the given index and the index is valid otherwise null
     * @return An object or null if not found
     */
    private Object get(int index) {
        return isList() && index >= 0 && index < size() ? listData().get(index) : null;
    }
    
    /**
     * Returns a JsonObj if the list contains the given index and the index is valid otherwise null
     * @return A JsonObj if found or NULL type JsonObj if not found
     */
    private JsonObj getObjInternal(int index) {
        return new JsonObj(get(index));
    }
    
    /**
     * Returns an optional object if the list contains the given index and the index is valid
     * @return An optional object
     */
    public Optional<JsonObj> getObj(int index) {
        Object obj = get(index);
        return obj != null ? Optional.of(new JsonObj(obj)) : Optional.empty();
    }
    
    /**
     * Return an optional object by following a json path specified by the path parameters.
     * Integer parameters are treated as list indices and String parameters are treated as map keys.
     * @return An optional object
     */
    public Optional<JsonObj> getByPath(Object ... ids) {
        JsonObj jsonObj = this;
        for(Object obj : ids) {
            if(jsonObj.isList() && obj instanceof Integer id) {
                if(!jsonObj.contains(id)) {
                    return Optional.empty();
                }
                jsonObj = jsonObj.getObjInternal(id);
            }
            else if(jsonObj.isMap() && obj instanceof String name) {
                if(!jsonObj.contains(name)) {
                    return Optional.empty();
                }
                jsonObj = jsonObj.getObjInternal(name);
            }
            else {
                return Optional.empty();
            }
        }
        return Optional.of(jsonObj);
    }
    
    /**
     * Return the raw underlying POJO being wrapped by this implementation.
     * @return The raw POJO
     */
    public Object raw() {
        return core.val;
    }
    
    /**
     * Create a mutable list from this object. If this object is a list then
     * each element is converted to the given type and added to the list.
     * If the object is not a list then an empty list is returned.
     * @param loader The function to convert each element
     * @return A new mutable list
     */
    public <T> List<T> toMutableList(JsonObjLoad<T> loader) throws InvalidJsonProperty {
        List<T> list = new ArrayList<>(this.size());
        if(isList()) {
            for(JsonObj item : this) {
                T convItem = loader.apply(item);
                if(convItem != null) {
                    list.add(convItem);
                }
            }
        }
        return list;
    }
    
    /**
     * Create an immutable list from this object. If this object is a list then
     * each element is converted to the given type and added to the list.
     * If the object is not a list then an empty list is returned.
     * @param loader The function to convert each element
     * @return A new immutable list
     */
    public <T> List<T> toImmutableList(JsonObjLoad<T> loader) throws InvalidJsonProperty {
        return List.copyOf(toMutableList(loader));
    }
    
    /**
     * Create a mutable map from this object. If this object is a map then
     * each element is converted to the given type and added to the map.
     * If the object is not a list then an empty map is returned.
     * @param loader The function to convert each element
     * @return A new mutable map
     */
    public <T> Map<String, T> toMutableMap(JsonObjLoad<T> loader) throws InvalidJsonProperty {
        Map<String, T> map = new LinkedHashMap<>();
        if(isMap()) {
            for(var entry : asMap().orElseGet(HashMap::new).entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                T convItem = loader.apply(new JsonObj(val));
                if(convItem != null) {
                    map.put(key, convItem);
                }
            }
        }
        return map;
    }
    
    /**
     * Create an immutable map from this object. If this object is a map then
     * each element is converted to the given type and added to the map.
     * If the object is not a list then an empty map is returned.
     * @param loader The function to convert each element
     * @return A new immutable map
     */
    public <T> Map<String, T> toImmutableMap(JsonObjLoad<T> loader) throws InvalidJsonProperty {
        return Map.copyOf(toMutableMap(loader));
    }
    
    /**
     * Converts this object to a new object of the given type using the given loader.
     * @param loader The function to convert this object
     * @return A new object of the given type
     */
    public <T> T convert(JsonObjLoad<T> loader) throws InvalidJsonProperty {
        return loader.apply(this);
    }
    
    /**
     * An interface for converting a JsonObj to another type
     */
    @FunctionalInterface
    public interface JsonObjLoad<T> {
        T apply(JsonObj src) throws InvalidJsonProperty;
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////
    
    /**
     * Transforms the given object into a form that can be inserted into a JsonObj
     * @param obj The object to transform
     * @return The transformed object and its type
     */
    private static Core transformForInsertion(Object obj) {
        if(obj instanceof Jsonable jsonable) {
            obj = jsonable.toJsonObj();
        }
        if(obj instanceof JsonObj jsonObj) {
            obj = jsonObj.raw();
        }
        JsonType type = identifyDataType(obj);
        if(type == JsonType.INVALID) {
            obj = stringifyObject(obj);
            type = JsonType.STRING;
        }
        return new Core(obj, type);
    }
    
    /**
     * Converts the given object to a string
     * @param object The object to convert
     * @return The stringified object
     */
    private static String stringifyObject(Object object) {
        if(object instanceof Instant instant) {
            return instant.truncatedTo(ChronoUnit.MILLIS).toString();
        }
        return object.toString();
    }
    
    /**
     * Sets the given object in the map if this object is a map
     * @param name The name of the object
     * @return This object
     */
    public JsonObj set(String name, Object obj) {
        if(isMap()) {
            mapData().put(name, transformForInsertion(obj).val);
        }
        return this;
    }
    
    /**
     * Sets the given object in the map if this object is a map and doSet is true
     * @param name The name of the object
     * @param doSet Whether to set the object
     * @return This object
     */
    public JsonObj set(String name, Object obj, boolean doSet) {
        return doSet ? set(name, obj) : this;
    }
    
    /**
     * Sets the given object in the map if this object is a map and the predicate is true
     * @param name The name of the object
     * @param predicate The predicate to test the object against
     * @return This object
     */
    public JsonObj set(String name, Object obj, Predicate<Object> predicate) {
        return set(name, obj, predicate.test(obj));
    }
    
    /**
     * Sets the given object in the list if this object is a list
     * @param index The index of the object
     * @return This object
     */
    public JsonObj set(int index, Object obj) {
        if(isList()) {
            listData().set(index, transformForInsertion(obj).val);
        }
        return this;
    }
    
    /**
     * Sets the given object in the list if this object is a list and doSet is true
     * @param index The index of the object
     * @param doSet Whether to set the object
     * @return This object
     */
    public JsonObj set(int index, Object obj, boolean doSet) {
        return doSet ? set(index, obj) : this;
    }
    
    /**
     * Sets the given object in the list if this object is a list and the predicate is true
     * @param index The index of the object
     * @param predicate The predicate to test the object against
     * @return This object
     */
    public JsonObj set(int index, Object obj, Predicate<Object> predicate) {
        return set(index, obj, predicate.test(obj));
    }
    
    /**
     * Appends the given object to the list if this object is a list
     * @param obj The object to add
     * @return This object
     */
    public JsonObj add(Object obj) {
        if(isList()) {
            listData().add(transformForInsertion(obj).val);
        }
        return this;
    }
    
    /**
     * Appends the given object to the list if this object is a list and doAdd is true
     * @param obj The object to add
     * @param doAdd Whether to add the object
     * @return This object
     */
    public JsonObj add(Object obj, boolean doAdd) {
        return doAdd ? add(obj) : this;
    }
    
    /**
     * Appends the given object to the list if this object is a list and the predicate is true
     * @param obj The object to add
     * @param predicate The predicate to test the object against
     * @return This object
     */
    public JsonObj add(Object obj, Predicate<Object> predicate) {
        return add(obj, predicate.test(obj));
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Adapters
    ////////////////////////////////////////////////////////////////
    
    /**
     * Returns an optional list of raw objects if this object is a list
     * @return An optional list of raw objects
     */
    public Optional<List<Object>> asList() {
        return isList() ? Optional.of(listData()) : Optional.empty();
    }
    
    /**
     * Returns an optional mapping of string to raw objects if this object is a map
     * @return An optional mapping of string to raw objects
     */
    public Optional<Map<String,Object>> asMap() {
        return isMap() ? Optional.of(mapData()) : Optional.empty();
    }
    
    /**
     * Returns an optional string if the wrapped object can be interpreted as a string
     * @return An optional string
     */
    public Optional<String> asString() {
        return isString() ? Optional.of((String)core.val) : Optional.empty();
    }
    
    /**
     * Returns an optional number if the wrapped object can be interpreted as a number
     * @return An optional number
     */
    public Optional<Number> asNumber() {
        return isNumber() ? Optional.of((Number)core.val) : asString().flatMap(this::numbery);
    }
    
    /**
     * Returns an optional double if the wrapped object can be interpreted as an double
     * @return An optional double
     */
    public Optional<Double> asDouble() {
        return asNumber().map(Number::doubleValue);
    }
    
    /**
     * Returns an optional integer if the wrapped object can be interpreted as a integer
     * @return An optional integer
     */
    public Optional<Integer> asInteger() {
        return asNumber().map(Number::intValue);
    }
    
    /**
     * Returns an optional long if the wrapped object can be interpreted as a long
     * @return An optional long
     */
    public Optional<Long> asLong() {
        return asNumber().map(Number::longValue);
    }
    
    /**
     * Returns an optional boolean if the wrapped object can be interpreted as a boolean
     * @return An optional boolean
     */
    public Optional<Boolean> asBoolean() {
        if(isBoolean()) {
            return Optional.of((Boolean)core.val);
        }
        if(isString()) {
            return truthy(asString().orElse(""));
        }
        if(isNumber()) {
            return Optional.of(asInteger().orElse(0) != 0);
        }
        return Optional.empty();
    }
    
    /**
     * Returns an optional number if the wrapped object can be interpreted as a number
     * @return An optional number
     */
    private Optional<Number> numbery(String val) {
        try {
            return Optional.of(Double.parseDouble(val));
        }
        catch(NumberFormatException nfe) {
            return Optional.empty();
        }
    }
    
    /**
     * Returns an optional boolean if the wrapped object can be interpreted as a boolean.
     * Case-insensitive string values of "true" and "false" are considered truthy.
     * @return An optional boolean
     */
    private Optional<Boolean> truthy(String val) {
        val = val == null ? "" : val.trim();
        if(val.equalsIgnoreCase("true")) {
            return Optional.of(true);
        }
        if(val.equalsIgnoreCase("false")) {
            return Optional.of(false);
        }
        return Optional.empty();
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Manipulators
    ////////////////////////////////////////////////////////////////
    
    /**
     * If this object is a list then returns a new list containing the elements
     * from startInc to stopInc inclusive. If stopInc is -1 then returns all
     * elements from startInc to the end of the list.
     * @param startInc The starting index
     * @param stopInc The ending index
     * @return A new list containing the elements from startInc to stopInc
     */
    public JsonObj range(int startInc, int stopInc) {
        JsonObj newList = JsonObj.newList();
        if(isList()) {
            if(stopInc == -1) {
                stopInc = size() - 1;
            }
            for(int i = startInc; i <= stopInc; i++) {
                if(i >= 0 && i < size()) {
                    newList.add(get(i));
                }
            }
        }
        return newList;
    }
    
    /**
     * In this object is a list then filters this list in place based on the predicate.
     * @param predicate The predicate to test each element against
     * @return This object
     */
    public JsonObj filter(Predicate<JsonObj> predicate) {
        if(isList()) {
            listData().removeIf(elem -> predicate.negate().test(new JsonObj(elem)));
        }
        return this;
    }
    
    /**
     * In this object is a list then collects the results of a
     * function into a new list.
     * @param collectFunc The function to apply to each element
     * @return A new list containing the results of the function
     */
    public JsonObj collect(UnaryOperator<JsonObj> collectFunc) {
        JsonObj accum = JsonObj.newList();
        if(isList()) {
            for(JsonObj objIn : this) {
                JsonObj objOut = collectFunc.apply(objIn);
                if(objOut != null) {
                    accum.add(objOut);
                }
            }
        }
        return accum;
    }
    
    /**
     * In this object is a list then returns the first element that matches the predicate.
     * @param predicate The predicate to test each element against
     * @return The first element that matches the predicate
     */
    public JsonObj any(Predicate<JsonObj> predicate) {
        for(JsonObj obj : this) {
            if(predicate.test(obj)) {
                return obj;
            }
        }
        return nullVal();
    }
    
    /**
     * Returns true if this object is map and contains the given key
     * or if the object is a list and contains the given string value.
     * Search is case-sensitive.
     */
    public boolean contains(String name) {
        return contains(name, true);
    }
    
    /**
     * Returns true if this object is map and contains the given key
     * or if the object is a list and contains the given string value.
     * @param name The name to search for
     * @param caseSensitive Whether the search is case-sensitive
     * @return True if the name is found
     */
    public boolean contains(String name, boolean caseSensitive) {
        return caseSensitive ? containsCaseSensitive(name) : containsCaseInsensitive(name);
    }
    
    /**
     * Returns true if this object is a map and contains the given key
     * or if the object is a list and contains the given string value.
     * Search is case-sensitive.
     * @param name The name to search for
     * @return True if the name is found
     */
    private boolean containsCaseSensitive(String name) {
        if(isMap()) {
            return mapData().containsKey(name);
        }
        if(isList() ) {
            return listData().contains(name);
        }
        return false;
    }
    
    /**
     * Returns true if this object is a map and contains the given key
     * or if the object is a list and contains the given string value.
     * Search is case-insensitive.
     * @param name The name to search for
     * @return True if the name is found
     */
    private boolean containsCaseInsensitive(String name) {
        if(isMap()) {
            Map<String, Object> map = mapData();
            for(var entry : map.entrySet()) {
                String key = entry.getKey();
                if(name.equalsIgnoreCase(key)) {
                    return true;
                }
            }
        }
        if(isList()) {
            for(JsonObj obj : this) {
                if(obj.isString() && obj.asString().orElse("").equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Returns true if this object is a list and contains the given index.
     * @param id The index to search for
     * @return True if object is a list and the index is found
     */
    public boolean contains(int id) {
        return isList() && id >= 0 && id < listData().size();
    }
    
    /**
     * Transforms this object to a list if it is a list or a map using the given function.
     * @param function The function to apply to each element
     * @return A new list containing the results of the function
     */
    public JsonObj toList(BiFunction<String, JsonObj, JsonObj> function) {
        JsonObj list = JsonObj.newList();
        if(isList()) {
            for (Object o : listData()) {
                JsonObj objIn = new JsonObj(o);
                JsonObj objOut = function.apply("", objIn);
                if (objOut != null) {
                    list.add(objOut);
                }
            }
            return this;
        }
        if(isMap()) {
            for (Map.Entry<String, Object> entry : mapData().entrySet()) {
                String key = entry.getKey();
                JsonObj objIn = new JsonObj(entry.getValue());
                JsonObj objOut = function.apply(key, objIn);
                if (objOut != null) {
                    list.add(objOut);
                }
            }
            return list;
        }
        list.add(function.apply("", this));
        return list;
    }
    
    /**
     * Transforms this object to a new map if it is a list using the given function.
     * If the object is a map then it is returned as-is.
     * If the object is not a list or a map then it is returned as a map with a single entry.
     * @param function The function to apply to each element
     * @return A new map containing the results of the function or the original map if it is a map
     */
    public JsonObj toMap(Function<JsonObj, String> function) {
        if(isMap()) {
            return this;
        }
        JsonObj map = JsonObj.newMap();
        if(isList()) {
            for (Object o : listData()) {
                JsonObj objIn = new JsonObj(o);
                String name = function.apply(objIn);
                if (name != null) {
                    map.set(name, objIn);
                }
            }
            return map;
        }
        map.set(function.apply(this), this);
        return map;
    }
    
    /**
     * Removes the given object by key name from this object if it is a map.
     * @param name The key name to remove
     * @return This object
     */
    public JsonObj remove(String name) {
        if(isMap()) {
            mapData().remove(name);
        }
        return this;
    }
    
    /**
     * Removes the given object by index from this object if it is a list.
     * @param id The index to remove
     * @return This object
     */
    public JsonObj remove(int id) {
        if(isList() && id < size()) {
            listData().remove(id);
        }
        return this;
    }
    
    /**
     * Performs a deep copy of this object. This is usually performed
     * as a defensive copy to prevent mutation.
     * @return A new JsonObj with the same data
     */
    public JsonObj copy() {
        return new JsonObj(deepCopy());
    }
    
    /**
     * Performs a deep copy of this object usually performed
     * as a defensive copy to prevent mutation.
     * @return A new object with the same data
     */
    private Object deepCopy() {
        switch (core.mode) {
            case LIST: {
                List<Object> newList = createListType(size());
                forList(j -> newList.add(j.deepCopy()));
                return newList;
            }
            case MAP: {
                Map<String, Object> newMap = createMapType();
                asMap().orElseGet(JsonObj::createMapType).forEach((k, v) -> newMap.put(k, new JsonObj(v).deepCopy()));
                return newMap;
            }
            default:
                return core.val;
        }
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Iteration
    ////////////////////////////////////////////////////////////////
    
    /**
     * Iterates over the elements of this object if it is a list.
     * @param consumer The consumer to apply to each element
     */
    public void forList(Consumer<JsonObj> consumer) {
        if(isList()) {
            for(int i = 0; i < size(); i++) {
                consumer.accept(new JsonObj(get(i)));
            }
        }
    }
    
    /**
     * Iterates over the elements of this object if it is a map.
     * @param consumer The consumer to apply to each element
     * @return This object
     */
    public JsonObj forMap(BiConsumer<String, JsonObj> consumer) {
        if(isMap()) {
            for (var entry : mapData().entrySet()) {
                String key = entry.getKey();
                JsonObj objIn = new JsonObj(entry.getValue());
                consumer.accept(key, objIn);
            }
        }
        return this;
    }
    
    @Override
    public Iterator<JsonObj> iterator() {
        return new Iterator<>() {
            private int index = 0;
            
            @Override
            public boolean hasNext() {
                return index < size();
            }
            
            @Override
            public JsonObj next() {
                List<Object> list = listData();
                if (index < list.size()) {
                    Object object = listData().get(index++);
                    return new JsonObj(object);
                }
                throw new NoSuchElementException();
            }
        };
    }
    
    
    ////////////////////////////////////////////////////////////////
    // Serialization
    ////////////////////////////////////////////////////////////////
    
    /**
     * Serializes list data to a string
     */
    private void stringifyList(StringBuilder builder) {
        builder.append("[");
        int count = size();
        for(Object obj : listData()) {
            builder.append(new JsonObj(obj));
            if(--count > 0) {
                builder.append(",");
            }
        }
        builder.append("]");
    }
    
    /**
     * Serializes map data to a string
     */
    private void stringifyMap(StringBuilder builder) {
        builder.append("{");
        int count = mapData().size();
        for(var entry : mapData().entrySet()) {
            String name = entry.getKey();
            JsonObj obj = new JsonObj(entry.getValue());
            enquote(builder, name).append(":").append(obj);
            if(--count > 0) {
                builder.append(",");
            }
        }
        builder.append("}");
    }
    
    /**
     * Enquotes the given string and appends it to the builder
     * @param builder The builder to append to
     * @param val The string to enquote
     * @return The builder
     */
    private StringBuilder enquote(StringBuilder builder, String val) {
        return builder.append(escapeString(val));
    }
    
    /**
     * Returns a string representation of the given number
     * @param num The number to convert
     * @return A string representation of the number
     */
    private String getStringForNumber(Number num) {
        if(num instanceof BigDecimal) {
            return num.toString();
        }
        if(num instanceof BigInteger) {
            return num.toString();
        }
        if(num instanceof Integer) {
            return Integer.toString(num.intValue());
        }
        if(num instanceof Long) {
            return Long.toString(num.longValue());
        }
        return Double.toString(num.doubleValue());
    }
    
    /**
     * Escapes the given string
     * @param input The string to escape
     * @return The escaped string
     */
    private String escapeString(String input) {
        try {
            return mapper.writeValueAsString(input);
        }
        catch (JsonProcessingException e) {
            LOG.error("Error escaping string", e);
            return "";
        }
    }
    
    /**
     * Builds a string representation of this object
     * @param builder The builder to append to
     */
    private void buildString(StringBuilder builder) {
        switch (core.mode) {
            case LIST: stringifyList(builder); break;
            case MAP: stringifyMap(builder); break;
            case NUMBER: builder.append(getStringForNumber((Number) core.val)); break;
            case STRING: builder.append(escapeString(asString().orElse(""))); break;
            case BOOLEAN: builder.append(Boolean.TRUE.equals(asBoolean().orElse(false)) ? "true" : "false"); break;
            case NULL: builder.append("null"); break;
            default: break;
        }
    }
    
    /**
     * Serializes this object to a string
     * @return A string representation of this object
     */
    public String serialize() {
        StringBuilder builder = new StringBuilder(1024);
        buildString(builder);
        return builder.toString();
    }
    
    /**
     * Serializes this object to a string.
     * Same as serialize()
     * @return A string representation of this object
     */
    @Override
    public String toString() {
        return serialize();
    }
    
}

