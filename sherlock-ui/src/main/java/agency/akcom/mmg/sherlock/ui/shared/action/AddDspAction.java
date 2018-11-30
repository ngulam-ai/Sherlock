package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.rpc.shared.Action;

public class AddDspAction implements Action<AddDspResult> { 

  java.lang.String name;
  java.lang.String partnerName;
  java.lang.String typeConnection;

  protected AddDspAction() {
    // Possibly for serialization.
  }

  public AddDspAction(java.lang.String name, java.lang.String partnerName, java.lang.String typeConnection) {
    this.name = name;
    this.partnerName = partnerName;
    this.typeConnection = typeConnection;
  }

  @Override
  public String getServiceName() {
    return "dispatch/";
  }

  @Override
  public boolean isSecured() {
    return false;
  }

  public java.lang.String getName(){
    return name;
  }

  public java.lang.String getPartnerName(){
    return partnerName;
  }

  public java.lang.String getTypeConnection(){
    return typeConnection;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    AddDspAction other = (AddDspAction) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (partnerName == null) {
      if (other.partnerName != null)
        return false;
    } else if (!partnerName.equals(other.partnerName))
      return false;
    if (typeConnection == null) {
      if (other.typeConnection != null)
        return false;
    } else if (!typeConnection.equals(other.typeConnection))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (name == null ? 1 : name.hashCode());
    hashCode = (hashCode * 37) + (partnerName == null ? 1 : partnerName.hashCode());
    hashCode = (hashCode * 37) + (typeConnection == null ? 1 : typeConnection.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "AddDspAction["
                 + name
                 + ","
                 + partnerName
                 + ","
                 + typeConnection
    + "]";
  }
}
