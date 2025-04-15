package se2.BookNetwork.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import se2.BookNetwork.core.constants.Roles;
import se2.BookNetwork.models.common.Book;
import se2.BookNetwork.models.common.Favourite;
import se2.BookNetwork.models.common.Role;
import se2.BookNetwork.models.common.User;
import se2.BookNetwork.repositories.BookRepository;
import se2.BookNetwork.repositories.FavouriteRepository;
import se2.BookNetwork.repositories.RoleRepository;
import se2.BookNetwork.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer {

        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final FavouriteRepository favouriteRepository;
        private final BookRepository bookRepository;
        private final PasswordEncoder passwordEncoder;

        // Classic books data for realistic seeding
        private final List<Object[]> classicBooks = Arrays.asList(
                        new Object[] { "To Kill a Mockingbird", "Harper Lee", "978-0446310789",
                                        "Set in the 1930s American South, the story follows young Scout Finch and her father's defense of a Black man accused of rape." },
                        new Object[] { "1984", "George Orwell", "978-0451524935",
                                        "A dystopian novel set in a totalitarian society where critical thought is suppressed under a regime of surveillance and propaganda." },
                        new Object[] { "Pride and Prejudice", "Jane Austen", "978-0141439518",
                                        "The story follows Elizabeth Bennet as she deals with issues of manners, upbringing, morality, and marriage in early 19th-century England." },
                        new Object[] { "The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565",
                                        "Set in the Jazz Age, the novel explores themes of decadence, idealism, resistance to change, and excess." },
                        new Object[] { "Moby-Dick", "Herman Melville", "978-0142437247",
                                        "The narrative tells the adventures of wandering sailor Ishmael and his voyage on the whaling ship Pequod, commanded by Captain Ahab." },
                        new Object[] { "War and Peace", "Leo Tolstoy", "978-0143039990",
                                        "Chronicles the French invasion of Russia and the impact of the Napoleonic era on Tsarist society." },
                        new Object[] { "The Catcher in the Rye", "J.D. Salinger", "978-0316769488",
                                        "The story of Holden Caulfield, a teenage boy dealing with complex issues of identity, belonging, and connection." },
                        new Object[] { "The Hobbit", "J.R.R. Tolkien", "978-0547928227",
                                        "The adventures of hobbit Bilbo Baggins as he journeys to the Lonely Mountain with a spirited group of dwarves." },
                        new Object[] { "Crime and Punishment", "Fyodor Dostoevsky", "978-0143058144",
                                        "The story of the mental anguish and moral dilemmas of Rodion Raskolnikov, an impoverished ex-student in Saint Petersburg." },
                        new Object[] { "The Lord of the Rings", "J.R.R. Tolkien", "978-0618640157",
                                        "Epic high-fantasy novel that follows hobbits Frodo and Sam as they journey to destroy the One Ring." });

        @Bean
        public CommandLineRunner seedData() {
                return args -> {
                        // Seed roles first
                        if (roleRepository.findByName(Roles.USER).isEmpty()) {
                                roleRepository.save(Role.builder().name(Roles.USER).build());
                        }
                        if (roleRepository.findByName(Roles.ADMIN).isEmpty()) {
                                roleRepository.save(Role.builder().name(Roles.ADMIN).build());
                        }

                        // Then seed admin
                        User admin;
                        if (userRepository.findByEmail("admin@bsn.com").isEmpty()) {
                                Role adminRole = roleRepository.findByName(Roles.ADMIN)
                                                .orElseThrow(() -> new EntityNotFoundException("Role not found!"));
                                admin = User.builder()
                                                .firstname("System")
                                                .lastname("Admin")
                                                .email("admin@bsn.com")
                                                .password(passwordEncoder.encode("admin123")) // secure this!
                                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                                .isEnabled(true)
                                                .isAccountLocked(false)
                                                .roles(List.of(adminRole))
                                                .createdAt(LocalDateTime.now())
                                                .build();
                                userRepository.save(admin);

                        } else {
                                admin = userRepository.findByEmail("admin@bsn.com").get();
                        }

                        var adminFavourite = Favourite
                                        .builder()
                                        .owner(admin)
                                        .createdBy(admin.getEmail())
                                        .name(admin.getUsername())
                                        .createdAt(LocalDateTime.now())
                                        .build();
                        favouriteRepository.save(adminFavourite);

                        Faker faker = new Faker();
                        Random random = new Random();

                        // Seed users if none exist yet (except admin)
                        List<User> users = new ArrayList<>();
                        List<Favourite> favourites = new ArrayList<>();
                        if (userRepository.count() <= 1) {
                                var userRole = roleRepository.findByName(Roles.USER)
                                                .orElseThrow(() -> new EntityNotFoundException("Role user not found"));

                                for (int i = 0; i < 10; i++) {
                                        var firstname = faker.name().firstName();
                                        var lastname = faker.name().lastName();
                                        var password = firstname + "1234";
                                        var email = firstname.toLowerCase() + "." + lastname.toLowerCase()
                                                        + "@gmail.com";
                                        LocalDate randomDate = between();

                                        var user = User.builder()
                                                        .firstname(firstname)
                                                        .lastname(lastname)
                                                        .email(email)
                                                        .password(passwordEncoder.encode(password))
                                                        .roles(List.of(userRole))
                                                        .isAccountLocked(false)
                                                        .isEnabled(true)
                                                        .dateOfBirth(randomDate)
                                                        .createdAt(LocalDateTime.now())
                                                        .build();
                                        users.add(user);

                                        var favourite = Favourite.builder()
                                                        .owner(user)
                                                        .createdBy(user.getEmail())
                                                        .name(user.getUsername())
                                                        .createdAt(LocalDateTime.now())
                                                        .build();
                                        favourites.add(favourite);
                                }
                                userRepository.saveAll(users);

                                favouriteRepository.saveAll(favourites);
                                users = userRepository.findAll();
                        } else {
                                users = userRepository.findAll();
                        }

                        // Seed books if none exist yet
                        if (bookRepository.count() == 0) {
                                List<Book> books = new ArrayList<>();

                                // First, add classic books (reliable data)
                                for (Object[] bookData : classicBooks) {
                                        String title = (String) bookData[0];
                                        String author = (String) bookData[1];
                                        String isbn = (String) bookData[2];
                                        String synopsis = (String) bookData[3];

                                        // Assign to random user (including admin)
                                        User randomUser = users.get(random.nextInt(users.size()));

                                        Book book = Book.builder()
                                                        .title(title)
                                                        .authorName(author)
                                                        .isbn(isbn)
                                                        .synopsis(synopsis)
                                                        .bookCover("./uploads/book_cover.jpg")
                                                        .owner(randomUser)
                                                        .shareable(random.nextBoolean())
                                                        .archived(false)
                                                        .createdBy(randomUser.getEmail())
                                                        .createdAt(LocalDateTime.now())
                                                        .build();
                                        books.add(book);
                                }

                                // Then add some random books
                                for (int i = 0; i < 40; i++) {
                                        User randomUser = users.get(random.nextInt(users.size()));

                                        String title = faker.book().title();
                                        if (title.length() < 5) {
                                                // Add some words for very short titles
                                                title = title + " "
                                                                + faker.lorem().words(random.nextInt(3) + 1).stream()
                                                                                .reduce("", (a, b) -> a + " " + b)
                                                                                .trim();
                                        }

                                        Book book = Book.builder()
                                                        .title(title)
                                                        .authorName(faker.name().fullName())
                                                        .isbn("978-" + faker.number().digits(9))
                                                        .synopsis("A captivating tale about " + title.toLowerCase()
                                                                        + " and its impact on readers.")
                                                        .bookCover("./uploads/book_cover.jpg")
                                                        .owner(randomUser)
                                                        .shareable(random.nextBoolean())
                                                        .archived(false)
                                                        .createdBy(randomUser.getEmail())
                                                        .createdAt(LocalDateTime.now())
                                                        .build();
                                        books.add(book);
                                }

                                bookRepository.saveAll(books);
                        }
                };
        }

        private static LocalDate between() {
                LocalDate start = LocalDate.of(1989, Month.OCTOBER, 14);
                LocalDate end = LocalDate.now();
                long startEpochDay = start.toEpochDay();
                long endEpochDay = end.toEpochDay();
                long randomDay = ThreadLocalRandom
                                .current()
                                .nextLong(startEpochDay, endEpochDay);

                return LocalDate.ofEpochDay(randomDay);
        }
}