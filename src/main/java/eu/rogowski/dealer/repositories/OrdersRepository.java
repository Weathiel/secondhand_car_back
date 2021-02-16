package eu.rogowski.dealer.repositories;

import eu.rogowski.dealer.models.Offers;
import eu.rogowski.dealer.models.Orders;
import eu.rogowski.dealer.models.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByOffers(Offers offers);
    List<Orders> findAllByUser(User user, Pageable pageable);

    @Query("SELECT new Orders(o.orderId, o.discount, o.createdDate, " +
            "p.offerId, p.mileage, p.price, p.color, p.prod_country, p.englishCar, p.image, p.vin, p.archivized, p.production_date, " +
            "car.carId, car.brand, car.model, car.body_type, car.gas_type, car.s_production_year, car.e_production_year, car.engine_power, " +
            "c.deposit, c.done, u.username) " +
            "FROM Orders o JOIN o.user u JOIN o.offers p JOIN o.contract c  JOIN p.cars car WHERE " +
            "u.username LIKE %:username% AND p.vin LIKE %:vin% AND o.discount <= :minDiscount AND o.discount >= :maxDiscount AND p.price <= :minValue AND p.price >= :maxValue AND c.done = :done")
            List<Orders> filterOrders(@Param("username") String username, @Param("vin") String vin, @Param("maxDiscount") Float maxDiscount, @Param("minDiscount") Float minDiscount, @Param("minValue") Float minValue, @Param("maxValue") Float maxValue, @Param("done") boolean done);


    @Query("SELECT new Orders(o.orderId, o.discount, o.createdDate, " +
            "p.offerId, p.mileage, p.price, p.color, p.prod_country, p.englishCar, p.image, p.vin, p.archivized, p.production_date, " +
            "car.carId, car.brand, car.model, car.body_type, car.gas_type, car.s_production_year, car.e_production_year, car.engine_power, " +
            "c.deposit, c.done) " +
            "FROM Orders o JOIN o.user u JOIN o.offers p JOIN  o.contract c  JOIN p.cars car WHERE " +
            "o.orderId = :orderId AND u.userId = :userId")
    Orders findOrderByIdForUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);
}
