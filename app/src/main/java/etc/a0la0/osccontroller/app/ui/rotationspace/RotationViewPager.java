package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import etc.a0la0.osccontroller.R;

public interface RotationViewPager {

    View instantiatePagerView(ViewGroup viewPager);
    int getTitleResource();
    void onAccelerometerChange(float[] accelerometerData);
    void setMessageDelegate(RotationSpaceActivity.MessageDelegate messageDelegate);
    void setTrainingSet(List<TrainingInstance> trainingData);

    class TrainView implements RotationViewPager {
        PagerTrainView pagerTrainView;

        @Override
        public View instantiatePagerView(ViewGroup viewPager) {
            pagerTrainView = PagerTrainView.inflate(viewPager, false);
            return pagerTrainView;
        }

        @Override
        public int getTitleResource() {
            return R.string.train;
        }

        public void setNumberOfPresets(int numberOfPresets) {
            if (pagerTrainView != null) {
                pagerTrainView.setNumberOfPresets(numberOfPresets);
            }
        }

        @Override
        public void onAccelerometerChange(float[] accelerometerData) {
            if (pagerTrainView != null) {
                pagerTrainView.onAccelerometerChange(accelerometerData);
            }
        }

        @Override
        public void setTrainingSet(List<TrainingInstance> trainingData) {}

        @Override
        public void setMessageDelegate(RotationSpaceActivity.MessageDelegate messageDelegate) {
            if (pagerTrainView != null) {
                pagerTrainView.setMessageDelegate(messageDelegate);
            }
        }

    }

    class ClassifyView implements RotationViewPager {
        PagerClassifyView pagerClassifyView;

        @Override
        public View instantiatePagerView(ViewGroup viewPager) {
            pagerClassifyView = PagerClassifyView.inflate(viewPager, false);
            return pagerClassifyView;
        }

        @Override
        public int getTitleResource() {
            return R.string.classify;
        }

        @Override
        public void onAccelerometerChange(float[] accelerometerData) {
            if (pagerClassifyView != null) {
                pagerClassifyView.onAccelerometerChange(accelerometerData);
            }
        }

        @Override
        public void setTrainingSet(List<TrainingInstance> trainingData) {
            if (pagerClassifyView != null) {
                pagerClassifyView.setTrainingSet(trainingData);
            }
        }

        @Override
        public void setMessageDelegate(RotationSpaceActivity.MessageDelegate messageDelegate) {
            if (pagerClassifyView != null) {
                pagerClassifyView.setMessageDelegate(messageDelegate);
            }
        }

    }

    class InterpolateView implements RotationViewPager {
        PagerInterpolateView pagerInterpolateView;

        @Override
        public View instantiatePagerView(ViewGroup viewPager) {
            pagerInterpolateView = PagerInterpolateView.inflate(viewPager, false);
            return pagerInterpolateView;
        }

        @Override
        public int getTitleResource() {
            return R.string.interpolate;
        }

        @Override
        public void onAccelerometerChange(float[] accelerometerData) {
            if (pagerInterpolateView != null) {
                pagerInterpolateView.onAccelerometerChange(accelerometerData);
            }
        }

        public void setTrainingSet(List<TrainingInstance> trainingData) {
            if (pagerInterpolateView != null) {
                pagerInterpolateView.setTrainingSet(trainingData);
            }
        }

        @Override
        public void setMessageDelegate(RotationSpaceActivity.MessageDelegate messageDelegate) {
            if (pagerInterpolateView != null) {
                pagerInterpolateView.setMessageDelegate(messageDelegate);
            }
        }

    }

}
