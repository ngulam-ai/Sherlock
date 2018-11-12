package agency.akcom.mmg.sherlock.ui.shared.action;



import com.gwtplatform.dispatch.rpc.shared.Result;

public class AddDspResult implements Result { 

  agency.akcom.mmg.sherlock.ui.shared.dto.DspDto dspDto;

  protected AddDspResult() {
    // Possibly for serialization.
  }

  public AddDspResult(agency.akcom.mmg.sherlock.ui.shared.dto.DspDto dspDto) {
    this.dspDto = dspDto;
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
    AddDspResult other = (AddDspResult) obj;
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
    return "AddDspResult["
                 + dspDto
    + "]";
  }
}
