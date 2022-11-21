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

import week7.learn.exception.customException.CustomNotFoundException;
import week7.learn.model.dto.request.BorrowBookDto;
import week7.learn.model.dto.response.ResponseData;
import week7.learn.service.BorrowBookService2;

@RestController
@RequestMapping
public class BorrowBookController2 {

    @Autowired
    private BorrowBookService2 borrowBookService2;

    private ResponseData<Object> responseData;

    @PostMapping("/borrow2")
    public ResponseEntity<Object> borrowBook(@RequestBody BorrowBookDto request)
            throws CustomNotFoundException, Exception {
        responseData = borrowBookService2.borrowBook(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @PostMapping("/return2")
    public ResponseEntity<Object> returnBook(@RequestBody BorrowBookDto request)
            throws CustomNotFoundException, Exception {
        responseData = borrowBookService2.returnBook(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @PutMapping("/borrow2/edit")
    public ResponseEntity<Object> editBorrow(@RequestBody BorrowBookDto request)
            throws CustomNotFoundException, Exception {
        responseData = borrowBookService2.updateBorrowBook(request);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping("/borrow2")
    public ResponseEntity<Object> getBorrow(@RequestParam(value = "status", defaultValue = "") Boolean status)
            throws CustomNotFoundException, Exception {
        responseData = borrowBookService2.getBorrow(status);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping("/borrow2/findUser")
    public ResponseEntity<Object> getBorrowByUserId(@RequestParam(value = "userId", defaultValue = "") Long userId)
            throws CustomNotFoundException, Exception {
        responseData = borrowBookService2.getBorrowByUserId(userId);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

    @GetMapping("/borrow2/findId")
    public ResponseEntity<Object> getBorrowByBorrowId(
            @RequestParam(value = "borrowId", defaultValue = "") Long borrowId)
            throws CustomNotFoundException, Exception {
        responseData = borrowBookService2.getBorrowByBorrowId(borrowId);
        return ResponseEntity.status(responseData.getStatus()).body(responseData);
    }

}
