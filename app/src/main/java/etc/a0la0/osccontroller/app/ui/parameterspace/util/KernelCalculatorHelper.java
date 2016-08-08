package etc.a0la0.osccontroller.app.ui.parameterspace.util;

import etc.a0la0.osccontroller.anative.NativeHelper;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;

public class KernelCalculatorHelper extends Thread {

    private SpacePreset spacePreset;

    public KernelCalculatorHelper(SpacePreset spacePreset) {
        this.spacePreset = spacePreset;
    }

    public void calculateMatrixNative() {
        int size = spacePreset.getWidth() * spacePreset.getHeight();
        float[] matrix = new float[size];
        int[] pixelValue = new int[size];

        NativeHelper nativeHelper = new NativeHelper();
        nativeHelper.calcMatricies(
                spacePreset.getWidth(),
                spacePreset.getHeight(),
                spacePreset.getCenterX(),
                spacePreset.getCenterY(),
                spacePreset.getStandardDeviation(),
                spacePreset.getAmplitude(),
                matrix,
                pixelValue,
                spacePreset.getR(),
                spacePreset.getG(),
                spacePreset.getB()
        );

        spacePreset.setMatrix(matrix);
        spacePreset.setPixelValue(pixelValue);
    }

    @Override
    public void run() {
        calculateMatrixNative();
    }

}
