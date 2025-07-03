package com.localexplorer.backend.service;

import com.localexplorer.backend.model.PointOfInterest;
import com.localexplorer.backend.repository.PointOfInterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PointOfInterestService {

    // Constant for Earth's radius in kilometers, used in distance calculations
    private static final int EARTH_RADIUS_KM = 6371;

    @Autowired
    private PointOfInterestRepository pointOfInterestRepository;

    // --- CRUD Operations ---

    public List<PointOfInterest> findAllPoints() {
        return pointOfInterestRepository.findAll();
    }

    public Optional<PointOfInterest> findPointById(Long id) {
        return pointOfInterestRepository.findById(id);
    }

    public PointOfInterest savePoint(PointOfInterest pointOfInterest) {
        return pointOfInterestRepository.save(pointOfInterest);
    }

    public void deletePointById(Long id) {
        pointOfInterestRepository.deleteById(id);
    }

    // --- Location-based Logic ---

    /**
     * Finds points of interest within a specified radius from a given location.
     *
     * @param userLat Latitude of the user's current location.
     * @param userLon Longitude of the user's current location.
     * @param radiusKm Radius in kilometers to search for points.
     * @return A list of PointOfInterest within the specified radius.
     */
    public List<PointOfInterest> findNearbyPoints(double userLat, double userLon, double radiusKm) {
        List<PointOfInterest> allPoints = pointOfInterestRepository.findAll(); // Fetches all points
        List<PointOfInterest> nearbyPoints = new ArrayList<>();

        for (PointOfInterest point : allPoints) {
            double distance = calculateHaversineDistance(userLat, userLon, point.getLatitude(), point.getLongitude());
            if (distance <= radiusKm) {
                nearbyPoints.add(point);
            }
        }
        return nearbyPoints;
    }

    /**
     * Calculates the distance between two geographical coordinates using the Haversine formula.
     *
     * @param lat1 Latitude of the first point.
     * @param lon1 Longitude of the first point.
     * @param lat2 Latitude of the second point.
     * @param lon2 Longitude of the second point.
     * @return Distance in kilometers.
     */
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS_KM * c;
    }
}
