package tqs.cars.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.cars.model.Car;
import tqs.cars.data.CarRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    @DisplayName("🧪 [Integration] When find car by valid id, then return car")
    void whenFindCarByValidId_thenReturnCar() {
        // arrange a new car and insert into db
        Car car = new Car("Tesla", "Model S");
        entityManager.persistAndFlush(car); //ensure data is persisted at this point

        // test the query method
        Car found = carRepository.findById(car.getCarId()).orElse(null);
        assertThat(found).isEqualTo(car);
    }

    @Test
    @DisplayName("🧪 [Integration] When create car, then return car")
    void whenCreateCar_thenReturnCar() {

        // arrange a new car and insert into db
        Car persistedCar = entityManager.persistFlushFind( new Car("Tesla", "Model S")); //ensure data is persisted at this point

        // test the query method of interest
        Car found = carRepository.findById(persistedCar.getCarId()).orElse(null);
        assertThat(found).isNotNull().
                extracting(Car::getMaker).isEqualTo(persistedCar.getMaker());
    }

}
