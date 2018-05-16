package agency.akcom.mmg.sherlock.ui.shared.action;



import com.gwtplatform.dispatch.rpc.shared.Result;

public class GetCurrentUserResult implements Result { 

  agency.akcom.mmg.sherlock.ui.shared.UserDto currentUserDto;

  protected GetCurrentUserResult() {
    // Possibly for serialization.
  }

  public GetCurrentUserResult(agency.akcom.mmg.sherlock.ui.shared.UserDto currentUserDto) {
    this.currentUserDto = currentUserDto;
  }

  public agency.akcom.mmg.sherlock.ui.shared.UserDto getCurrentUserDto(){
    return currentUserDto;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    GetCurrentUserResult other = (GetCurrentUserResult) obj;
    if (currentUserDto == null) {
      if (other.currentUserDto != null)
        return false;
    } else if (!currentUserDto.equals(other.currentUserDto))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (currentUserDto == null ? 1 : currentUserDto.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "GetCurrentUserResult["
                 + currentUserDto
    + "]";
  }
}
