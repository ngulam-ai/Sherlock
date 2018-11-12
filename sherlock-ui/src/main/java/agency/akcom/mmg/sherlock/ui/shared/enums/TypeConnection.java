package agency.akcom.mmg.sherlock.ui.shared.enums;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public enum TypeConnection implements Serializable, IsSerializable {
    TOKEN,
    LOGIN,
    SECRET_ID;

    TypeConnection() {}
}
