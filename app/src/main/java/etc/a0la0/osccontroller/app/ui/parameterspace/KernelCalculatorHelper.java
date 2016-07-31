package etc.a0la0.osccontroller.app.ui.parameterspace;

import etc.a0la0.osccontroller.anative.NativeHelper;
import etc.a0la0.osccontroller.app.ui.parameterspace.entities.PresetViewModel;

public class KernelCalculatorHelper extends Thread {

    private PresetViewModel presetViewModel;

    public KernelCalculatorHelper(PresetViewModel presetViewModel) {
        this.presetViewModel = presetViewModel;
    }

    public void calculateMatrixNative() {
        int size = presetViewModel.getWidth() * presetViewModel.getHeight();
        float[] matrix = new float[size];
        int[] pixelValue = new int[size];

        NativeHelper nativeHelper = new NativeHelper();
        nativeHelper.calcMatricies(
                presetViewModel.getWidth(),
                presetViewModel.getHeight(),
                presetViewModel.getCenterX(),
                presetViewModel.getCenterY(),
                presetViewModel.getStandardDeviation(),
                presetViewModel.getAmplitude(),
                matrix,
                pixelValue,
                presetViewModel.getR(),
                presetViewModel.getG(),
                presetViewModel.getB()
        );

        presetViewModel.setMatrix(matrix);
        presetViewModel.setPixelValue(pixelValue);
    }

    @Override
    public void run() {
        calculateMatrixNative();
    }

}
