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

    public String toString() {
        return alpha + ", " + beta + ", " + gamma;
    }

}
