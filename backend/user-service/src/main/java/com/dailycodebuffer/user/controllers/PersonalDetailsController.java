package com.dailycodebuffer.user.controllers;


import com.dailycodebuffer.user.dto.PersonalDetailsDto;
import com.dailycodebuffer.user.dto.UserDto;
import com.dailycodebuffer.user.entity.User;
import com.dailycodebuffer.user.service.PersonalDetailsService;
import com.dailycodebuffer.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personal-details")
public class PersonalDetailsController {
    @Autowired
    private PersonalDetailsService personalDetailsService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<PersonalDetailsDto>> findAllDetails() {
        return new ResponseEntity<>(personalDetailsService.getAllPersonalDetails(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PersonalDetailsDto> savePersonalDetails(@RequestBody PersonalDetailsDto personalDetailsDto) {
        return new ResponseEntity<>(personalDetailsService.savePersonalDetail(personalDetailsDto), HttpStatus.CREATED);
    }
}
