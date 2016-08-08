package etc.a0la0.osccontroller.app.ui.parameterspace.util;

import com.annimon.stream.Stream;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import etc.a0la0.osccontroller.anative.NativeHelper;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;

public class MatrixAggregatorHelper extends Thread {

    private int[] pixelBuffer;
    private int bufferLength;
    private int bufferOffset;
    private List<SpacePreset> spacePresetList;

    public MatrixAggregatorHelper(int bufferLength, int bufferOffset, List<SpacePreset> spacePresetList) {
        pixelBuffer = new int[bufferLength];
        this.bufferLength = bufferLength;
        this.bufferOffset = bufferOffset;
        this.spacePresetList = spacePresetList;
    }

    @Override
    public void run() {

        int numberOfPresets = spacePresetList.size();
        int size = numberOfPresets * bufferLength;
        AtomicInteger accumulatedIndex = new AtomicInteger(0);

        int[] pixelValues = Stream.of(spacePresetList)
                .map(vm -> getPixelArraySubset(vm.getPixelValueArray(), bufferOffset, bufferLength))
                .reduce(new int[size], (accumulatedArray, pixelValueArray) -> {
                    int index = accumulatedIndex.getAndIncrement();
                    System.arraycopy(pixelValueArray, 0, accumulatedArray, index * bufferLength, bufferLength);
                    return accumulatedArray;
                });

        NativeHelper nativeHelper = new NativeHelper();
        nativeHelper.aggregateMatrices(pixelBuffer, pixelValues, bufferLength, numberOfPresets);
    }

    public int[] getPixelBuffer() {
        return pixelBuffer;
    }

    private int[] getPixelArraySubset(int[] sourceArray, int bufferOffset, int bufferLength) {
        int[] copiedValues = new int[bufferLength];
        System.arraycopy(sourceArray, bufferOffset, copiedValues, 0, bufferLength);
        return copiedValues;
    }

}
