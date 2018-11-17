package agency.akcom.mmg.sherlock.ui.server.dao;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.configConnection.ConfigConnection;
import agency.akcom.mmg.sherlock.ui.server.dao.BaseDao;
import agency.akcom.mmg.sherlock.ui.shared.TooManyResultsException;
import agency.akcom.mmg.sherlock.ui.shared.enums.Partner;
import com.googlecode.objectify.cmd.Query;
import static agency.akcom.mmg.sherlock.ui.server.dao.objectify.OfyService.ofy;

import java.util.ArrayList;


public class DspDao extends BaseDao<Dsp> {

    public DspDao() {
        super(Dsp.class);
    }

   /* public Dsp findById(Long id) throws TooManyResultsException {
        return getByProperty("id",id);
    }*/

	public ArrayList<ConfigConnection> getCredentials(Partner partner) {
		Query<Dsp> credentials = ofy().load().type(Dsp.class);
		credentials.filter("Partner", partner);
		Dsp dsp = credentials.list().get(0);
		return dsp.getConfigConnections();
	}
}
