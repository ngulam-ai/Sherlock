package agency.akcom.mmg.sherlock.ui.shared.action;



import com.gwtplatform.dispatch.rpc.shared.Result;

public class SetUserAdminResult implements Result { 

  agency.akcom.mmg.sherlock.ui.shared.UserDto userDto;

  protected SetUserAdminResult() {
    // Possibly for serialization.
  }

  public SetUserAdminResult(agency.akcom.mmg.sherlock.ui.shared.UserDto userDto) {
    this.userDto = userDto;
  }

  public agency.akcom.mmg.sherlock.ui.shared.UserDto getUserDto(){
    return userDto;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    SetUserAdminResult other = (SetUserAdminResult) obj;
    if (userDto == null) {
      if (other.userDto != null)
        return false;
    } else if (!userDto.equals(other.userDto))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (userDto == null ? 1 : userDto.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "SetUserAdminResult["
                 + userDto
    + "]";
  }
}
