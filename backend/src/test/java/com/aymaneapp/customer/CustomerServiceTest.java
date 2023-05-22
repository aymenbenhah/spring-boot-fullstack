package com.aymaneapp.customer;

import com.aymaneapp.exeption.DuplicateResourceException;
import com.aymaneapp.exeption.ResourceNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void itShouldGetAllCustomers() {
        //When
        underTest.getAllCustomers();
        //Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void itShouldGetCustomer() {
        //Given
        int id = 1;
        Customer customer = new Customer(
                id,
                "hih",
                "email",
                27
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        Customer actual = underTest.getCustomer(id);
        //Then
        assertThat(actual).isEqualTo(customer);
    }
    @Test
    void itWillThrowWhenCustomerNotFound() {
        //Given
        int id = 1;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(()->underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage((String.format("customer with id [%s] not found",id)));
    }

    @Test
    void itShouldAddCustomer() {
        //Given
        String email = "hiho";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        Customer customer = new Customer(
                1,
                "aymen",
                email,
                23
        );
        //When

        underTest.addCustomer(customer);
        //Then
        //verify(customerDao).insertCustomer(customer);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer captredCustomer = customerArgumentCaptor.getValue();
        assertThat(captredCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(captredCustomer.getName()).isEqualTo(customer.getName());
        assertThat(captredCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captredCustomer.getId()).isEqualTo(customer.getId());
    }
    @Test
    void itShouldThrowWhenEmailExist() {
        //Given
        String email = "hiho";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);
        //When

        assertThatThrownBy(()->underTest.addCustomer(new Customer(1,"ayamne",email,12)))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("mail exist deja");
        verify(customerDao,never()).insertCustomer(any());
    }

    @Test
    void itShouldDeleteCustomer() {
        //Given
        int id = 1;
        Customer customer = new Customer(
                id,
                "hih",
                "email",
                27
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When
        underTest.deleteCustomer(id);
        //Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void itShouldThrowWhenCustomerDoesNotExistCustomer() {
        //Given
        int id = 1;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //When
        assertThatThrownBy(()->underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage(String.format("customer with id [%s] not found",id));
        //verify(customerDao,never()).deleteCustomerById(any());
    }
}