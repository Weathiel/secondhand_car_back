package eu.rogowski.dealer.repositories;

import eu.rogowski.dealer.models.Offers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OffersRepository extends JpaRepository<Offers, Long> {
    void deleteById(Long id);
    Offers findByOfferId(Long id);

    @Query(value = "SELECT new Offers(o.offerId, o.mileage, o.price, o.color, o.prod_country, o.englishCar, o.image, o.vin, o.archivized, o.production_date," +
            "                  c.carId, c.brand, c.model, c.body_type, c.gas_type, c.s_production_year, c.e_production_year, c.engine_power) " +
            " FROM Offers o JOIN o.cars c WHERE " +
            " c.body_type LIKE %:bodyType% AND c.brand LIKE %:brand% AND c.model LIKE %:model% AND c.gas_type LIKE %:gasType% " +
            " AND o.mileage >= :minMilleage AND o.mileage <= :maxMileage " +
            " AND o.price >= :minPrice AND o.price <= :maxPrice AND o.vin LIKE %:vin%" +
            " AND o.englishCar = :englishCar AND o.prod_country LIKE %:prod_country% AND o.production_date BETWEEN :date AND :maxDate "
    )
    List<Offers> filterOffers(
            @Param("bodyType") String bodyType, @Param("brand") String brand, @Param("model") String model, @Param("gasType") String gasType,
            @Param("minMilleage") Float minMilleage, @Param("maxMileage") Float maxMileage, @Param("minPrice") Float minPrice, @Param("maxPrice") Float maxPrice,
            @Param("englishCar") boolean englishCar, @Param("prod_country") String prod_country, @Param("date") Date date, @Param("maxDate") Date maxDate, @Param("vin") String vin
    );
}
