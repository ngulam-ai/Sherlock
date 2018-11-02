package agency.akcom.mmg.sherlock.ui.client.application.users;

import java.util.List;

import javax.inject.Inject;

import org.gwtbootstrap3.client.ui.Pagination;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ImageType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.RangeChangeEvent;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import agency.akcom.mmg.sherlock.ui.client.widget.MyImageCell;
import agency.akcom.mmg.sherlock.ui.shared.dto.UserDto;

class UsersView extends ViewWithUiHandlers<UsersUiHandlers> implements UsersPresenter.MyView {
	interface Binder extends UiBinder<Widget, UsersView> {
	}

	@UiField(provided = true)
	CellTable<UserDto> userTable = new CellTable<UserDto>(10);
	@UiField
	Pagination userTablePagination;

	private SimplePager userTablePager = new SimplePager();
	private ListDataProvider<UserDto> userTableProvider = new ListDataProvider<UserDto>();

	@Inject
	UsersView(Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));

		initTable();
	}

	private void initTable() {
		// User Pic
		final Column<UserDto, String> userImageColumn = new Column<UserDto, String>(
				new MyImageCell(ImageType.THUMBNAIL)) {

			@Override
			public String getValue(UserDto userDto) {
				return userDto.getPictureURL() + "?sz=50";
			}
		};
		userTable.addColumn(userImageColumn);

		// User Info
		final TextColumn<UserDto> nameTextColumn = new TextColumn<UserDto>() {

			@Override
			public String getValue(final UserDto userDto) {
				return userDto.getCustomerName() + " (" + userDto.getLogin() + ")";
			}
		};
		userTable.addColumn(nameTextColumn);

		// is Administrator
		Column<UserDto, Boolean> isAdminCheckColumn = new Column<UserDto, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(UserDto userDto) {
				return userDto.isAdmin();
			}
		};
		isAdminCheckColumn.setFieldUpdater(new FieldUpdater<UserDto, Boolean>() {

			@Override
			public void update(int index, UserDto userDto, Boolean value) {
				getUiHandlers().onIsAdminUpdate(userDto, value);
			}
		});
		userTable.addColumn(isAdminCheckColumn);

		// Delete button
		final Column<UserDto, String> actionButtonsColumn = new Column<UserDto, String>(
				new ButtonCell(ButtonType.DANGER, IconType.TRASH)) {
			@Override
			public String getValue(UserDto object) {
				return "";
			}
		};
		actionButtonsColumn.setFieldUpdater(new FieldUpdater<UserDto, String>() {
			@Override
			public void update(int index, UserDto userDto, String value) {
				getUiHandlers().onUserDeleteUpdate(userDto);
			}
		});
		userTable.addColumn(actionButtonsColumn);

		userTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {

			@Override
			public void onRangeChange(final RangeChangeEvent event) {
				userTablePagination.rebuild(userTablePager);
			}
		});

		userTablePager.setDisplay(userTable);
		userTablePagination.clear();
		userTableProvider.addDataDisplay(userTable);
	}

	@Override
	public void displayUsers(List<UserDto> userList) {
		userTableProvider.getList().clear();
		userTableProvider.getList().addAll(userList);
		userTableProvider.flush();
		userTablePagination.rebuild(userTablePager);
	}

	@Override
	public void removeUser(UserDto userDto) {
		userTableProvider.getList().remove(userDto);
		userTableProvider.flush();
		userTablePagination.rebuild(userTablePager);
	}

	@Override
	public void updateUser(UserDto userDto, UserDto updatedUserDto) {
		int index = userTableProvider.getList().indexOf(userDto);
		userTableProvider.getList().set(index, updatedUserDto);
		userTableProvider.flush();
		// userTablePagination.rebuild(userTablePager);
	}
}