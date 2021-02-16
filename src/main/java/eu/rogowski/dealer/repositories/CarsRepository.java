package eu.rogowski.dealer.repositories;

import eu.rogowski.dealer.models.Cars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarsRepository extends JpaRepository<Cars, Long> {
    Cars save(Cars cars);
    void deleteById(Long id);
}
