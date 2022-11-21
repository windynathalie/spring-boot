package week7.learn.service;

import week7.learn.exception.customException.CustomNotFoundException;
import week7.learn.model.dto.request.BorrowBookDto;
import week7.learn.model.dto.response.ResponseData;

public interface BorrowBookService2 {
    ResponseData<Object> borrowBook(BorrowBookDto request) throws CustomNotFoundException, Exception;

    ResponseData<Object> returnBook(BorrowBookDto request) throws CustomNotFoundException, Exception;

    ResponseData<Object> updateBorrowBook(BorrowBookDto request) throws CustomNotFoundException, Exception;

    ResponseData<Object> getBorrow(Boolean status) throws CustomNotFoundException, Exception;

    ResponseData<Object> getBorrowByUserId(Long id) throws CustomNotFoundException, Exception;

    ResponseData<Object> getBorrowByBorrowId(Long id) throws CustomNotFoundException, Exception;
}
