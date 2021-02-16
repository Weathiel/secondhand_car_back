package eu.rogowski.dealer.services;

import eu.rogowski.dealer.dto.OfferFilter;
import eu.rogowski.dealer.exceptions.ResourceNotFoundException;
import eu.rogowski.dealer.models.Offers;
import eu.rogowski.dealer.dto.OffersDTO;
import eu.rogowski.dealer.repositories.CarsRepository;
import eu.rogowski.dealer.repositories.OffersRepository;
import eu.rogowski.dealer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OffersService {
    private final OffersRepository offersRepository;
    private final CarsRepository carsRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Page<Offers> findAll(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "offerId"));
        return offersRepository.findAll(pageable);
    }

    public Long lenght(){
        return offersRepository.count();
    }

    public Offers newOffer(OffersDTO offersDTO) {
        Offers offers = modelMapper.map(offersDTO, Offers.class);
        offers.setCars(carsRepository.findById(offersDTO.getCarId()).orElseThrow(() -> new ResourceNotFoundException("Didn't found car with id: " + offersDTO.getCarId()))
        );
        return offersRepository.save(offers);
    }

    public void update(OffersDTO offersDTO, Long id) {
        Offers offers = offersRepository.getOne(id);
        offers.setArchivized(offersDTO.getArchivized());
        offers.setPrice(offersDTO.getPrice());
        offersRepository.save(offers);
    }

    public Offers updateImage(Long id, String img){
        Offers offers = offersRepository.getOne(id);
        offers.setImage(img);
        offersRepository.save(offers);
        return offers;
    }

    public void deleteOffer(Long id){
        offersRepository.deleteById(id);
    }

    public ResponseEntity findOffer(OfferFilter offersFilter) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Offers> filteredOffers = null;
        try {
            filteredOffers = offersRepository.filterOffers(
                    offersFilter.getCar().getBody_type(), offersFilter.getCar().getBrand(), offersFilter.getCar().getModel(), offersFilter.getCar().getGas_type(),
                    (float) offersFilter.getMinMilleage(), (float)offersFilter.getMaxMilleage(), (float)offersFilter.getMinValue(), (float)offersFilter.getMaxValue(),
                    offersFilter.isEnglishCar(), offersFilter.getProd_country(), format.parse(offersFilter.getDate()), format.parse(offersFilter.getMaxDate()), offersFilter.getVin()
                    );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(filteredOffers);
    }

    public Offers getOne(Long id) {
        return offersRepository.findByOfferId(id);
    }

    public ResponseEntity getQueueForOffer(String vin) {
       long count = userRepository.countUsersForOffer(vin);
       return ResponseEntity.ok(count);
    }
}
