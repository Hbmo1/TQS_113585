package io.github.bonigarcia;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.anyString;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductFinderServiceTest {

    @Mock
    private ISimpleHttpClient httpClient;

    @InjectMocks
    private ProductFinderService productFinderService;

    @BeforeEach
    void setUp() {
        productFinderService = new ProductFinderService(httpClient);
    }

    @Test
    void findProductDetails_WhenValidId_ShouldReturnProduct() throws IOException {
        // Given (Mock API response)
        String fakeJsonResponse = "{ \"id\": 3, \"title\": \"Mens Cotton Jacket\", \"price\": 55.99, \"description\": \"great outerwear jackets for Spring/Autumn/Winter, suitable for many occasions, such as working, hiking, camping, mountain/rock climbing, cycling, traveling or other outdoors. Good gift choice for you or your family member. A warm hearted love to Father, husband or son in this thanksgiving or Christmas Day.\", \"category\": \"men's clothing\", \"image\": \"https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg\" }";
        when(httpClient.doHttpGet("https://fakestoreapi.com/products/3")).thenReturn(fakeJsonResponse);

        // When
        Optional<Product> result = productFinderService.findProductDetails(3);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(3);
        assertThat(result.get().getTitle()).isEqualTo("Mens Cotton Jacket");
        assertThat(result.get().getPrice()).isEqualTo(55.99);
        assertThat(result.get().getDescription()).isEqualTo(
                "great outerwear jackets for Spring/Autumn/Winter, suitable for many occasions, such as working, hiking, camping, mountain/rock climbing, cycling, traveling or other outdoors. Good gift choice for you or your family member. A warm hearted love to Father, husband or son in this thanksgiving or Christmas Day.");
        assertThat(result.get().getCategory()).isEqualTo("men's clothing");
        assertThat(result.get().getImage()).isEqualTo("https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg");
    }

    @Test
    void findProductDetails_WhenInvalidId_ShouldReturnEmpty() throws IOException {

        // When
        Optional<Product> result = productFinderService.findProductDetails(300);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findProductDetails_WhenInvalidResponse_ShouldThrowIOException() {
        // Given (Mock API response)
        when(httpClient.doHttpGet("https://fakestoreapi.com/products/3")).thenReturn("invalid json");

        // Then
        assertThatThrownBy(() -> productFinderService.findProductDetails(3))
                .isInstanceOf(IOException.class);
    }

    @AfterEach
    void tearDown() {
        // Ensure that the httpClient.get() method was called once per test
        verify(httpClient, times(1)).doHttpGet(anyString());
    }
}