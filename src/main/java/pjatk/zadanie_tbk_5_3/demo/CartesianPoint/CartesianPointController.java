package pjatk.zadanie_tbk_5_3.demo.CartesianPoint;

import lombok.*;
import org.apache.commons.collections4.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartesianPointController {
    private final CartesianPointRepository cartesianPointRepository;

    @GetMapping("/points")
    public ResponseEntity<List<CartesianPoint>> getAllPoints() {
        try {
            if (CollectionUtils.isNotEmpty(cartesianPointRepository.findAll())) {
                return new ResponseEntity<>(cartesianPointRepository.findAll().stream().toList(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/points/{n}")
    public ResponseEntity<List<CartesianPoint>> getNPoints(@PathVariable("n") final int n) {
        try {
            if (n <= 0) {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Pageable paging = PageRequest.of(0, n);
            List<CartesianPoint> points;
            Page<CartesianPoint> pagePoints = cartesianPointRepository.findAll(paging);;
            points = pagePoints.getContent();

            if (points.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(points.stream().toList(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/points/distance/{id1}/{id2}")
    public ResponseEntity<Double> getDistanceBetweenPoints(@PathVariable("id1") final long id1, @PathVariable("id2") final long id2) {

        if (id1 <= 0 || id2 <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (cartesianPointRepository.existsById(id1) && cartesianPointRepository.existsById(id2)) {
            CartesianPoint point1 = cartesianPointRepository.findById(id1).orElse(null);
            CartesianPoint point2 = cartesianPointRepository.findById(id2).orElse(null);

            double distance = sqrt((point2.getY() - point1.getY()) * (point2.getY() - point1.getY())
                    + (point2.getX() - point1.getX()) * (point2.getX() - point1.getX()));
            return new ResponseEntity<>(distance, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/points")
    public ResponseEntity<CartesianPoint> createPoint(@RequestBody CartesianPoint cartesianPoint) {
        try {
            cartesianPointRepository.save(new CartesianPoint(cartesianPoint.getX(), cartesianPoint.getY()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/points/{id}")
    public ResponseEntity<HttpStatus> deletePoint(@PathVariable("id") final long id) {
        try {
            if (!cartesianPointRepository.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            cartesianPointRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
