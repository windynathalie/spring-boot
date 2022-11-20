package week7.learn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import week7.learn.model.entity.Book;
import week7.learn.model.entity.Category;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategory(Category category);
}
