package etc.a0la0.osccontroller.app.data;

import android.content.Context;

public class ModelProvider {

    private static Model model = null;

    public static Model getModel(Context context) {
        if (model == null) {
            model = new Model(context);
        }
        return model;
    }

}
