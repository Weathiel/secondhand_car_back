package eu.rogowski.dealer.services;

import eu.rogowski.dealer.configuration.jwt.AuthTokenFilter;
import eu.rogowski.dealer.configuration.jwt.JwtUtils;
import eu.rogowski.dealer.dto.OrderFilter;
import eu.rogowski.dealer.exceptions.ResourceNotFoundException;
import eu.rogowski.dealer.models.Contract;
import eu.rogowski.dealer.models.Orders;
import eu.rogowski.dealer.dto.OrdersDTO;
import eu.rogowski.dealer.models.Role;
import eu.rogowski.dealer.models.User;
import eu.rogowski.dealer.payload.ResponseJSON;
import eu.rogowski.dealer.repositories.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final OffersRepository offersRepository;
    private final ContractRepository contractRepository;
    @Autowired
    private final JwtUtils jwtUtils;
    @Autowired
    private final AuthTokenFilter authTokenFilter;

    public List<Orders> findAll(Integer page, Integer size, String sortBy, String sortOrder) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        return ordersRepository.findAll(pageable).toList();
    }

    public List<Orders> findAllOrdersByOfferId(Long offerId) {
        try {
            return ordersRepository.findAllByOffers(offersRepository.findById(offerId).orElseThrow(() -> new ResourceNotFoundException()));
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    public List<Orders> findAllOrdersByToken(Integer page, Integer size, String sortBy, String sortOrder, HttpServletRequest httpServletRequest) {
        try {
            String token = authTokenFilter.parseJwt(httpServletRequest);
            if(token != null && jwtUtils.validateJwtToken(token)){
                Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
                return ordersRepository.findAllByUser(userRepository.findByUsername(jwtUtils.getUsernameByToken(token)).orElseThrow(() -> new ResourceNotFoundException()), pageable);
            }
        } catch (ResourceNotFoundException e) {
            return null;
        }
        return null;
    }

    public Orders newOrder(OrdersDTO ordersDTO) {
        Orders orders = new Orders();
        Contract contract = new Contract(ordersDTO.getContract().getDeposit(), ordersDTO.getContract().isDone());

        orders.setUser(userRepository.findByUsername(ordersDTO.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Didn't found user with id: " + ordersDTO.getUserId())));

        orders.setOffers(offersRepository.findById(ordersDTO.getOfferId())
                .orElseThrow(() -> new ResourceNotFoundException("Didn't found offer with id: " + ordersDTO.getOfferId())));

        orders.setContract(contractRepository.save(contract));

        orders.setDiscount(ordersDTO.getDiscount());

        return ordersRepository.save(orders);
    }

    public void updateOrder(OrdersDTO ordersDTO, Long id) {
        Orders orders = ordersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Didn't found order with id: " + id));
        orders.setDiscount(ordersDTO.getDiscount());
        Contract contract = contractRepository.getOne(ordersDTO.getContract().getContractId());
        contract.setDeposit(ordersDTO.getContract().getDeposit());
        contract.setDone(ordersDTO.getContract().isDone());
        ordersRepository.save(orders);
    }

    public void delete(Long id){
        ordersRepository.delete(ordersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Didn't found order to delete!")));
    }

    public List<Orders> filter(OrderFilter orderFilter, HttpServletRequest httpServletRequest){
        String token = authTokenFilter.parseJwt(httpServletRequest);
        if(token != null && jwtUtils.validateJwtToken(token)) {
            User user = userRepository.findByUsername(jwtUtils.getUsernameByToken(token)).orElseThrow(() -> new ResourceNotFoundException());
            for(Role role: user.getRole()){
                if(role.getRoleId() > 1){
                    System.out.println(orderFilter.getUsername());
                    return ordersRepository.filterOrders(orderFilter.getUsername(), orderFilter.getVin(), orderFilter.getMaxDiscount(), orderFilter.getMinDiscount(), orderFilter.getMinValue(), orderFilter.getMaxValue(), orderFilter.getDone());
                }
            }
            return ordersRepository.filterOrders(user.getUsername(), orderFilter.getVin(), orderFilter.getMaxDiscount(), orderFilter.getMinDiscount(), orderFilter.getMinValue(), orderFilter.getMaxValue(), orderFilter.getDone());
        }
       return null;
    }


    public ResponseEntity getOne(Long id, HttpServletRequest httpServletRequest) {
        Orders orders = null;
        try {
            String token = authTokenFilter.parseJwt(httpServletRequest);
            if(token != null && jwtUtils.validateJwtToken(token)){
                User user = userRepository.findByUsername(
                        jwtUtils.getUsernameByToken(token)).orElseThrow(() -> new ResourceNotFoundException());
                boolean isUser = true;
                for(Role role: user.getRole()){
                    if(role.getRoleId() > 1)
                        isUser = false;

                }
                if(isUser)
                    orders = ordersRepository.findOrderByIdForUserId(id, user.getUserId());
                else
                    orders = ordersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ResponseJSON("Object not found", 404));
        }
        if(orders == null)
            return ResponseEntity.badRequest().body(new ResponseJSON("Object not found", 404));
        return ResponseEntity.ok().body(orders);
    }

    public long getSizeOfAll(){
        return ordersRepository.count();
    }

    public long getSizeOfAllUserOrders(HttpServletRequest httpServletRequest) {
        try {
            String token = authTokenFilter.parseJwt(httpServletRequest);
            if(token != null && jwtUtils.validateJwtToken(token)){
                return ordersRepository.findAllByUser(userRepository.findByUsername(jwtUtils.getUsernameByToken(token)).orElseThrow(() -> new ResourceNotFoundException()), Pageable.unpaged()).size();
            }
        } catch (ResourceNotFoundException e) {
            return -1;
        }
        return -1;
    }
}
