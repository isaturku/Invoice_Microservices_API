package datacollectiondispatcher.invoice.repository;

import datacollectiondispatcher.invoice.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// kjo klase ben te mundur lidhjen me database, specifikisht me tabelen "Station".
// te file "resources/application.properties" behete konfigurimi i lidhjes me database
@Repository // annotation qe trg se kjo  klase funksionon si lidhje me nje tabele te nje database-i
public interface StationRepository extends JpaRepository<Station,Integer> {

    List<Station> findAllByAvailable(boolean available);// metode qe ben te mundur filtrimin e stacioneve me ane te field "available
}
