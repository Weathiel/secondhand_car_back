package eu.rogowski.dealer.controllers;

import eu.rogowski.dealer.dto.OrderFilter;
import eu.rogowski.dealer.dto.OrdersDTO;
import eu.rogowski.dealer.models.Orders;
import eu.rogowski.dealer.services.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrdersController {
    @Autowired
    private final OrdersService ordersService;

    @GetMapping(params = {"page", "size", "sortBy", "sortOrder"})
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public List<Orders> findAll(@RequestParam Integer page,
                                @RequestParam Integer size,
                                @RequestParam("sortBy") String sortBy, @RequestParam("sortOrder") String sortOrder) {
        return ordersService.findAll(page, size, sortBy, sortOrder);
    }

    @GetMapping("/length")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public long ordersSize() {
        return ordersService.getSizeOfAll();
    }

    @GetMapping("/userLength")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_WORKER', 'ROLE_ADMIN')")
    public Long userOrdersSize(HttpServletRequest httpServletRequest) {
        return ordersService.getSizeOfAllUserOrders(httpServletRequest);
    }

    @GetMapping(params = "offerId")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public List<Orders> findAllOffersWith(@RequestParam Long offerId) {
        return ordersService.findAllOrdersByOfferId(offerId);
    }

    @GetMapping(value = "/token", params = {"page", "size", "sortBy", "sortOrder"})
    @PreAuthorize("hasAnyRole('ROLE_USER' ,'ROLE_WORKER', 'ROLE_ADMIN')")
    public List<Orders> findAllByToken(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("sortBy") String sortBy, @RequestParam("sortOrder") String sortOrder, HttpServletRequest httpServletRequest) {
        return ordersService.findAllOrdersByToken(page, size, sortBy, sortOrder, httpServletRequest);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ROLE_USER' ,'ROLE_WORKER', 'ROLE_ADMIN')")
    public Orders newOrder(@RequestBody OrdersDTO ordersDTO) {
        return ordersService.newOrder(ordersDTO);
    }

    @PostMapping("/filter")
    @PreAuthorize("hasAnyRole('ROLE_USER' ,'ROLE_WORKER', 'ROLE_ADMIN')")
    public List<Orders> filter(@RequestBody OrderFilter filter, HttpServletRequest httpServletRequest) {
        return ordersService.filter(filter, httpServletRequest);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public void updateOrder(@RequestBody OrdersDTO ordersDTO,
                            @PathVariable Long id) {
        ordersService.updateOrder(ordersDTO, id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER' ,'ROLE_WORKER', 'ROLE_ADMIN')")
    public ResponseEntity getOne(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return ordersService.getOne(id, httpServletRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER' ,'ROLE_WORKER', 'ROLE_ADMIN')")
    public void deleteOrder(@PathVariable Long id) {
        ordersService.delete(id);
    }
}
