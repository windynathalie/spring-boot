package week7.learn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import week7.learn.model.dto.request.BookDto;
import week7.learn.model.dto.response.ResponseData;
import week7.learn.model.entity.Book;
import week7.learn.model.entity.BorrowBook;
import week7.learn.model.entity.Category;
import week7.learn.repository.BookRepository;
import week7.learn.repository.BorrowBookRepository;
import week7.learn.repository.CategoryRepository;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BorrowBookRepository borrowBookRepository;

    private ResponseData<Object> responseData;

    private Book book;

    private List<Book> books;

    private Category category;

    private Map<Object, Object> data;

    List<Map<Object, Object>> maps;

    @Override
    public ResponseData<Object> createBook(BookDto requesDto) {
        book = new Book(requesDto.getTitle(), requesDto.getAuthor());
        Optional<Category> categoryOpt = categoryRepository.findByName(requesDto.getCategoryName());

        if (categoryOpt.isPresent()) {
            category = categoryOpt.get();
            if (category.isDeleted() == false) {
                book.setCategory(category);
                bookRepository.save(book);

                data = new HashMap<>();
                data.put("Id: ", book.getId());
                data.put("Title: ", book.getTitle());
                data.put("Author: ", book.getAuthor());
                data.put("Category: ", book.getCategory().getName());
                responseData = new ResponseData<Object>(HttpStatus.OK.value(), "add successfully", data);
            } else {
                responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                        "category has been deleted. choose other category!", data);
            }
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "Wrong category input!", null);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> getAll() {
        books = bookRepository.findAll();

        if (books.isEmpty()) {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "empty data", null);
        } else {
            maps = new ArrayList<Map<Object, Object>>();

            for (int i = 0; i < books.size(); i++) {
                book = books.get(i);
                data = new HashMap<>();
                data.put("Id: ", book.getId());
                data.put("Title: ", book.getTitle());
                data.put("Author: ", book.getAuthor());

                if (book.getCategory() == null) {
                    data.put("Category: ", book.getCategory());
                } else {
                    data.put("Category: ", book.getCategory().getName());
                }
                maps.add(data);
            }
            responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success", maps);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> getById(long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            book = bookOpt.get();

            data = new HashMap<>();
            data.put("Id: ", book.getId());
            data.put("Title: ", book.getTitle());
            data.put("Author: ", book.getAuthor());
            if (book.getCategory() == null) {
                data.put("Category: ", book.getCategory());
            } else {
                data.put("Category: ", book.getCategory().getName());
            }
            responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success", data);
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "empty data", null);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> updateBook(long id, BookDto request) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            book = bookOpt.get();

            Optional<Category> categoryOpt = categoryRepository.findByName(request.getCategoryName());

            if (categoryOpt.isPresent()) {
                category = categoryOpt.get();
                if (category.isDeleted() == false) {
                    book.setTitle(request.getTitle());
                    book.setAuthor(request.getAuthor());
                    book.setCategory(category);
                    bookRepository.save(book);

                    data = new HashMap<>();
                    data.put("Id: ", book.getId());
                    data.put("Title: ", book.getTitle());
                    data.put("Author: ", book.getAuthor());
                    data.put("Category: ", book.getCategory().getName());
                    responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success", data);
                } else {
                    responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                            "category has been deleted. choose other category!", null);
                }
            } else {
                responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "Wrong category input!", null);
            }

        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "empty data", null);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> deleteBook(long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            book = bookOpt.get();

            Optional<BorrowBook> borrowBookOpt = borrowBookRepository.findIdByBookAndUpdatedAt(book, null);
            if (borrowBookOpt.isPresent()) {
                responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                        "can't delete book because user currently borrow the book!", null);
            } else {
                book.setIsDeleted(true);
                bookRepository.save(book);
                responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success", null);
            }

        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "empty data", null);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> getByCategory(BookDto request) {
        Optional<Category> categoryOpt = categoryRepository.findByName(request.getCategoryName());

        if (categoryOpt.isPresent()) {
            category = categoryOpt.get();
            books = bookRepository.findAllByCategory(category);
            if (books.isEmpty()) {
                responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "empty data", null);
            } else {
                maps = new ArrayList<Map<Object, Object>>();
                for (int i = 0; i < books.size(); i++) {
                    book = books.get(i);
                    data = new HashMap<>();
                    data.put("Id: ", book.getId());
                    data.put("Title: ", book.getTitle());
                    data.put("Author: ", book.getAuthor());
                    maps.add(data);
                }
                responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success", maps);
            }
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "wrong category input", null);
        }
        return responseData;
    }

}
