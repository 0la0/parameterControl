package etc.a0la0.osccontroller.app.ui.parameterspace.entities;

public class PresetViewModel {

    private int centerX;
    private int centerY;
    private float standardDeviation = 50;
    private float amplitude = 1;
    private int width;
    private int height;
    public int r;
    public int g;
    public int b;
    private float[] matrix;
    private int[] pixelValue;

    public PresetViewModel(int centerX, int centerY, int width, int height, int r, int g, int b) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public float getValue(int x, int y) {
        return matrix[x + y * width];
    }

    public int getPixelValue(int index) {
        return pixelValue[index];
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(float standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
    }

    public void setPixelValue(int[] pixelValue) {
        this.pixelValue = pixelValue;
    }

    public int[] getPixelValueArray(int startIndex, int length) {
        int[] copiedValues = new int[length];
        System.arraycopy(pixelValue, startIndex, copiedValues, 0, length);
        return copiedValues;
    }

}
