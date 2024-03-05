package com.paran.sample.domain.parking.api;

import com.paran.sample.domain.common.dto.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/parking-area")
@RequiredArgsConstructor

public class ParkingAreaController {

    @GetMapping("/list")
    public ResponseEntity<?> getParkingAreaList() {
        return ok(ResponseWrapper.of("ok"));
    }
}
