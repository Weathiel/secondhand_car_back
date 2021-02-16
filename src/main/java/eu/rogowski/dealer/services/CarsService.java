package eu.rogowski.dealer.services;

import eu.rogowski.dealer.exceptions.ResourceNotFoundException;
import eu.rogowski.dealer.models.Cars;
import eu.rogowski.dealer.repositories.CarsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CarsService {
    private final CarsRepository carsRepository;

    public List<Cars> getAll() {
        return carsRepository.findAll();
    }

    public ResponseEntity create(Cars car) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String epro = dateFormat.format(car.getE_production_year());
        String spro = dateFormat.format(car.getS_production_year());

        car.setS_production_year(dateFormat.parse(spro));
        car.setE_production_year(dateFormat.parse(epro));
        carsRepository.save(car);
        return ResponseEntity.ok(car);
    }

    public ResponseEntity delete(Long id){
        carsRepository.deleteById(id);
        return ResponseEntity.ok(id);
    }

    public ResponseEntity getOne(Long id) {
       return ResponseEntity.ok().body(carsRepository.findById(id));
    }

    public ResponseEntity update(Long id, Cars car) {
        Cars cars = carsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        cars.setBrand(car.getBrand());
        cars.setModel(car.getModel());
        cars.setE_production_year(car.getE_production_year());
        cars.setS_production_year(car.getS_production_year());
        cars.setBody_type(car.getBody_type());
        cars.setEngine_power(car.getEngine_power());
        cars.setGas_type(car.getGas_type());
        Cars returnCar = carsRepository.save(cars);
        return ResponseEntity.ok(returnCar);
    }
}
