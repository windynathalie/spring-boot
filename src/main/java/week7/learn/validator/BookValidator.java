package week7.learn.validator;

import java.util.Optional;

import org.springframework.stereotype.Service;

import week7.learn.exception.customException.CustomNotFoundException;
import week7.learn.model.entity.Book;

@Service
public class BookValidator {
    public void validateBookNotFound(Optional<Book> bookOpt) throws CustomNotFoundException {
        if (bookOpt.isEmpty()) {
            throw new CustomNotFoundException("Book not found. Please input another book");
        }
    }

    public void validateBookInactive(Book book) throws Exception {
        if (book.getIsDeleted().equals(true)) {
            throw new Exception("Book cannot be borrowed because book is inactive!");
        }
    }
}
