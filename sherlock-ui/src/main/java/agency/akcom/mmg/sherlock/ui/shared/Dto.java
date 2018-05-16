package agency.akcom.mmg.sherlock.ui.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Dto implements IsSerializable, Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
