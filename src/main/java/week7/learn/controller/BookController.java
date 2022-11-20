package week7.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import week7.learn.model.dto.BookDto;
import week7.learn.model.dto.ResponseData;
import week7.learn.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    private ResponseData<Object> responseData;

    @GetMapping
    public ResponseEntity<Object> getBooks() {
        responseData = bookService.getAll();
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @PostMapping
    public ResponseEntity<Object> postBook(@RequestBody BookDto request) {
        responseData = bookService.createBook(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id) {
        responseData = bookService.getById(id);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable long id, @RequestBody BookDto request) {
        responseData = bookService.updateBook(id, request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable long id) {
        responseData = bookService.deleteBook(id);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping("/category")
    public ResponseEntity<Object> getCategory(@RequestBody BookDto request) {
        responseData = bookService.getByCategory(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
}