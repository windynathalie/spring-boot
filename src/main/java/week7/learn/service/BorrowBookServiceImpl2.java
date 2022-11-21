package week7.learn.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
// import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import week7.learn.exception.customException.CustomBadRequestException;
import week7.learn.exception.customException.CustomNotFoundException;
import week7.learn.model.dto.request.BorrowBookDto;
import week7.learn.model.dto.response.ResponseData;
import week7.learn.model.entity.Book;
import week7.learn.model.entity.BorrowBook;
import week7.learn.model.entity.User;
import week7.learn.repository.BookRepository;
import week7.learn.repository.BorrowBookRepository;
import week7.learn.repository.UserRepository;
import week7.learn.validator.BookValidator;
import week7.learn.validator.BorrowValidator;
import week7.learn.validator.UserValidator;

@Service
@Transactional
public class BorrowBookServiceImpl2 implements BorrowBookService2 {
    @Autowired
    private BorrowBookRepository borrowBookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    private Book book;
    private User user;
    private BorrowBook borrowBook;
    private ResponseData<Object> responseData;
    private Map<Object, Object> data;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private BookValidator bookValidator;

    @Autowired
    private BorrowValidator borrowValidator;

    private List<BorrowBook> borrowBooks;
    List<Map<Object, Object>> maps;

    public void maps() {
        data = new HashMap<>();
        data.put("borrow id", borrowBook.getId());
        data.put("email", borrowBook.getUser().getEmail());
        data.put("title", borrowBook.getBook().getTitle());
        data.put("borrow started at", borrowBook.getCreatedAt());

        if (borrowBook.getUpdatedAt() == null) {
            data.put("return at", "not return yet");
        } else {
            data.put("return at", borrowBook.getUpdatedAt());
        }

    }

    public void list() {
        maps = new ArrayList<Map<Object, Object>>();
        for (int i = 0; i < borrowBooks.size(); i++) {
            borrowBook = borrowBooks.get(i);
            maps();
            maps.add(data);
        }
    }

    @Override
    public ResponseData<Object> borrowBook(BorrowBookDto request) throws CustomNotFoundException, Exception {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());

        if (userOpt.isEmpty() && bookOpt.isEmpty()) {
            throw new CustomNotFoundException("User and book not found. Please input correct data!");
        }

        userValidator.validateUserNotFound(userOpt);
        user = userOpt.get();
        userValidator.validateUserInactive(user);

        bookValidator.validateBookNotFound(bookOpt);
        book = bookOpt.get();
        bookValidator.validateBookInactive(book);

        Optional<BorrowBook> borrowBookOpt = borrowBookRepository.findByBookAndUserAndUpdatedAt(book, user, null);
        borrowValidator.validateBookBorrowedByUser(borrowBookOpt);
        Optional<BorrowBook> borrowBookOpt1 = borrowBookRepository.findIdByBookAndUpdatedAt(book, null);
        borrowValidator.validateBookBorrowed(borrowBookOpt1);

        borrowBook = new BorrowBook(book, user);
        borrowBook.setCreatedAt(LocalDateTime.now());
        book.setStatus("borrowed");
        bookRepository.save(book);
        borrowBookRepository.save(borrowBook);
        maps();
        responseData = new ResponseData<Object>(HttpStatus.CREATED.value(), "Success borrow", data);

