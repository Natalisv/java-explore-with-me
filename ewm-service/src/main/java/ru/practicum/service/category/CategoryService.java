package ru.practicum.service.category;

import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.ConflictException;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    void deleteCategory(Long id) throws ConflictException;

    CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws ConflictException;

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);
}
