package tqs.cars.services;

import org.springframework.stereotype.Service;
import tqs.cars.data.CarRepository;
import tqs.cars.model.Car;

import java.util.Optional;
import java.util.List;

@Service
public class CarManagerService {

    final CarRepository carRepository;

    public CarManagerService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car save(Car oneCar) {
        return carRepository.save(oneCar);
    }

    public List<Car> getAllCars() {

        return carRepository.findAll();
    }

    public Optional<Car> getCarDetails(Long carId) {
        return Optional.ofNullable(carRepository.findByCarId(carId));
    }

    public Optional<List<Car>> findSimilarCar(Car car) {
        return Optional.ofNullable(carRepository.findByMaker(car.getMaker()));
    }
}
