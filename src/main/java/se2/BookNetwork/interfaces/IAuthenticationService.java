package se2.BookNetwork.interfaces;

import se2.BookNetwork.core.requests.user.RegistrationRequest;

public interface IAuthenticationService {
    /**
     * Register user
     * @param registrationRequest
     * @throws MessagingException 
     */
    public void register(RegistrationRequest registrationRequest);
}
