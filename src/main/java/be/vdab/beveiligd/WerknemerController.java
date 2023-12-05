package be.vdab.beveiligd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("werknemers")
public class WerknemerController {
    @GetMapping("aantal")
    int aantalWerknemers(){
        return 3;
    }
}
