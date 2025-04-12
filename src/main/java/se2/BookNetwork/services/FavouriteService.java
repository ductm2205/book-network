package se2.BookNetwork.services;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.exceptions.FavouriteExistException;
import se2.BookNetwork.interfaces.IFavouriteService;
import se2.BookNetwork.models.common.Favourite;
import se2.BookNetwork.models.common.User;
import se2.BookNetwork.repositories.FavouriteRepository;

@Service
@RequiredArgsConstructor
public class FavouriteService implements IFavouriteService {

    private final FavouriteRepository favouriteRepository;

    @Override
    public Integer createFavourite(User currentUser) {
        var hasFavourite = favouriteRepository.findByOwnerId(currentUser.getId());

        if (hasFavourite.isPresent()) {
            throw new FavouriteExistException("Favourite with this user already exists");
        }

        var favourite = Favourite
                .builder()
                .owner(currentUser)
                .createdBy(currentUser.getEmail())
                .name(currentUser.getUsername())
                .build();

        return favouriteRepository.save(favourite).getId();
    }

    @Override
    public Integer updateFavourite(Integer favouriteId, User user) {
        throw new UnsupportedOperationException("Unimplemented method 'updateFavourite'");
    }

    @Override
    public Integer deleteFavour(Integer favouriteId, User user) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteFavourInteger'");
    }

    @Override
    public Favourite getById(Integer id) {
        return favouriteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Favourite not found!"));
    }

}
