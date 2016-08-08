package etc.a0la0.osccontroller.app.ui.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by lukeanderson on 6/5/16.
 */
public class AccelerometerProvider {

    private Observable<float[]> accelerometerObservable;
    private SensorManager sensorManager;
    private SensorEventListener listener;

    public AccelerometerProvider(SensorManager sensorManager) {
        this.accelerometerObservable = this.getObservableSensorWrapper(sensorManager);
    }

    public Observable<float[]> getObservable () {
        return this.accelerometerObservable;
    }

    private Observable<float[]> getObservableSensorWrapper (SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        return Observable.create(new Observable.OnSubscribe<float[]> () {

            @Override
            public void call(Subscriber<? super float[]> subscriber) {

                listener = new SensorEventListener() {
                    @Override public void onSensorChanged(SensorEvent sensorEvent) {
                        subscriber.onNext(sensorEvent.values);
                    }

                    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                };

                Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI);
            }
        });
    }

    public void unregister() {
        sensorManager.unregisterListener(listener);
    }

}
