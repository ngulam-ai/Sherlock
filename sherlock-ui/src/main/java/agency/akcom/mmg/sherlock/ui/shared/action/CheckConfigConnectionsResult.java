package agency.akcom.mmg.sherlock.ui.shared.action;



import com.gwtplatform.dispatch.rpc.shared.Result;

public class CheckConfigConnectionsResult implements Result { 

  java.lang.Boolean result;

  protected CheckConfigConnectionsResult() {
    // Possibly for serialization.
  }

  public CheckConfigConnectionsResult(java.lang.Boolean result) {
    this.result = result;
  }

  public java.lang.Boolean getResult(){
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    CheckConfigConnectionsResult other = (CheckConfigConnectionsResult) obj;
    if (result == null) {
      if (other.result != null)
        return false;
    } else if (!result.equals(other.result))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (result == null ? 1 : result.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "CheckConfigConnectionsResult["
                 + result
    + "]";
  }
}
