package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.rpc.shared.Action;

public class SetUserAdminAction implements Action<SetUserAdminResult> { 

  java.lang.Long id;
  java.lang.Boolean value;

  protected SetUserAdminAction() {
    // Possibly for serialization.
  }

  public SetUserAdminAction(java.lang.Long id, java.lang.Boolean value) {
    this.id = id;
    this.value = value;
  }

  @Override
  public String getServiceName() {
    return "dispatch/";
  }

  @Override
  public boolean isSecured() {
    return false;
  }

  public java.lang.Long getId(){
    return id;
  }

  public java.lang.Boolean getValue(){
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    SetUserAdminAction other = (SetUserAdminAction) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (id == null ? 1 : id.hashCode());
    hashCode = (hashCode * 37) + (value == null ? 1 : value.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "SetUserAdminAction["
                 + id
                 + ","
                 + value
    + "]";
  }
}
