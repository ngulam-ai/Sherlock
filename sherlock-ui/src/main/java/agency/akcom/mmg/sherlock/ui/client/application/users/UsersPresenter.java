package agency.akcom.mmg.sherlock.ui.client.application.users;

import java.util.List;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import agency.akcom.mmg.sherlock.ui.client.application.ApplicationPresenter;
import agency.akcom.mmg.sherlock.ui.client.dispatch.AsyncCallbackImpl;
import agency.akcom.mmg.sherlock.ui.client.place.NameTokens;
import agency.akcom.mmg.sherlock.ui.shared.action.DeleteUserAction;
import agency.akcom.mmg.sherlock.ui.shared.action.DeleteUserResult;
import agency.akcom.mmg.sherlock.ui.shared.action.GetUsersAction;
import agency.akcom.mmg.sherlock.ui.shared.action.GetUsersResult;
import agency.akcom.mmg.sherlock.ui.shared.action.SetUserAdminAction;
import agency.akcom.mmg.sherlock.ui.shared.action.SetUserAdminResult;
import agency.akcom.mmg.sherlock.ui.shared.dto.UserDto;

public class UsersPresenter extends Presenter<UsersPresenter.MyView, UsersPresenter.MyProxy> implements UsersUiHandlers {
	interface MyView extends View, HasUiHandlers<UsersUiHandlers> {

		void displayUsers(List<UserDto> userList);

		void removeUser(UserDto userDto);

		void updateUser(UserDto userDto, UserDto updatedUserDto);
	}

	@ProxyStandard
	@NameToken(NameTokens.USERS)
	interface MyProxy extends ProxyPlace<UsersPresenter> {
	}

	private final DispatchAsync dispatcher;

	@Inject
	UsersPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);

		getView().setUiHandlers(this);

		this.dispatcher = dispatcher;
	}

	@Override
	protected void onReset() {
		super.onReset();

		dispatcher.execute(new GetUsersAction(), new AsyncCallbackImpl<GetUsersResult>() {

			@Override
			public void onSuccess(GetUsersResult result) {
				getView().displayUsers(result.getUsers());

			}
		});

	}

	@Override
	public void onUserDeleteUpdate(final UserDto userDto) {
		dispatcher.execute(new DeleteUserAction(userDto.getId()), new AsyncCallbackImpl<DeleteUserResult>() {

			@Override
			public void onSuccess(DeleteUserResult result) {
				getView().removeUser(userDto);

			}
		});

	}

	@Override
	public void onIsAdminUpdate(final UserDto userDto, Boolean value) {
		dispatcher.execute(new SetUserAdminAction(userDto.getId(), value), new AsyncCallbackImpl<SetUserAdminResult>() {

			@Override
			public void onSuccess(SetUserAdminResult result) {
				// TODO is it really has sense to update other user attributes from server?
				getView().updateUser(userDto, result.getUserDto());

			}
		});

	}

}