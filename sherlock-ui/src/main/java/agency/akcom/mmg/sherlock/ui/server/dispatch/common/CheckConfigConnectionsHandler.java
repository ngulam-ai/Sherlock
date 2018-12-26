package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import agency.akcom.mmg.sherlock.ui.server.avazu.AvazuUtils;
import agency.akcom.mmg.sherlock.ui.server.configConnection.SecretIdConnection;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.server.pocket.PocketUtils;
import agency.akcom.mmg.sherlock.ui.shared.action.CheckConfigConnectionsAction;
import agency.akcom.mmg.sherlock.ui.shared.action.CheckConfigConnectionsResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.ConfigConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.SecretIdConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.dto.TokenConnectionDto;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class CheckConfigConnectionsHandler extends MyAbstractActionHandler<CheckConfigConnectionsAction, CheckConfigConnectionsResult> {
    public CheckConfigConnectionsHandler() {
        super(CheckConfigConnectionsAction.class);
    }

    @Override
    public CheckConfigConnectionsResult execute(CheckConfigConnectionsAction action, ExecutionContext context) throws ActionException {
        Partner curentPartner = action.getPartner();
        ConfigConnectionDto curentConfigConnectionDto = action.getConfigConnectionDto();
        Boolean result = new Boolean(false);
        if (curentPartner!=null&&curentConfigConnectionDto!=null) {
            switch (curentPartner) {
                case AVAZU: {
                    SecretIdConnectionDto secretIdConnectionDto = (SecretIdConnectionDto) curentConfigConnectionDto;
                    SecretIdConnection secretIdConnection = new SecretIdConnection(secretIdConnectionDto.getName(), secretIdConnectionDto.getClientId(), secretIdConnectionDto.getClientSecret(), secretIdConnectionDto.getGrantType());
                    AvazuUtils avazuUtils = new AvazuUtils(secretIdConnection);
                    result = avazuUtils.checkingValidCredentials();
                    break;
                }
                case POCKETMATH: {
                    TokenConnectionDto tokenConnectionDto = (TokenConnectionDto) curentConfigConnectionDto;
                    result = PocketUtils.checkingValidCredentials(tokenConnectionDto.getToken());
                    break;
                }
            }
        }
        return new CheckConfigConnectionsResult(result);
    }
}
