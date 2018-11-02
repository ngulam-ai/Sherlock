package agency.akcom.mmg.sherlock.ui.shared.action;



import com.gwtplatform.dispatch.rpc.shared.Result;

public class GetUsersResult implements Result { 

  java.util.List<agency.akcom.mmg.sherlock.ui.shared.dto.UserDto> users;

  protected GetUsersResult() {
    // Possibly for serialization.
  }

  public GetUsersResult(java.util.List<agency.akcom.mmg.sherlock.ui.shared.dto.UserDto> users) {
    this.users = users;
  }

  public java.util.List<agency.akcom.mmg.sherlock.ui.shared.dto.UserDto> getUsers(){
    return users;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    GetUsersResult other = (GetUsersResult) obj;
    if (users == null) {
      if (other.users != null)
        return false;
    } else if (!users.equals(other.users))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (users == null ? 1 : users.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "GetUsersResult["
                 + users
    + "]";
  }
}
