package pjatk.zadanie_tbk_5_3.demo;

import org.apache.commons.collections4.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pjatk.zadanie_tbk_5_3.demo.CartesianPoint.*;

import java.util.*;

@RestController
@RequestMapping("/")
public class WelcomeController {
    @GetMapping("")
    public ResponseEntity<List<CartesianPoint>> welcome() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
