package agency.akcom.mmg.sherlock.ui.server.avazu.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
	String access_token; // The access token issued by Avazu authorization server.

	String expires_in; // the token will expire in 15 minutes

}
