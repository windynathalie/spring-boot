package week7.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import week7.learn.model.dto.request.BorrowBookDto;
import week7.learn.model.dto.response.ResponseData;
import week7.learn.service.BorrowBookService;

@RestController
@RequestMapping
public class BorrowBookController {

    @Autowired
    private BorrowBookService borrowBookService;

    private ResponseData<Object> responseData;

    @PostMapping("/borrow")
    public ResponseEntity<Object> borrowBook(@RequestBody BorrowBookDto request) {
        responseData = borrowBookService.borrowBook(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @PostMapping("/return")
    public ResponseEntity<Object> returnBook(@RequestBody BorrowBookDto request) {
        responseData = borrowBookService.returnBook(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @PutMapping("/borrow/edit")
    public ResponseEntity<Object> editBorrow(@RequestBody BorrowBookDto request) {
        responseData = borrowBookService.updateBorrowBook(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping("/borrow")
    public ResponseEntity<Object> getBorrow(@RequestParam(value = "status", defaultValue = "") Boolean status) {
        responseData = borrowBookService.getBorrow(status);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }
}
