package agency.akcom.mmg.sherlock.ui.client.application.users;

import com.gwtplatform.mvp.client.UiHandlers;

import agency.akcom.mmg.sherlock.ui.shared.dto.UserDto;

public interface UsersUiHandlers extends UiHandlers {

	void onUserDeleteUpdate(UserDto userDto);

	void onIsAdminUpdate(UserDto userDto, Boolean value);
}
