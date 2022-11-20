package week7.learn.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "borrow_book")
@Data
@NoArgsConstructor
// @AllArgsConstructor
public class BorrowBook {

    public BorrowBook(Book book, User user) {
        this.book = book;
        this.user = user;
    }

    public BorrowBook(Book book, User user, LocalDateTime createdAt) {
        this.book = book;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
