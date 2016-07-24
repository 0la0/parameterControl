package etc.a0la0.osccontroller.app.data;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import etc.a0la0.osccontroller.app.data.entities.Option;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;

public class Model {

    private List<Option> optionList = new ArrayList<>();
    private Context context;

    public Model(Context context) {
        this.context = context;
        optionList = FileWriterUtil.getModelFromStore(context);
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public void set(int position, String title, String ipAddress, int port, List<Parameter> parameterList, List<Preset> presetList) {
        optionList.set(position, new Option(title, ipAddress, port, parameterList, presetList));
        persist();
    }

    public Option addOption() {
        String optionName = getUniqueUntitledName();
        optionList.add(new Option(optionName, "127.0.0.1", 8080, new ArrayList<>(), new ArrayList<>()));
        persist();
        return optionList.get(optionList.size() - 1);
    }

    public void removeOption(int position) {
        optionList.remove(position);
        persist();
    }

    private String getUniqueUntitledName() {
        String optionName = "Untitled " + optionList.size();
        List<String> optionNames = Stream.of(optionList)
                .map(option -> option.getTitle())
                .collect(Collectors.toList());

        boolean listContainsName = true;
        while (listContainsName) {
            boolean iterationHasMatchingName = false;
            for (String option : optionNames) {
                if (option.equals(optionName)) {
                    optionName = optionName + ".x";
                    iterationHasMatchingName = true;
                }
            }
            listContainsName = iterationHasMatchingName;
        }
        return optionName;
    }

    public void persist() {
        FileWriterUtil.storeModel(context, optionList);
    }

}
