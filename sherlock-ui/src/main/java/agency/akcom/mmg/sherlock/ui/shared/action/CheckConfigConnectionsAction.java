package agency.akcom.mmg.sherlock.ui.shared.action;

import com.gwtplatform.dispatch.rpc.shared.Action;

public class CheckConfigConnectionsAction implements Action<CheckConfigConnectionsResult> { 

  agency.akcom.mmg.sherlock.ui.shared.enums.Partner partner;
  agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto configConnectionDto;

  protected CheckConfigConnectionsAction() {
    // Possibly for serialization.
  }

  public CheckConfigConnectionsAction(agency.akcom.mmg.sherlock.ui.shared.enums.Partner partner, agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto configConnectionDto) {
    this.partner = partner;
    this.configConnectionDto = configConnectionDto;
  }

  @Override
  public String getServiceName() {
    return "dispatch/";
  }

  @Override
  public boolean isSecured() {
    return false;
  }

  public agency.akcom.mmg.sherlock.ui.shared.enums.Partner getPartner(){
    return partner;
  }

  public agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto getConfigConnectionDto(){
    return configConnectionDto;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
        return true;
    if (obj == null)
        return false;
    if (getClass() != obj.getClass())
        return false;
    CheckConfigConnectionsAction other = (CheckConfigConnectionsAction) obj;
    if (partner == null) {
      if (other.partner != null)
        return false;
    } else if (!partner.equals(other.partner))
      return false;
    if (configConnectionDto == null) {
      if (other.configConnectionDto != null)
        return false;
    } else if (!configConnectionDto.equals(other.configConnectionDto))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 23;
    hashCode = (hashCode * 37) + (partner == null ? 1 : partner.hashCode());
    hashCode = (hashCode * 37) + (configConnectionDto == null ? 1 : configConnectionDto.hashCode());
    return hashCode;
  }

  @Override
  public String toString() {
    return "CheckConfigConnectionsAction["
                 + partner
                 + ","
                 + configConnectionDto
    + "]";
  }
}
