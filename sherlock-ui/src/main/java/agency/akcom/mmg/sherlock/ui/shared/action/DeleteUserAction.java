package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.rpc.shared.Action;

public class DeleteUserAction implements Action<DeleteUserResult> { 

  java.lang.Long Id;

  protected DeleteUserAction() {
    // Possibly for serialization.
  }

  public DeleteUserAction(java.lang.Long Id) {
    this.Id = Id;
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
    return Id;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    DeleteUserAction other = (DeleteUserAction) obj;
    if (Id == null) {
      if (other.Id != null)
        return false;
    } else if (!Id.equals(other.Id))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (Id == null ? 1 : Id.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "DeleteUserAction["
                 + Id
    + "]";
  }
}
