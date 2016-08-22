package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;

public class RotationSpaceActivity extends BaseActivity implements ViewPager.OnPageChangeListener, RotationSpacePresenter.View  {

    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    private RotationSpacePresenter presenter = new RotationSpacePresenter();
    private List<RotationViewPager> tabViewList;
    private List<TrainingInstance> trainingSet;
    private MessageDelegate messageDelegate;
    private int selectedTabIndex = 0;
    private int numberOfPresets;

    private final int TRAIN_VIEW = 0;
    private final int CLASSIFY_VIEW = 1;
    private final int INTERPOLATE_VIEW = 2;

    interface MessageDelegate {
        void setTrainingSet(List<TrainingInstance> trainingSet);
        void onUpdateWeightList(List<Double> weightList);
        void onUpdateWeightList(int classificationIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotation_space_activity);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setHomeAsUpEnabled(false);
        presenter.attachView(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int position = intent.getIntExtra(getString(R.string.option_id), 0);
        presenter.init(this, position);

        messageDelegate = new PageMessageDelegate();
        numberOfPresets = presenter.getNumberOfPresets();
        initTabs();
    }

    private void initTabs() {
        if (tabViewList != null) {
            return;
        }
        tabViewList = Arrays.asList(
                new RotationViewPager.TrainView(),
                new RotationViewPager.ClassifyView(),
                new RotationViewPager.InterpolateView()
        );
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new RotationSpacePagerAdapter(tabViewList));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0, true);
        tabLayout.dispatchSetSelected(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        presenter.onResume(sensorManager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        selectedTabIndex = position;

        switch(position) {
            case TRAIN_VIEW:
                tabViewList.get(TRAIN_VIEW).setMessageDelegate(messageDelegate);
                ((RotationViewPager.TrainView) tabViewList.get(TRAIN_VIEW)).setNumberOfPresets(numberOfPresets);
                break;
            case CLASSIFY_VIEW:
                tabViewList.get(CLASSIFY_VIEW).setMessageDelegate(messageDelegate);
                if (trainingSet != null) {
                    tabViewList.get(CLASSIFY_VIEW).setTrainingSet(trainingSet);
                }
                break;
            case INTERPOLATE_VIEW:
                tabViewList.get(INTERPOLATE_VIEW).setMessageDelegate(messageDelegate);
                if (trainingSet != null) {
                    tabViewList.get(INTERPOLATE_VIEW).setTrainingSet(trainingSet);
                }
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onAccelerometerChange(float[] accelerometerData) {
        if (tabViewList != null) {
            tabViewList.get(selectedTabIndex).onAccelerometerChange(accelerometerData);
        }
    }

    private class RotationSpacePagerAdapter extends PagerAdapter {

        List<RotationViewPager> viewList;

        public RotationSpacePagerAdapter(List<RotationViewPager> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = viewList.get(position).instantiatePagerView(container);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int titleResource = viewList.get(position).getTitleResource();
            return getResources().getString(titleResource);
        }

    }

    private class PageMessageDelegate implements MessageDelegate {
        @Override
        public void setTrainingSet(List<TrainingInstance> trainingData) {
            trainingSet = trainingData;
        }
        @Override
        public void onUpdateWeightList(List<Double> weightList) {
            presenter.onUpdateWeightList(weightList);
        }
        @Override
        public void onUpdateWeightList(int classificationIndex) {
            presenter.onUpdateWeightList(classificationIndex);
        }
    }

}