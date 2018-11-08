package agency.akcom.mmg.sherlock.ui.server.dsp.perfectaudience.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
	String status;
	String token;
}
