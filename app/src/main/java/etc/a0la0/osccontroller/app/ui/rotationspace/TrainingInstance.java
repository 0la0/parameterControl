package etc.a0la0.osccontroller.app.ui.rotationspace;

public class TrainingInstance {

    public float alpha;
    public float beta;
    public float gamma;
    public int classification;

    public TrainingInstance(float alpha, float beta, float gamma, int classification) {
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.classification = classification;
    }

    public TrainingInstance add(TrainingInstance trainingInstance) {
        alpha += trainingInstance.alpha;
        beta += trainingInstance.beta;
        gamma += trainingInstance.gamma;
        return this;
    }

    public TrainingInstance divide(float scalar) {
        alpha /= scalar;
        beta /= scalar;
        gamma /= scalar;
        return this;
    }

    public String toString() {
        return alpha + ", " + beta + ", " + gamma;
    }

}
