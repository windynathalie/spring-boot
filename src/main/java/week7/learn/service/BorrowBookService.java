package week7.learn.service;

import week7.learn.model.dto.request.BorrowBookDto;
import week7.learn.model.dto.response.ResponseData;

public interface BorrowBookService {
    ResponseData<Object> borrowBook(BorrowBookDto request);

    ResponseData<Object> returnBook(BorrowBookDto request);

    ResponseData<Object> updateBorrowBook(BorrowBookDto request);

    ResponseData<Object> getBorrow(Boolean status);
}
