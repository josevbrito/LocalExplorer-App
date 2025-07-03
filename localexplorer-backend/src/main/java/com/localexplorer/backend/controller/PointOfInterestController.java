package com.localexplorer.backend.controller;

import com.localexplorer.backend.model.PointOfInterest;
import com.localexplorer.backend.model.PointOfInterestType;
import com.localexplorer.backend.service.PointOfInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points-of-interest")
@CrossOrigin(origins = {"http://localhost:8100", "http://localhost:4200"})
public class PointOfInterestController {

    @Autowired
    private PointOfInterestService pointOfInterestService;

    // Endpoint to get all points of interest
    // GET http://localhost:8080/api/points-of-interest
    @GetMapping
    public List<PointOfInterest> getAllPointsOfInterest() {
        return pointOfInterestService.findAllPoints();
    }

    // Endpoint to get a point of interest by its ID
    // GET http://localhost:8080/api/points-of-interest/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PointOfInterest> getPointOfInterestById(@PathVariable Long id) {
        return pointOfInterestService.findPointById(id)
                .map(ResponseEntity::ok) // If found, return 200 OK with the point
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }

    // Endpoint to create a new point of interest
    // POST http://localhost:8080/api/points-of-interest
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Return 201 Created for successful creation
    public PointOfInterest createPointOfInterest(@RequestBody PointOfInterest pointOfInterest) {
        return pointOfInterestService.savePoint(pointOfInterest);
    }

    // Endpoint to update an existing point of interest
    // PUT http://localhost:8080/api/points-of-interest/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PointOfInterest> updatePointOfInterest(@PathVariable Long id, @RequestBody PointOfInterest pointOfInterest) {
        return pointOfInterestService.findPointById(id)
                .map(existingPoint -> {
                    existingPoint.setName(pointOfInterest.getName());
                    existingPoint.setDescription(pointOfInterest.getDescription());
                    existingPoint.setLatitude(pointOfInterest.getLatitude());
                    existingPoint.setLongitude(pointOfInterest.getLongitude());
                    existingPoint.setType(pointOfInterest.getType()); // Assuming PointOfInterestType is an enum and the setter is available
                    return ResponseEntity.ok(pointOfInterestService.savePoint(existingPoint));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to delete a point of interest by its ID
    // DELETE http://localhost:8080/api/points-of-interest/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retturn 204 No Content for successful deletion
    public void deletePointOfInterest(@PathVariable Long id) {
        pointOfInterestService.deletePointById(id);
    }

    // --- Location-based Endpoint ---

    // Endpoint to get nearby points of interest based on latitude, longitude, and radius
    // GET http://localhost:8080/api/points-of-interest/nearby?latitude=LAT&longitude=LON&radiusKm=RAIO
    @GetMapping("/nearby")
    public List<PointOfInterest> getNearbyPointsOfInterest(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5.0") double radiusKm) { // default radius is 5 km
        return pointOfInterestService.findNearbyPoints(latitude, longitude, radiusKm);
    }
}
