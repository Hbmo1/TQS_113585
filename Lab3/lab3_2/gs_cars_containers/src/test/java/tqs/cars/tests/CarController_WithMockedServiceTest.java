package tqs.cars.tests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.cars.boundary.CarController;
import tqs.cars.services.CarManagerService;
import tqs.cars.utils.JsonUtils;
import tqs.cars.model.Car;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class CarController_WithMockedServiceTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarManagerService service;

    @Test
    void whenPostCar_thenCreateCar( ) throws Exception {
        Car car = new Car("Volkswagen", "Golf");

        when( service.save(Mockito.any()) ).thenReturn( car);

        mvc.perform(
                post("/api/cars").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(car)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.maker", is("Volkswagen")))
                .andExpect(jsonPath("$.model", is("Golf")));

        verify(service, times(1)).save(Mockito.any());

    }

    @Test
    void givenManyCars_whenGetCars_thenReturnJsonArray() throws Exception {
        Car car1 = new Car("Volkswagen", "Golf");
        Car car2 = new Car("Renault", "Clio");

        List<Car> allCars = Arrays.asList(car1, car2);

        when(service.getAllCars()).thenReturn(allCars);

        mvc.perform(get("/api/cars").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].maker", is(car1.getMaker())))
                .andExpect(jsonPath("$[0].model", is(car1.getModel())))
                .andExpect(jsonPath("$[1].maker", is(car2.getMaker())))
                .andExpect(jsonPath("$[1].model", is(car2.getModel())));

        verify(service, times(1)).getAllCars();
    }

}
