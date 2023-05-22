package com.aymaneapp.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerJPADataAccessServiceTest {
    private CustomerJPADataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSelectAllCustomers() {
        //When
        underTest.selectAllCustomers();
        //Then
        verify(customerRepository).findAll();
    }

    @Test
    void itShouldSelectCustomerById() {
        //Given
        int id =1;
        //When
        underTest.selectCustomerById(id);
        //Then
        verify(customerRepository).findById(id);

    }

    @Test
    void itShouldInsertCustomer() {
        //Given
        Customer customer = new Customer(
                1,
                "aymane",
                "hhpok",
                1
        );
        //When
        underTest.insertCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void itShouldExistsPersonWithEmail() {
        //Given
        String email = "hhpok";

        //When
        underTest.existsPersonWithEmail(email);
        //Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void itShouldDeleteCustomerById() {
        //Given
        int id = 1;
        //When
        underTest.deleteCustomerById(id);
        //Then
        verify(customerRepository).deleteById(id);
    }
}