package agency.akcom.mmg.sherlock.ui.domain;

import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.shared.domain.DatastoreObject;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import agency.akcom.mmg.sherlock.ui.shared.enums.TypeConnection;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import java.util.ArrayList;

@Entity
public class Dsp extends DatastoreObject {

    private static final long serialVersionUID = 1L;
    @Index
    private Partner partner;
    private String name;
    private TypeConnection typeConnection;
    private ArrayList<ConfigConnection> configConnections;

    public Dsp() {
    }

    public Dsp(Partner partner, String name) {
        this.partner = partner;
        this.name = name;
    }

    public Dsp(Partner partner, String name, TypeConnection typeConnection) {
        this.partner = partner;
        this.name = name;
        this.typeConnection = typeConnection;
        this.configConnections = new ArrayList<>();
    }

    public Dsp(long id, Partner partner, String name, TypeConnection typeConnection, ArrayList<ConfigConnection> configConnections) {
        this.setId(id);
        this.partner = partner;
        this.name = name;
        this.typeConnection = typeConnection;
        this.configConnections = configConnections;

    }

    public ArrayList<ConfigConnection> getConfigConnections() {
        return configConnections;
    }

    public void setConfigConnections(ArrayList<ConfigConnection> configConnections) {
        this.configConnections = configConnections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public TypeConnection getTypeConnection() {
        return typeConnection;
    }

    public void setTypeConnection(TypeConnection typeConnection) {
        this.typeConnection = typeConnection;
    }
}