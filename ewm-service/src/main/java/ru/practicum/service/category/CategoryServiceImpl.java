package ru.practicum.service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category savedCategory = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
        log.debug("Add category: {}", savedCategory);
        return CategoryMapper.toCategoryDto(savedCategory);
    }

    @Override
    public void deleteCategory(Long id) throws ConflictException {
        if (id != null) {
            Category category = categoryRepository.findById(id).orElseThrow(() -> {
                throw new IllegalArgumentException();
            });
            if (eventRepository.findByCategory(category.getId()).isEmpty()) {
                log.debug("Delete category: {}", category);
                categoryRepository.deleteById(id);
            } else {
                log.error("The category is not empty");
                throw new ConflictException("The category is not empty");
            }
        }
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws ConflictException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });
        try {
            category.setName(categoryDto.getName());
            log.debug("Update category: {}", category);
            categoryRepository.save(category);
            return CategoryMapper.toCategoryDto(category);
        } catch (ConstraintViolationException e) {
            log.error("SQL exception");
            throw new ConflictException("could not execute statement; " +
                    "SQL [n/a]; constraint [uq_category_name]; " +
                    "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                    "could not execute statement");
        }
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<Category> list = categoryRepository.findAll();
        if (!list.isEmpty()) {
            return list.stream()
                    .map(CategoryMapper::toCategoryDto)
                    .skip(from).limit(size).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });
        return CategoryMapper.toCategoryDto(category);
    }

}
