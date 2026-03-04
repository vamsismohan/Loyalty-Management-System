package com.loyalty.member_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.member_service.dto.CustomerRequest;
import com.loyalty.member_service.dto.MemberResponse;
import com.loyalty.member_service.dto.PointMasterRequestDTO;
import com.loyalty.member_service.dto.PointMasterResponseDTO;
import com.loyalty.member_service.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<MemberResponse> enroll(
            @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enroll(request));
    }

    @GetMapping("/pointType/{pointype}")
    public ResponseEntity<Boolean> getPoints(@PathVariable String pointype) {
        return ResponseEntity.ok(enrollmentService.getPointType(pointype));
    }

    @PostMapping("/point")
    public ResponseEntity<PointMasterResponseDTO> postMethodName(@RequestBody PointMasterRequestDTO pointDetails) { 
        return ResponseEntity.ok().body(enrollmentService.createPoints(pointDetails));
    }
    
}