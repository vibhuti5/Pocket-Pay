package com.dailycodebuffer.user.service;

import com.dailycodebuffer.user.dto.PersonalDetailsDto;
import com.dailycodebuffer.user.dto.UserDto;
import com.dailycodebuffer.user.entity.PersonalDetails;
import com.dailycodebuffer.user.entity.User;
import com.dailycodebuffer.user.exception.NotFoundException;
import com.dailycodebuffer.user.exception.PostException;
import com.dailycodebuffer.user.repository.PersonalDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


public class PersonalDetailsImplTest {

    @Mock
    private PersonalDetailsRepository personalDetailsRepository;

    @InjectMocks
    private PersonalDetailsImpl personalDetailsServiceImpl;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        modelMapper = new ModelMapper();
    }

    @Test
    public void testGetAllPersonalDetails() {
        // Arrange
        List<PersonalDetails> personalDetailsList = new ArrayList<>();
        // Add some PersonalDetails objects to the list

        when(personalDetailsRepository.findAll()).thenReturn(personalDetailsList);

        // Act
        List<PersonalDetailsDto> personalDetailsDtoList = personalDetailsServiceImpl.getAllPersonalDetails();

        // Assert
        assertNotNull(personalDetailsDtoList);
        assertEquals(personalDetailsList.size(), personalDetailsDtoList.size());
        verify(personalDetailsRepository, times(1)).findAll();
    }

    @Test
    public void testSavePersonalDetail() {
        // Arrange
        PersonalDetailsDto personalDetailsDto = PersonalDetailsDto.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                // Set other fields accordingly
                .build();

        PersonalDetails personalDetails = modelMapper.map(personalDetailsDto, PersonalDetails.class);

        when(personalDetailsRepository.save(any(PersonalDetails.class))).thenReturn(personalDetails);

        // Act
        PersonalDetailsDto savedPersonalDetailsDto = personalDetailsServiceImpl.savePersonalDetail(personalDetailsDto);

        // Assert
        assertNotNull(savedPersonalDetailsDto);
        assertEquals(personalDetailsDto.getId(), savedPersonalDetailsDto.getId());
        assertEquals(personalDetailsDto.getFirstName(), savedPersonalDetailsDto.getFirstName());
        // Add assertions for other fields
        verify(personalDetailsRepository, times(1)).save(any(PersonalDetails.class));
    }

    @Test
    public void testGetAllPersonalDetails_EmptyList() {
        // Arrange
        List<PersonalDetails> personalDetailsList = new ArrayList<>();
        when(personalDetailsRepository.findAll()).thenReturn(personalDetailsList);

        // Act
        List<PersonalDetailsDto> personalDetailsDtoList = personalDetailsServiceImpl.getAllPersonalDetails();

        // Assert
        assertNotNull(personalDetailsDtoList);
        assertTrue(personalDetailsDtoList.isEmpty());
        verify(personalDetailsRepository, times(1)).findAll();
    }
}
