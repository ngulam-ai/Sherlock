package agency.akcom.mmg.sherlock.ui.server.avazu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
//	private String client_id = "100304";
//	private String client_secret = "02b7da4f561ca3be4db4ddd4aa4571cf";
//	private String grant_type = "client_credentials"; // default value: client_credentials
	
	private String client_id;
	private String client_secret;
	private String grant_type = "client_credentials"; // default value: client_credentials
}
