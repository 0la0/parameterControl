package etc.a0la0.osccontroller.anative;

/**
 * Facade for JNI functions
 * This is in a different module because gradle of conflicting gradle dependencies in app module
 */
public class NativeHelper {

    public native void calcMatricies(
            int width, int height, int centerX, int centerY,
            float std, float amplitude,
            float[] valueArray, int[] pixelArray,
            int r, int g, int b);

    public native void aggregateMatrices(
            int[] pixelBuffer, int[] pixelValues,
            int bufferLength, int numberOfPresets);

    static {
        System.loadLibrary("jni-calc");
    }

}
