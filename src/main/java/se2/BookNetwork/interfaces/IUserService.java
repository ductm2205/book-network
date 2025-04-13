package se2.BookNetwork.interfaces;


import se2.BookNetwork.core.PageResponse;
import se2.BookNetwork.core.requests.user.ChangePasswordRequest;
import se2.BookNetwork.core.requests.user.RegistrationRequest;
import se2.BookNetwork.core.requests.user.UpdateProfileRequest;
import se2.BookNetwork.models.common.User;

public interface IUserService {
    User registerUser(RegistrationRequest request);

    User updateUserProfile(UpdateProfileRequest request);

    void changePassword(Integer userId, ChangePasswordRequest request);

    void assignRoleToUser(Integer userId, String role);

    void removeRoleFromUser(Integer userId, String role);

    void setAccountLocked(Integer userId, boolean lock);

    void setAccountEnabled(Integer userId, boolean enabled);

    User getUserById(Integer id);

    User getUserByEmail(String email);

    PageResponse<User> getAllUsers(int pageNumber, int pageSize);
}
