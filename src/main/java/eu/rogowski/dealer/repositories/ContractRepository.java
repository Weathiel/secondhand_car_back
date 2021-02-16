package eu.rogowski.dealer.repositories;

import eu.rogowski.dealer.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Contract getOne(Long id);
}