        return responseData;
    }

    @Override
    public ResponseData<Object> returnBook(BorrowBookDto request) throws CustomNotFoundException, Exception {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());

        if (userOpt.isEmpty() && bookOpt.isEmpty()) {
            throw new CustomNotFoundException("User and book not found. Please input correct data!");
        }

        userValidator.validateUserNotFound(userOpt);
        user = userOpt.get();
        userValidator.validateUserInactive(user);

        bookValidator.validateBookNotFound(bookOpt);
        book = bookOpt.get();
        bookValidator.validateBookInactive(book);

        Optional<BorrowBook> borrowBookOpt = borrowBookRepository.findByBookAndUserAndUpdatedAt(book, user, null);
        borrowValidator.validateBookBorrowedByUser2(borrowBookOpt);

        borrowBook = borrowBookOpt.get();
        borrowBook.setUpdatedAt(LocalDateTime.now());
        book.setStatus("available");
        bookRepository.save(book);
        borrowBookRepository.save(borrowBook);
        maps();
        responseData = new ResponseData<Object>(HttpStatus.OK.value(),
                "Thank you for returning the borrowed book!", data);

        return responseData;
    }

    @Override
    public ResponseData<Object> updateBorrowBook(BorrowBookDto request) throws CustomNotFoundException, Exception {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());

        if (userOpt.isEmpty() && bookOpt.isEmpty()) {
            throw new CustomNotFoundException("User and book not found. Please input correct data!");
        }

        userValidator.validateUserNotFound(userOpt);
        user = userOpt.get();
        userValidator.validateUserInactive(user);

        bookValidator.validateBookNotFound(bookOpt);
        book = bookOpt.get();
        bookValidator.validateBookInactive(book);

        Optional<BorrowBook> borrowBookOpt = borrowBookRepository.findIdByBookAndUpdatedAt(book, null);
        Optional<BorrowBook> borrowBookOpt1 = borrowBookRepository.findIdByUserAndUpdatedAt(user, null);

        borrowValidator.validateBookBorrowedForUpdate(borrowBookOpt, user);

        if (borrowBookOpt1.isPresent()) {
            borrowBook = borrowBookOpt1.get();
            borrowBook.getBook().setStatus("available");
            borrowBook.setBook(book);
            borrowBook.setCreatedAt(LocalDateTime.now());
            book.setStatus("borrowed");
            bookRepository.save(book);
            borrowBookRepository.save(borrowBook);

            maps();
            responseData = new ResponseData<Object>(HttpStatus.OK.value(),
                    "Successfully edit data!", data);
        }

        if (borrowBookOpt1.isEmpty()) {
            borrowBook = new BorrowBook(book, user);
            borrowBook.setCreatedAt(LocalDateTime.now());
            book.setStatus("borrowed");
            bookRepository.save(book);
            borrowBookRepository.save(borrowBook);
            maps();
            responseData = new ResponseData<Object>(HttpStatus.CREATED.value(), "Success borrow", data);
        }

        return responseData;
    }

    @Override
    public ResponseData<Object> getBorrow(Boolean status) throws CustomNotFoundException, Exception {
        if (status == null) {
            borrowBooks = borrowBookRepository.findAll();
        }

        else if (status.equals(true)) {
            borrowBooks = borrowBookRepository.findByUpdatedAtNull();
        }

        else if (status.equals(false)) {
            borrowBooks = borrowBookRepository.findByUpdatedAtNotNull();
        }

        borrowValidator.validateGetBorrow(borrowBooks);

        list();

        responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success",
                maps);

        return responseData;
    }

    @Override
    public ResponseData<Object> getBorrowByUserId(Long id) throws CustomNotFoundException, Exception {
        Optional<User> userOpt = userRepository.findById(id);

        userValidator.validateUserNotFound(userOpt);
        user = userOpt.get();
        userValidator.validateUserInactive(user);

        borrowBooks = borrowBookRepository.findByUser(user);

        borrowValidator.validateGetBorrow(borrowBooks);

        list();

        responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success",
                maps);
        return responseData;
    }

    @Override
    public ResponseData<Object> getBorrowByBorrowId(Long id) throws CustomNotFoundException, Exception {
        Optional<BorrowBook> borrowBookOpt = borrowBookRepository.findById(id);

        borrowValidator.validateBorrowNotFound(borrowBookOpt);

        borrowBook = borrowBookOpt.get();

        maps();

        responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success",
                data);

        return responseData;
    }
}