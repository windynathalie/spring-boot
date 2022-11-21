package week7.learn.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import week7.learn.model.dto.request.BorrowBookDto;
import week7.learn.model.dto.response.ResponseData;
import week7.learn.model.entity.Book;
import week7.learn.model.entity.BorrowBook;
import week7.learn.model.entity.User;
import week7.learn.repository.BookRepository;
import week7.learn.repository.BorrowBookRepository;
import week7.learn.repository.UserRepository;

@Service
@Transactional
public class BorrowBookServiceImpl implements BorrowBookService {
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

    private List<BorrowBook> borrowBooks;
    List<Map<Object, Object>> maps;

    @Override
    public ResponseData<Object> borrowBook(BorrowBookDto request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());
        if (userOpt.isPresent() && bookOpt.isPresent()) {
            user = userOpt.get();
            book = bookOpt.get();
            Optional<BorrowBook> borrowBookOpt = borrowBookRepository.findByBookAndUserAndUpdatedAt(book, user, null);
            if (borrowBookOpt.isPresent()) {
                borrowBook = borrowBookOpt.get();
                data = new HashMap<>();
                data.put("email: ", borrowBook.getUser().getEmail());
                data.put("title: ", borrowBook.getBook().getTitle());
                responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                        "You already borrow this book!", data);
            } else {
                Optional<BorrowBook> borrowBookOpt1 = borrowBookRepository.findIdByBookAndUpdatedAt(book, null);
                if (borrowBookOpt1.isPresent()) {
                    borrowBook = borrowBookOpt1.get();
                    data = new HashMap<>();
                    data.put("email: ", borrowBook.getUser().getEmail());
                    data.put("title: ", borrowBook.getBook().getTitle());
                    responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                            "Book cannot be borrowed because book has been borrowed by someone!", data);
                } else {
                    if (book.getIsDeleted().equals(false)) {
                        borrowBook = new BorrowBook(book, user);
                        borrowBook.setCreatedAt(LocalDateTime.now());
                        book.setStatus("borrowed");
                        bookRepository.save(book);
                        borrowBookRepository.save(borrowBook);
                        data = new HashMap<>();
                        data.put("email: ", borrowBook.getUser().getEmail());
                        data.put("title: ", borrowBook.getBook().getTitle());
                        responseData = new ResponseData<Object>(HttpStatus.CREATED.value(), "Success borrow", data);
                    } else {
                        responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                                "Book cannot be borrowed because book has been deleted!", null);
                    }
                }
            }
        } else if (userOpt.isPresent() && bookOpt.isEmpty()) {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(),
                    "Book not found. Please input another book", null);
        } else if (userOpt.isEmpty() && bookOpt.isPresent()) {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(),
                    "User not found. Please input correct user", null);
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "Wrong book and user input!", null);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> returnBook(BorrowBookDto request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());
        if (userOpt.isPresent() && bookOpt.isPresent()) {
            user = userOpt.get();
            book = bookOpt.get();
            Optional<BorrowBook> borrowBookOpt = borrowBookRepository.findByBookAndUserAndUpdatedAt(book, user, null);
            if (borrowBookOpt.isPresent()) {
                borrowBook = borrowBookOpt.get();
                borrowBook.setUpdatedAt(LocalDateTime.now());
                book.setStatus("available");
                bookRepository.save(book);
                borrowBookRepository.save(borrowBook);
                data = new HashMap<>();
                data.put("email: ", borrowBook.getUser().getEmail());
                data.put("title: ", borrowBook.getBook().getTitle());
                responseData = new ResponseData<Object>(HttpStatus.OK.value(),
                        "Thank you for returning the borrowed book!", data);
            } else {
                responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                        "User has not borrowed the book!", null);
            }
        } else if (userOpt.isPresent() && bookOpt.isEmpty()) {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(),
                    "Book not found. Please input another book", null);
        } else if (userOpt.isEmpty() && bookOpt.isPresent()) {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(),
                    "User not found. Please input correct user", null);
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "Wrong book and user input!", null);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> updateBorrowBook(BorrowBookDto request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());
        if (userOpt.isPresent() && bookOpt.isPresent()) {
            user = userOpt.get();
            book = bookOpt.get();
            Optional<BorrowBook> borrowBookOpt = borrowBookRepository.findIdByBookAndUpdatedAt(book, null);
            Optional<BorrowBook> borrowBookOpt1 = borrowBookRepository.findIdByUserAndUpdatedAt(user, null);
            if (borrowBookOpt.isPresent()) {
                borrowBook = borrowBookOpt.get();
                if (borrowBook.getUser() == user) {
                    data = new HashMap<>();
                    data.put("email: ", borrowBook.getUser().getEmail());
                    data.put("title: ", borrowBook.getBook().getTitle());
                    responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                            "No edit changes!", data);
                } else {
                    responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                            "Book cannot be borrowed because book has been borrowed by someone!", null);
                }
            } else {
                if (borrowBookOpt1.isPresent()) {
                    if (book.getIsDeleted().equals(false)) {
                        borrowBook = borrowBookOpt1.get();
                        borrowBook.getBook().setStatus("available");
                        borrowBook.setBook(book);
                        borrowBook.setCreatedAt(LocalDateTime.now());
                        book.setStatus("borrowed");
                        bookRepository.save(book);
                        borrowBookRepository.save(borrowBook);

                        data = new HashMap<>();
                        data.put("email: ", borrowBook.getUser().getEmail());
                        data.put("title: ", borrowBook.getBook().getTitle());
                        responseData = new ResponseData<Object>(HttpStatus.OK.value(),
                                "Successfully edit data!", data);
                    } else {
                        responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                                "Book cannot be borrowed because book has been deleted!", null);
                    }
                } else {
                    if (book.getIsDeleted().equals(false)) {
                        borrowBook = new BorrowBook(book, user);
                        borrowBook.setCreatedAt(LocalDateTime.now());
                        book.setStatus("borrowed");
                        bookRepository.save(book);
                        borrowBookRepository.save(borrowBook);
                        data = new HashMap<>();
                        data.put("email: ", borrowBook.getUser().getEmail());
                        data.put("title: ", borrowBook.getBook().getTitle());
                        responseData = new ResponseData<Object>(HttpStatus.CREATED.value(), "Success borrow", data);
                    } else {
                        responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                                "Book cannot be borrowed because book has been deleted!", null);
                    }
                }
            }
        } else if (userOpt.isPresent() && bookOpt.isEmpty()) {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(),
                    "Book not found. Please input another book", null);
        } else if (userOpt.isEmpty() && bookOpt.isPresent()) {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(),
                    "User not found. Please input correct user", null);
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "Wrong book and user input!", null);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> getBorrow(Boolean status) {
        if (status == null) {
            borrowBooks = borrowBookRepository.findAll();
        } else if (status == true) {
            borrowBooks = borrowBookRepository.findByUpdatedAtNull();
        } else if (status == false) {
            borrowBooks = borrowBookRepository.findByUpdatedAtNotNull();
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "can't proceed your request!", null);
        }

        if (borrowBooks == null || borrowBooks.isEmpty()) {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "empty data", null);
        } else {
            maps = new ArrayList<Map<Object, Object>>();

            for (int i = 0; i < borrowBooks.size(); i++) {
                borrowBook = borrowBooks.get(i);
                data = new HashMap<>();
                data.put("email: ", borrowBook.getUser().getEmail());
                data.put("title: ", borrowBook.getBook().getTitle());
                data.put("borrow started at: ", borrowBook.getCreatedAt());

                if (borrowBook.getUpdatedAt() == null) {
                    data.put("return at: ", "not return yet");
                } else {
                    data.put("return at: ", borrowBook.getUpdatedAt());
                }

                maps.add(data);
            }
            responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success",
                    maps);
        }

        return responseData;
    }
}