package io.github.bonigarcia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Optional;

public class ProductFinderServiceIT {

    private TqsBasicHttpClient httpClient;

    private ProductFinderService productFinderService;

    @BeforeEach
    void setUp() {
        httpClient = new TqsBasicHttpClient();
        productFinderService = new ProductFinderService(httpClient);
    }

    @Test
    void findProductDetails_WhenValidId_ShouldReturnProduct() throws IOException {

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

    // Unnecessary test since the response is now always valid (fom the API itself)
    @Disabled
    @Test
    void findProductDetails_WhenInvalidResponse_ShouldThrowIOException() {

        // Then
        assertThatThrownBy(() -> productFinderService.findProductDetails(3))
                .isInstanceOf(IOException.class);
    }

    // @AfterEach
    // void tearDown() {
    // // Ensure that the httpClient.get() method was called once per test withu

    // }
}