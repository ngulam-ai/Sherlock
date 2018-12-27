package agency.akcom.mmg.sherlock.ui.server.dispatch.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import agency.akcom.mmg.sherlock.ui.server.dao.ImportLogDao;
import agency.akcom.mmg.sherlock.ui.server.dispatch.MyAbstractActionHandler;
import agency.akcom.mmg.sherlock.ui.shared.action.GetImportLogAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetImportLogResult;
import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;

public class GetImportLogHandler extends MyAbstractActionHandler<GetImportLogAction, GetImportLogResult> {
	@Inject
	public GetImportLogHandler() {
		super(GetImportLogAction.class);
	}

	@Override
	public GetImportLogResult execute(GetImportLogAction action, ExecutionContext context) throws ActionException {
		List<ImportLog> importLogs = new ImportLogDao().listAll();
		importLogs.sort(new Comparator<ImportLog>() {
			@Override
			public int compare(ImportLog o1, ImportLog o2) {
				return o2.getStart().compareTo(o1.getStart());
			}
		});
		// for (AppUser appUser : appUsers) {
		// userDtos.add(ServerUtils.createUserDto(null, appUser));
		// }
		return new GetImportLogResult(new ArrayList<ImportLog>(importLogs));
	}

}
