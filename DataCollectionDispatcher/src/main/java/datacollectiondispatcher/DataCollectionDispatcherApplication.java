package datacollectiondispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DataCollectionDispatcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataCollectionDispatcherApplication.class, args);
    }
}
