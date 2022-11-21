package week7.learn.service;

import week7.learn.model.dto.request.BookDto;
import week7.learn.model.dto.response.ResponseData;

public interface CategoryService {
    ResponseData<Object> addCategory(BookDto request);

    ResponseData<Object> getCategory(Boolean status);

    ResponseData<Object> getCategoryById(long id);

    ResponseData<Object> updateCategory(long id, BookDto request);

    ResponseData<Object> deleteCategory(long id);
}
