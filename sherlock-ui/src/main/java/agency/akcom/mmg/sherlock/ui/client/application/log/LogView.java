package agency.akcom.mmg.sherlock.ui.client.application.log;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import agency.akcom.mmg.sherlock.ui.shared.domain.ImportLog;

class LogView extends ViewWithUiHandlers<LogUiHandlers> implements LogPresenter.MyView {
	interface Binder extends UiBinder<Widget, LogView> {
	}

	@UiField(provided = true)
	CellTable<ImportLog> logTable = new CellTable<ImportLog>(30);
	@UiField
	Pagination logTablePagination;

	private SimplePager logTablePager = new SimplePager();
	private ListDataProvider<ImportLog> logTableProvider = new ListDataProvider<ImportLog>();

	@Inject
	LogView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));

		initTable();
	}

	private void initTable() {
		// Partner
		final TextColumn<ImportLog> partnerTextColumn = new TextColumn<ImportLog>() {
			@Override
			public String getValue(final ImportLog importLog) {
				return importLog.getPartner().toString();
			}
		};
		logTable.addColumn(partnerTextColumn);

		// Start
		final TextColumn<ImportLog> startTextColumn = new TextColumn<ImportLog>() {
			@Override
			public String getValue(final ImportLog importLog) {
				return importLog.getStart().toString();
			}
		};
		logTable.addColumn(startTextColumn);

		// End
		final TextColumn<ImportLog> endTextColumn = new TextColumn<ImportLog>() {
			@Override
			public String getValue(final ImportLog importLog) {
				Date end = importLog.getEnd();
				return end != null ? end.toString() : "";
			}
		};
		logTable.addColumn(endTextColumn);

		// Status
		final TextColumn<ImportLog> statusTextColumn = new TextColumn<ImportLog>() {
			@Override
			public String getValue(final ImportLog importLog) {
				return importLog.getStatus().toString();
			}
		};
		logTable.addColumn(statusTextColumn);

		logTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {

			@Override
			public void onRangeChange(final RangeChangeEvent event) {
				logTablePagination.rebuild(logTablePager);
			}
		});

		logTablePager.setDisplay(logTable);
		logTablePagination.clear();
		logTableProvider.addDataDisplay(logTable);
	}

	@Override
	public void displayLogs(List<ImportLog> importLogs) {
		logTableProvider.getList().clear();
		logTableProvider.getList().addAll(importLogs);
		logTableProvider.flush();
		logTablePagination.rebuild(logTablePager);
	}

}