package week7.learn.service;

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
import week7.learn.model.entity.Category;
import week7.learn.repository.BookRepository;
import week7.learn.repository.CategoryRepository;

@Service
@Transactional
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    private Category category;
    private ResponseData<Object> responseData;
    private List<Category> categories;

    private Map<Object, Object> data;

    private List<Book> books;

    private Book book;

    @Override
    public ResponseData<Object> addCategory(BookDto request) {
        Optional<Category> categoryOpt = categoryRepository.findByName(request.getCategoryName());
        if (categoryOpt.isPresent()) {
            responseData = new ResponseData<Object>(HttpStatus.BAD_REQUEST.value(),
                    "Category is found! Please enter other category...",
                    null);
        } else {
            category = new Category();
            category.setName(request.getCategoryName());
            categoryRepository.save(category);

            data = new HashMap<>();
            data.put("category: ", category.getName());
            responseData = new ResponseData<Object>(HttpStatus.CREATED.value(), "Add category success",
                    data);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> getCategory(Boolean status) {
        if (status == null) {
            categories = categoryRepository.findAll();
        } else {
            categories = categoryRepository.findByIsDeleted(status);
        }

        if (categories == null || categories.isEmpty()) {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "empty data", null);
        } else {
            responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success", categories);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> getCategoryById(long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            category = categoryOpt.get();
            data = new HashMap<>();
            data.put("category: ", category.getName());
            responseData = new ResponseData<Object>(HttpStatus.OK.value(), "success", data);
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "empty data", null);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> updateCategory(long id, BookDto request) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            category = categoryOpt.get();
            category.setName(request.getCategoryName());
            categoryRepository.save(category);
            data = new HashMap<>();
            data.put("category: ", category.getName());
            responseData = new ResponseData<Object>(HttpStatus.OK.value(), "Update success", data);
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "category not found", null);
        }
        return responseData;
    }

    @Override
    public ResponseData<Object> deleteCategory(long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            category = categoryOpt.get();
            books = bookRepository.findAllByCategory(category);
            for (int i = 0; i < books.size(); i++) {
                book = books.get(i);
                book.setCategory(null);
                bookRepository.save(book);
            }
            category.setDeleted(true);
            categoryRepository.save(category);
            responseData = new ResponseData<Object>(HttpStatus.OK.value(), "Delete success", null);
        } else {
            responseData = new ResponseData<Object>(HttpStatus.NOT_FOUND.value(), "Empty data", null);
        }
        return responseData;
    }

}
