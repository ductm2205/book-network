package se2.BookNetwork.interfaces;

import se2.BookNetwork.models.common.User;

public interface IFavouriteService {
    Integer createFavourite(User currentUser);

    Integer updateFavourite(Integer favouriteId, User user);

    Integer deleteFavourInteger(Integer favouriteId, User user);
}
