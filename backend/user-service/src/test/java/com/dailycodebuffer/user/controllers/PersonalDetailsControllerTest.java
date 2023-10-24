package com.dailycodebuffer.user.controllers;

import com.dailycodebuffer.user.dto.PersonalDetailsDto;
import com.dailycodebuffer.user.dto.UserDto;
import com.dailycodebuffer.user.service.PersonalDetailsService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalDetailsControllerTest {
    @Mock
    private PersonalDetailsService personalDetailsService;

    @InjectMocks
    private PersonalDetailsController personalDetailsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllDetails() {
        // Arrange
        List<PersonalDetailsDto> personalDetailsList = new ArrayList<>();
        // Add some PersonalDetailsDto objects to the list

        when(personalDetailsService.getAllPersonalDetails()).thenReturn(personalDetailsList);

        // Act
        ResponseEntity<List<PersonalDetailsDto>> response = personalDetailsController.findAllDetails();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personalDetailsList, response.getBody());
        verify(personalDetailsService, times(1)).getAllPersonalDetails();
    }

    @Test
    public void testSavePersonalDetails() {
        // Arrange
        PersonalDetailsDto personalDetailsDto = PersonalDetailsDto.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                // Set other fields accordingly
                .build();

        when(personalDetailsService.savePersonalDetail(personalDetailsDto)).thenReturn(personalDetailsDto);

        // Act
        ResponseEntity<PersonalDetailsDto> response = personalDetailsController.savePersonalDetails(personalDetailsDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(personalDetailsDto, response.getBody());
        verify(personalDetailsService, times(1)).savePersonalDetail(personalDetailsDto);
    }
}
