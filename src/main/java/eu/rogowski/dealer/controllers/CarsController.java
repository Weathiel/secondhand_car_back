package eu.rogowski.dealer.controllers;

import eu.rogowski.dealer.models.Cars;
import eu.rogowski.dealer.services.CarsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/cars")
public class CarsController {

    private final CarsService carsService;

    @GetMapping("/all")
    public List<Cars> getAll(){
        return carsService.getAll();
    }

    @GetMapping("/lenght")
    public ResponseEntity<Integer> getCarsLenght(){
        return ResponseEntity.ok(this.carsService.getAll().size());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public ResponseEntity createCar(@RequestBody Cars car) throws ParseException {
        return carsService.create(car);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public ResponseEntity deleteCar(@PathVariable("id") Long id){
        return carsService.delete(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public ResponseEntity getOne(@PathVariable Long id){
        return carsService.getOne(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public ResponseEntity updateCars(@PathVariable Long id, @RequestBody Cars car){
        return carsService.update(id, car);
    }
}
