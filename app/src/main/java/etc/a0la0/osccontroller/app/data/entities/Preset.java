package etc.a0la0.osccontroller.app.data.entities;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.HashMap;
import java.util.Map;

public class Preset {

    private Map<String, Float> map = new HashMap<>();

    public Preset() {
        map = new HashMap<>();
    }

    public Preset(Map<String, Float> map) {
        this.map = map;
    }

    public void put(String key, float value) {
        map.put(key, value);
    }

    public float get(String key) {
        return map.get(key);
    }

    public boolean has(String key) {
        return map.containsKey(key);
    }

    public String toString() {
        return Stream.of(map.entrySet())
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
    }

}
