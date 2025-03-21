package tqs.cars.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import tqs.cars.model.Car;
import tqs.cars.data.CarRepository;
import tqs.cars.services.CarManagerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CarService_UnitTests {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carService;

    @BeforeEach
    public void setUp() {

        Car car1 = new Car("Tesla", "Model S");
        Car car2 = new Car("BMW", "X6");
        Car car3 = new Car("Mercedes", "C220");

        // Manually setting the IDs (since no DB is being used)
        car1.setCarId(1L);
        car2.setCarId(2L);
        car3.setCarId(3L);

        // System.out.println("car1 id: " + car1.getCarId());
        // System.out.println("car1 id: " + car2.getCarId());
        // System.out.println("car1 id: " + car3.getCarId());

        List<Car> allCars = Arrays.asList(car1, car2, car3);

        Mockito.when(carRepository.findByCarId(car1.getCarId())).thenReturn(car1);
        Mockito.when(carRepository.findByCarId(car2.getCarId())).thenReturn(car2);
        Mockito.when(carRepository.findByCarId(-99L)).thenReturn(null);
        Mockito.when(carRepository.findAll()).thenReturn(allCars);
    }

    @Test
    public void whenValidId_thenCarShouldBeFound() {
        Optional<Car> found = carService.getCarDetails(1L);
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getCarId()).isEqualTo(1L);
        assertThat(found.get().getModel()).isEqualTo("Model S");
        assertThat(found.get().getMaker()).isEqualTo("Tesla");
        verifyFindByIdIsCalledOnce();
    }

    @Test
    public void whenInValidId_thenCarShouldNotBeFound() {
        Optional<Car> fromDb = carService.getCarDetails(-99L);
        assertThat(fromDb.isPresent()).isFalse();
        verifyFindByIdIsCalledOnce();
    }

    @Test
    public void given3Cars_whengetAll_thenReturn3Records() {
        Car car1 = new Car("Tesla", "Model S");
        Car car2 = new Car("BMW", "X6");
        Car car3 = new Car("Mercedes", "C220");

        List<Car> allCars = carService.getAllCars();
        verifyFindAllCarsIsCalledOnce();
        assertThat(allCars).hasSize(3).extracting(Car::getModel).contains(car1.getModel(), car2.getModel(),
                car3.getModel());
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findByCarId(Mockito.anyLong());
        Mockito.reset(carRepository);
    }

    private void verifyFindAllCarsIsCalledOnce() {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(carRepository);
    }

}
