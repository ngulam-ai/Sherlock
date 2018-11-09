package agency.akcom.mmg.sherlock.ui.server.dao;

import agency.akcom.mmg.sherlock.ui.domain.Dsp;
import agency.akcom.mmg.sherlock.ui.server.dao.BaseDao;
import agency.akcom.mmg.sherlock.ui.shared.TooManyResultsException;

public class DspDao extends BaseDao<Dsp> {

    public DspDao() {
        super(Dsp.class);
    }

    public Dsp findById(Long id) throws TooManyResultsException {
        return getByProperty("id",id);
    }
}
