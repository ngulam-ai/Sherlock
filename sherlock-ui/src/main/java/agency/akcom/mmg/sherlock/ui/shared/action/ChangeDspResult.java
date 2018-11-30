package agency.akcom.mmg.sherlock.ui.shared.action;



import com.gwtplatform.dispatch.rpc.shared.Result;

public class ChangeDspResult implements Result { 

  agency.akcom.mmg.sherlock.ui.shared.dto.DspDto outDspDto;

  protected ChangeDspResult() {
    // Possibly for serialization.
  }

  public ChangeDspResult(agency.akcom.mmg.sherlock.ui.shared.dto.DspDto outDspDto) {
    this.outDspDto = outDspDto;
  }

  public agency.akcom.mmg.sherlock.ui.shared.dto.DspDto getOutDspDto(){
    return outDspDto;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    ChangeDspResult other = (ChangeDspResult) obj;
    if (outDspDto == null) {
      if (other.outDspDto != null)
        return false;
    } else if (!outDspDto.equals(other.outDspDto))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (outDspDto == null ? 1 : outDspDto.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "ChangeDspResult["
                 + outDspDto
    + "]";
  }
}
