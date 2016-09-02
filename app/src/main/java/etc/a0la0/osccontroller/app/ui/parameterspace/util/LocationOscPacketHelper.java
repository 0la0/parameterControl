package etc.a0la0.osccontroller.app.ui.parameterspace.util;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;

import java.util.Collections;
import java.util.List;

import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;

public class LocationOscPacketHelper {

    public static OSCBundle getPacketFromWeights(List<Float> weightList, List<Parameter> parameterList, List<Preset> presetValueList) {
        float weightSum = Stream.of(weightList)
                .reduce(0.0f, (sum, weight) -> sum + weight);

        float baseWeight = 1 - (Math.min(1, weightSum));
        weightList.set(0, baseWeight);
        float totalWeightSum = weightSum + baseWeight;

        List<OSCPacket> messageList = Stream.of(parameterList)
                .map(parameter -> {
                    String parameterKey = parameter.getUniqueId();
                    String address = parameter.getAddress();
                    float presetValue = 0;
                    for (int i = 0; i < weightList.size(); i++) {
                        Preset preset = presetValueList.get(i);
                        float weight = weightList.get(i);
                        presetValue += preset.get(parameterKey) * weight;
                    }
                    presetValue /= totalWeightSum;
                    return new OSCMessage(address, Collections.singletonList(presetValue));
                })
                .collect(Collectors.toList());

        return new OSCBundle(messageList);
    }

}
