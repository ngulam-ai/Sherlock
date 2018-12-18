package agency.akcom.mmg.sherlock.ui.server.dao;

import static agency.akcom.mmg.sherlock.ui.server.dao.objectify.OfyService.ofy;

import java.util.ArrayList;

import com.googlecode.objectify.cmd.Query;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;

public class DspDao extends BaseDao<Dsp> {

    public DspDao() {
        super(Dsp.class);
    }

   /* public Dsp findById(Long id) throws TooManyResultsException {
        return getByProperty("id",id);
    }*/

	public ArrayList<ConfigConnection> getCredentials(Partner partner) throws NoSuchFieldException {
		Query<Dsp> credentials = ofy().load().type(Dsp.class).filter("partner", partner);
		if (credentials.list().size() != 0) {
			Dsp dsp = credentials.list().get(0);
			return dsp.getConfigConnections();
		} else {
			throw new NoSuchFieldException();
		}
	}
}
