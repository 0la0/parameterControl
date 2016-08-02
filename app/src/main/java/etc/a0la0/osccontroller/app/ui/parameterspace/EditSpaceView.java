package etc.a0la0.osccontroller.app.ui.parameterspace;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;

public class EditSpaceView extends View {

    private int width;
    private int height;
    private Bitmap bitmap;
    private Canvas canvas;
    private List<Preset> presetList;
    private List<SpacePreset> spacePresetList;

    public EditSpaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(int optionIndex, List<Preset> presetList, List<SpacePreset> spacePresetList) {
        //this.optionIndex = optionIndex;
        this.presetList = presetList;
        this.spacePresetList = spacePresetList;
    }

    @Override
    protected void onSizeChanged(int width, int height, int previousWidth, int previousHeight) {
        super.onSizeChanged(width, height, previousWidth, previousHeight);
        this.width = width;
        this.height = height;

        long start = System.currentTimeMillis();
        Log.i("matrices", "start ");

        Stream.of(spacePresetList)
                .forEach(spacePreset -> {
                    spacePreset.setWidth(width);
                    spacePreset.setHeight(height);
                });

        List<KernelCalculatorHelper> threadList = Stream.of(spacePresetList)
                .map(spacePreset -> {
                    KernelCalculatorHelper thread = new KernelCalculatorHelper(spacePreset);
                    thread.start();
                    return thread;
                })
                .collect(Collectors.toList());

        try {
            for (KernelCalculatorHelper thread : threadList) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i("matrices calculated", (System.currentTimeMillis() - start) + "");
        bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawValueMatrix(canvas, spacePresetList);
    }

    public void onPresetChange(SpacePreset vm) {
        KernelCalculatorHelper thread = new KernelCalculatorHelper(vm);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        draw(canvas);
        invalidate();
    }

    private void drawValueMatrix(Canvas canvas, List<SpacePreset> viewModelList) {
        long start = System.currentTimeMillis();
        int bufferSize = width * height;

        int numberOfThreads = 4;
        int workerBufferSize = bufferSize / numberOfThreads;

        MatrixAggregatorHelper[] aggregatorWorkers = new MatrixAggregatorHelper[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            MatrixAggregatorHelper worker = new MatrixAggregatorHelper(workerBufferSize, workerBufferSize * i, viewModelList);
            aggregatorWorkers[i] = worker;
        }

        for (Thread thread : aggregatorWorkers) {
            thread.start();
        }
        try {
            for (Thread thread : aggregatorWorkers) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int[] pixelBuffer = new int[bufferSize];

        for (int i = 0; i < numberOfThreads; i++) {
            int bufferPosition = workerBufferSize * i;
            System.arraycopy(aggregatorWorkers[i].getPixelBuffer(), 0, pixelBuffer, bufferPosition, aggregatorWorkers[i].getPixelBuffer().length);
        }

        bitmap.setPixels(pixelBuffer, 0, width, 0, 0, width, height);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Log.i("drawDataToCanvas", (System.currentTimeMillis() - start) + "");
    }


}
