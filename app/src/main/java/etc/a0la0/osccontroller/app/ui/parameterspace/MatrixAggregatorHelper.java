package etc.a0la0.osccontroller.app.ui.parameterspace;

import com.annimon.stream.Stream;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import etc.a0la0.osccontroller.anative.NativeHelper;
import etc.a0la0.osccontroller.app.ui.parameterspace.entities.PresetViewModel;

public class MatrixAggregatorHelper extends Thread {

    private int[] pixelBuffer;
    private int bufferLength;
    private int bufferOffset;
    private List<PresetViewModel> presetViewModelList;

    public MatrixAggregatorHelper(int bufferLength, int bufferOffset, List<PresetViewModel> presetViewModelList) {
        pixelBuffer = new int[bufferLength];
        this.bufferLength = bufferLength;
        this.bufferOffset = bufferOffset;
        this.presetViewModelList = presetViewModelList;
    }

    @Override
    public void run() {

        int numberOfPresets = presetViewModelList.size();
        int size = numberOfPresets * bufferLength;
        AtomicInteger accumulatedIndex = new AtomicInteger(0);

        int[] pixelValues = Stream.of(presetViewModelList)
                .map(vm -> vm.getPixelValueArray(bufferOffset, bufferLength))
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

}
