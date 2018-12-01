package agency.akcom.mmg.sherlock.ui.shared.action;



import com.gwtplatform.dispatch.rpc.shared.Result;

public class GetAllDspResult implements Result { 

  java.util.ArrayList<agency.akcom.mmg.sherlock.ui.shared.dto.DspDto> dspDtos;

  protected GetAllDspResult() {
    // Possibly for serialization.
  }

  public GetAllDspResult(java.util.ArrayList<agency.akcom.mmg.sherlock.ui.shared.dto.DspDto> dspDtos) {
    this.dspDtos = dspDtos;
  }

  public java.util.ArrayList<agency.akcom.mmg.sherlock.ui.shared.dto.DspDto> getDspDtos(){
    return dspDtos;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    GetAllDspResult other = (GetAllDspResult) obj;
    if (dspDtos == null) {
      if (other.dspDtos != null)
        return false;
    } else if (!dspDtos.equals(other.dspDtos))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (dspDtos == null ? 1 : dspDtos.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "GetAllDspResult["
                 + dspDtos
    + "]";
  }
}
