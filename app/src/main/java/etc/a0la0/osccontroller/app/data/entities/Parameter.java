package etc.a0la0.osccontroller.app.data.entities;

import java.util.UUID;

public class Parameter {

    private String uniqueId;
    private String address;

    public Parameter() {
        setAddress("/address");
        uniqueId = createUniqueId();
    }

    public Parameter(String address) {
        setAddress(address);
        uniqueId = createUniqueId();
    }

    public Parameter(String address, String uniqueId) {
        setAddress(address);
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String createUniqueId() {
        return UUID.randomUUID().toString();
    }

}
