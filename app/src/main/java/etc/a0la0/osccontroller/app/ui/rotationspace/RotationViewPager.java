package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Preset;

public interface RotationViewPager {

    View instantiatePagerView(ViewGroup viewPager);
    void onDestroy();
    int getTitleResource();
    void onSelect();
    void onUnselect();
    void setPresetList(List<Preset> presetList);
    void onAccelerometerChange(float[] accelerometerData);

    class TrainView implements RotationViewPager {
        PagerTrainView pagerTrainView;

        @Override
        public View instantiatePagerView(ViewGroup viewPager) {
            pagerTrainView = PagerTrainView.inflate(viewPager, false);
            return pagerTrainView;
        }

        @Override
        public void onDestroy() {
            if (pagerTrainView != null) {
                pagerTrainView.onDestroy();
            }
        }

        @Override
        public int getTitleResource() {
            return R.string.train;
        }

        @Override
        public void onSelect() {
            if (pagerTrainView != null) {
                pagerTrainView.onSelect();
            }
        }

        @Override
        public void onUnselect() {
            if (pagerTrainView != null) {
                pagerTrainView.onUnselect();
            }
        }

        @Override
        public void setPresetList(List<Preset> presetList) {
            if (pagerTrainView != null) {
                pagerTrainView.setPresetList(presetList);
            }
        }

        @Override
        public void onAccelerometerChange(float[] accelerometerData) {
            if (pagerTrainView != null) {
                pagerTrainView.onAccelerometerChange(accelerometerData);
            }
        }

        public void setMessageDelegate(PagerTrainView.MessageDelegate messageDelegate) {
            pagerTrainView.setMessageDelegate(messageDelegate);
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
        public void onDestroy() {
            if (pagerClassifyView != null) {
                pagerClassifyView.onDestroy();
            }
        }

        @Override
        public int getTitleResource() {
            return R.string.classify;
        }

        @Override
        public void onSelect() {
            if (pagerClassifyView != null) {
                pagerClassifyView.onSelect();
            }
        }

        @Override
        public void onUnselect() {
            if (pagerClassifyView != null) {
                pagerClassifyView.onUnselect();
            }
        }

        @Override
        public void setPresetList(List<Preset> presetList) {
            if (pagerClassifyView != null) {
                pagerClassifyView.setPresetList(presetList);
            }
        }

        @Override
        public void onAccelerometerChange(float[] accelerometerData) {
            if (pagerClassifyView != null) {
                pagerClassifyView.onAccelerometerChange(accelerometerData);
            }
        }

        public void setTrainingSet(List<TrainingInstance> trainingData) {
            if (pagerClassifyView != null) {
                pagerClassifyView.setTrainingSet(trainingData);
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
        public void onDestroy() {
            if (pagerInterpolateView != null) {
                pagerInterpolateView.onDestroy();
            }
        }

        @Override
        public int getTitleResource() {
            return R.string.interpolate;
        }

        @Override
        public void onSelect() {
            if (pagerInterpolateView != null) {
                pagerInterpolateView.onSelect();
            }
        }

        @Override
        public void onUnselect() {
            if (pagerInterpolateView != null) {
                pagerInterpolateView.onUnselect();
            }
        }

        @Override
        public void setPresetList(List<Preset> presetList) {
            if (pagerInterpolateView != null) {
                pagerInterpolateView.setPresetList(presetList);
            }
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

    }

}
