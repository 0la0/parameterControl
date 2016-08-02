package etc.a0la0.osccontroller.app.data.entities;

import java.util.List;

/**
 * Created by lukeanderson on 7/10/16.
 */
public class Option {

    private String title;
    private String ipAddress;
    private int port;
    private List<Parameter> parameterList;
    private List<Preset> presetList;
    private List<SpacePreset> spacePresetList;

    public Option(String title, String ipAddress, int port, List<Parameter> parameterList, List<Preset> presetList) {
        this.title = title;
        this.ipAddress = ipAddress;
        this.port = port;
        this.parameterList = parameterList;
        this.presetList = presetList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }

    public List<Preset> getPresetList() {
        return presetList;
    }

    public void setPresetList(List<Preset> presetList) {
        this.presetList = presetList;
    }

    public void addPreset(Preset preset) {
        presetList.add(preset);
    }

    public List<SpacePreset> getSpacePresetList() {
        return spacePresetList;
    }

    public void setSpacePresetList(List<SpacePreset> spacePresetList) {
        this.spacePresetList = spacePresetList;
    }
}
