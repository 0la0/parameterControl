package etc.a0la0.osccontroller.app.ui.rotationspace;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lukeanderson on 6/1/16.
 */
public class KnnClassifier {

    private int k;
    private List<TrainingInstance> trainingInstances;

    public KnnClassifier(int k) {
        this.k = k;
        clearInstances();
    }

    public KnnClassifier(int k, List<TrainingInstance> trainingInstances) {
        this.k = k;
        this.trainingInstances = trainingInstances;
    }

    public void clearInstances() {
        trainingInstances = new ArrayList<>();
    }

    public void addTrainingInstance(TrainingInstance trainingInstance) {
        trainingInstances.add(trainingInstance);
    }

    public void setTrainingInstances(List<TrainingInstance> trainingInstances) {
        this.trainingInstances = trainingInstances;
    }

    public void createModel() {
        List<TrainingInstance> sampledList = new ArrayList<>();

        Map<Integer, List<TrainingInstance>> groupedInstances = Stream.of(trainingInstances)
                .collect(Collectors.groupingBy(trainingInstance -> trainingInstance.classification));

        //for each classification, pick random sample from instance list
        Set<Integer> classSet = groupedInstances.keySet();
        for (Integer classification : classSet) {
            List<TrainingInstance> classPopulation = groupedInstances.get(classification);
            for (int i = 0; i < 10; i++) {
                if (!classPopulation.isEmpty()) {
                    int randomIndex = (int) Math.floor(classPopulation.size() * Math.random());
                    sampledList.add(classPopulation.get(randomIndex));
                }
            }
        }
        trainingInstances = sampledList;
    }

    public int classifyInstance(TrainingInstance testInstance) {
        List<DistanceClass> sortedDistances = Stream.of(trainingInstances)
                .map(trainingInstance -> {
                    double distance = getDistance(testInstance, trainingInstance);
                    return new DistanceClass(trainingInstance.classification, distance);
                })
                .sortBy(distanceClass -> distanceClass.distance)
                .collect(Collectors.toList());

        //---Reduce to K instances---//
        while (sortedDistances.size() > k) {
            sortedDistances.remove(sortedDistances.size() - 1);
        }

        //---Count number of classes in the sorted distances---//
        List<Counter> counterList = new ArrayList<>();
        for (DistanceClass dinstaceInstance : sortedDistances) {
            int instanceClass = dinstaceInstance.classification;
            Counter classificationCounter = listContainsClassification(counterList, instanceClass);
            if (classificationCounter != null) {
                classificationCounter.incrementCount();
            }
            else {
                counterList.add(new Counter(instanceClass));
            }
        }

        List<Counter> sortedCountList = Stream.of(counterList)
                .sortBy(counterInstance -> -counterInstance.count)
                .collect(Collectors.toList());

        int predictedClassification = sortedCountList.get(0).classification;
        return predictedClassification;
    }



    private double getDistance(TrainingInstance u, TrainingInstance v) {
        return Math.sqrt(
                        Math.pow(u.alpha - v.alpha, 2) +
                        Math.pow(u.beta - v.beta, 2) +
                        Math.pow(u.gamma - v.gamma, 2)
        );
    }

    private Counter listContainsClassification(List<Counter> counterList, int classification) {
        for (Counter counter : counterList) {
            if (counter.classification == classification) {
                return counter;
            }
        }
        return null;
    }

    private class DistanceClass {

        public int classification;
        public double distance;
        public int index;

        public DistanceClass (int classification, double distance) {
            this.classification = classification;
            this.distance = distance;
        }
    }

    private class Counter {

        public int classification;
        public int count = 1;

        public Counter (int classification) {
            this.classification = classification;
        }

        public void incrementCount () {
            count++;
        }

    }

}
