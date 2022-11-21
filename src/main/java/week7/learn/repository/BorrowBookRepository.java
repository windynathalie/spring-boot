package week7.learn.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import week7.learn.model.entity.Book;
import week7.learn.model.entity.BorrowBook;
import week7.learn.model.entity.User;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {
    Optional<BorrowBook> findIdByUserAndUpdatedAt(User user, LocalDateTime updatedAt);

    Optional<BorrowBook> findIdByBookAndUpdatedAt(Book book, LocalDateTime updatedAt);

    List<BorrowBook> findByUser(User user);

    List<BorrowBook> findByUpdatedAtNotNull();

    List<BorrowBook> findByUpdatedAtNull();

    Optional<BorrowBook> findByBookAndUserAndUpdatedAt(Book book, User user,
            LocalDateTime updatedAt);
}
