package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.rpc.shared.Action;

public class GetDspAction implements Action<GetDspResult> { 

  java.lang.Long id;

  protected GetDspAction() {
    // Possibly for serialization.
  }

  public GetDspAction(java.lang.Long id) {
    this.id = id;
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    GetDspAction other = (GetDspAction) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (id == null ? 1 : id.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "GetDspAction["
                 + id
    + "]";
  }
}
