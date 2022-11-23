package week7.learn.validator;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import week7.learn.exception.customException.CustomBadRequestException;
import week7.learn.exception.customException.CustomNotFoundException;
// import week7.learn.model.entity.Book;
import week7.learn.model.entity.BorrowBook;
import week7.learn.model.entity.User;

@Service
public class BorrowValidator {

    private BorrowBook borrowBook;

    public void validateBookBorrowedByUser(Optional<BorrowBook> borrowBookOpt) throws CustomBadRequestException {
        if (borrowBookOpt.isPresent()) {
            borrowBook = borrowBookOpt.get();
            String message = "You already borrow " + borrowBook.getBook().getTitle();
            throw new CustomBadRequestException(message);
        }
    }

    public void validateBookBorrowed(Optional<BorrowBook> borrowBookOpt1) throws CustomBadRequestException {
        if (borrowBookOpt1.isPresent()) {
            borrowBook = borrowBookOpt1.get();
            String message = borrowBook.getBook().getTitle() + " cannot be borrowed because book has been borrowed by "
                    + borrowBook.getUser().getEmail();
            throw new CustomBadRequestException(message);
        }
    }

    public void validateBookBorrowedByUser2(Optional<BorrowBook> borrowBookOpt) throws CustomBadRequestException {
        if (borrowBookOpt.isEmpty()) {
            throw new CustomBadRequestException("User not borrow the book!");
        }
    }

    public void validateBookBorrowedForUpdate(Optional<BorrowBook> borrowBookOpt, User user)
            throws CustomBadRequestException {
        if (borrowBookOpt.isPresent()) {
            borrowBook = borrowBookOpt.get();
            if (borrowBook.getUser().equals(user)) {
                throw new CustomBadRequestException("No edit changes!");
            } else {
                throw new CustomBadRequestException(
                        "Book cannot be borrowed because book has been borrowed by someone!");
            }
        }
    }

    public void validateGetBorrow(List<BorrowBook> borrowBooks) throws CustomNotFoundException {
        if (borrowBooks == null || borrowBooks.isEmpty()) {
            throw new CustomNotFoundException("Empty Data!");
        }
    }

    public void validateBorrowNotFound(Optional<BorrowBook> borrowBookOpt) throws Exception {
        if (borrowBookOpt.isEmpty()) {
            throw new CustomNotFoundException("Borrow not found. Please input correct borrow id");
        }
    }
}
