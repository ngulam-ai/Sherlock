package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.rpc.shared.Action;

public class GetImportLogAction implements Action<GetImportLogResult> { 


  public GetImportLogAction() {
    // Possibly for serialization.
  }

  @Override
  public String getServiceName() {
    return "dispatch/";
  }

  @Override
  public boolean isSecured() {
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String toString() {
    return "GetImportLogAction["
    + "]";
  }
}
