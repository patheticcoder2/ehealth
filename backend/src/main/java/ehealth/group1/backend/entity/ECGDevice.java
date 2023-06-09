package ehealth.group1.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all important information about the configuration, type and id of an ecg device.
 * Uses ECGDeviceComponent to represent individual ecg components (leads) of the ECGDevice.
 */
@Entity
@NoArgsConstructor
@Getter @Setter
public class ECGDevice {
    // Internal database id for device
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Device self-identification string
    private String selfID;

    // Unique identifier for the device, internal
    private String identifier;

    // Display name for the device, for example "Custom Arduino ECG"
    private String name;

    // Number of leads (ecg electrodes) of device
    private int leads;

    // Pin for registering device to user
    private String pin;

    // Lead info
    @OneToMany(cascade = CascadeType.ALL)
    private List<ECGDeviceComponent> components = new ArrayList<>();

    public ECGDevice(Long id, String selfID, String identifier, String name, int leads, String pin, List<ECGDeviceComponent> components)
            throws IllegalStateException {
        this.id = id;
        this.selfID = selfID;
        this.identifier = identifier;
        this.name = name;
        this.leads = leads;
        this.pin = pin;
        this.components = components;

        if (leads != components.size()) {
            throw new IllegalStateException("ECGDevice(): Leads argument and internal count of leads is not equal [Leads: " +
                    leads + ", component count: " + components.size() + "]. Can't construct Object ECGDevice!");
        }
    }

    public ECGDevice(String selfID, String identifier, String name, int leads, String pin, List<ECGDeviceComponent> components)
            throws IllegalStateException {
        this.selfID = selfID;
        this.identifier = identifier;
        this.name = name;
        this.leads = leads;
        this.pin = pin;
        this.components = components;

        if (leads != components.size()) {
            throw new IllegalStateException("ECGDevice(): Leads argument and internal count of leads is not equal [Leads: " +
                    leads + ", component count: " + components.size() + "]. Can't construct Object ECGDevice!");
        }
    }

    @Override
    public String toString() {
        return "ECGDevice[" +
                "id=" + id +
                ",selfID='" + selfID + "'" +
                ",identifier='" + identifier + "'" +
                ",name='" + name + "'" +
                ",leads=" + leads +
                ",pin='" + pin + "'" +
                ",components=" + components.toString() +
                ']';
    }
}
