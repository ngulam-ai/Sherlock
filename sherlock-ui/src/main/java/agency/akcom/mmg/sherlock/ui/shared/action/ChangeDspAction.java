package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.rpc.shared.Action;

public class ChangeDspAction implements Action<ChangeDspResult> { 

  agency.akcom.mmg.sherlock.ui.shared.dto.DspDto dspDto;

  protected ChangeDspAction() {
    // Possibly for serialization.
  }

  public ChangeDspAction(agency.akcom.mmg.sherlock.ui.shared.dto.DspDto dspDto) {
    this.dspDto = dspDto;
  }

  @Override
  public String getServiceName() {
    return "dispatch/";
  }

  @Override
  public boolean isSecured() {
    return false;
  }

  public agency.akcom.mmg.sherlock.ui.shared.dto.DspDto getDspDto(){
    return dspDto;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    ChangeDspAction other = (ChangeDspAction) obj;
    if (dspDto == null) {
      if (other.dspDto != null)
        return false;
    } else if (!dspDto.equals(other.dspDto))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (dspDto == null ? 1 : dspDto.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "ChangeDspAction["
                 + dspDto
    + "]";
  }
}
