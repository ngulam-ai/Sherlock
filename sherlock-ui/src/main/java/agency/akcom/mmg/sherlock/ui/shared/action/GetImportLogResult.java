package agency.akcom.mmg.sherlock.ui.shared.action;



import com.gwtplatform.dispatch.rpc.shared.Result;

public class GetImportLogResult implements Result { 

  java.util.List<agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog> logs;

  protected GetImportLogResult() {
    // Possibly for serialization.
  }

  public GetImportLogResult(java.util.List<agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog> logs) {
    this.logs = logs;
  }

  public java.util.List<agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog> getLogs(){
    return logs;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    GetImportLogResult other = (GetImportLogResult) obj;
    if (logs == null) {
      if (other.logs != null)
        return false;
    } else if (!logs.equals(other.logs))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (logs == null ? 1 : logs.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "GetImportLogResult["
                 + logs
    + "]";
  }
}
