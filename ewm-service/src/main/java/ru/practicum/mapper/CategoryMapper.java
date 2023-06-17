package ru.practicum.mapper;

import ru.practicum.dto.CategoryDto;
import ru.practicum.model.Category;

public final class CategoryMapper {

    private CategoryMapper() {}

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getName()
        );
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
