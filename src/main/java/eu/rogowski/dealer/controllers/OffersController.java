package eu.rogowski.dealer.controllers;

import eu.rogowski.dealer.dto.OfferFilter;
import eu.rogowski.dealer.models.Offers;
import eu.rogowski.dealer.dto.OffersDTO;
import eu.rogowski.dealer.services.OffersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/offers")
public class OffersController {

    @Autowired
    private final OffersService offersService;


    @GetMapping(params = {"page", "size"})
    public List<Offers> findAll(@RequestParam Integer page,
                                @RequestParam Integer size) {
        return offersService.findAll(page, size).toList();
    }

    @GetMapping("/lenght")
    public Long lenght(){
        return offersService.lenght();
    }

    @GetMapping("/{id}")
    public Offers getOne(@PathVariable Long id){
        return offersService.getOne(id);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public Offers newOffer(@RequestBody OffersDTO offersDTO) {
        return offersService.newOffer(offersDTO);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public Offers setImage(@PathVariable Long id, @RequestBody String image) {
        return offersService.updateImage(id, image);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public void carSold(@PathVariable Long id,
            @RequestBody OffersDTO offersDTO) {
        offersService.update(offersDTO, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_WORKER', 'ROLE_ADMIN')")
    public ResponseEntity deleteCar(@PathVariable Long id){
        offersService.deleteOffer(id);
        return ResponseEntity.ok("Deleted");
    }

    @PostMapping("/findOffer")
    public ResponseEntity findOffer(@RequestBody OfferFilter offersFilter){
        return offersService.findOffer(offersFilter);
    }

    @GetMapping("/queue/{vin}")
    public ResponseEntity getQueueForOffer(@PathVariable String vin){
        return offersService.getQueueForOffer(vin);
    }

}
