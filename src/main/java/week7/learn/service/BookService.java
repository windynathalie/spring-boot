package week7.learn.service;

import week7.learn.model.dto.request.BookDto;
import week7.learn.model.dto.response.ResponseData;

public interface BookService {
    ResponseData<Object> createBook(BookDto requesDto);

    ResponseData<Object> getAll();

    ResponseData<Object> getById(long id);

    ResponseData<Object> getByCategory(BookDto request);

    ResponseData<Object> updateBook(long id, BookDto request);

    ResponseData<Object> deleteBook(long id);
}
