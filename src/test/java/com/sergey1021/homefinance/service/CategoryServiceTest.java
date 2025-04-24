package com.sergey1021.homefinance.service;

import com.sergey1021.homefinance.dto.CategoryDTO;
import com.sergey1021.homefinance.entity.Category;
import com.sergey1021.homefinance.enums.TransactionType;
import com.sergey1021.homefinance.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;
    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setId(1L);
        category.setName("Food");
        category.setType(TransactionType.EXPENSE);

        categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Food");
        categoryDTO.setType(TransactionType.EXPENSE);
    }

    @Test
    void getAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        var result = categoryService.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getName());
    }

    @Test
    void getCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDTO reuslt = categoryService.getCategoryById(1L);

        assertNotNull(reuslt);
        assertEquals("Food", reuslt.getName());
    }

    @Test
    void createCategory() {
        // Мокаем сохранение категории
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Проверяем создание категории
        CategoryDTO result = categoryService.createCategory(categoryDTO);

        assertNotNull(result);
        assertEquals("Food", result.getName());
    }

    @Test
    void updateCategory() {
        // Мокаем нахождение категории по ID
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Проверяем обновление
        CategoryDTO result = categoryService.updateCategory(1L, categoryDTO);

        assertNotNull(result);
        assertEquals("Food", result.getName());
    }

    @Test
    void deleteCategory() {
        // Мокаем удаление
        doNothing().when(categoryRepository).deleteById(1L);

        // Проверяем удаление
        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void findEntityById() {
        // Мокаем нахождение категории
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Проверяем метод
        Category result = categoryService.findEntityById(1L);

        assertNotNull(result);
        assertEquals("Food", result.getName());
    }

    @Test
    void findEntityByIdNotFound() {
        // Мокаем, чтобы категория не была найдена
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Проверяем выброс исключения
        assertThrows(RuntimeException.class, () -> categoryService.findEntityById(1L));
    }
}
