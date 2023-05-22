package com.aymaneapp.customer;

import com.aymaneapp.exeption.DuplicateResourceException;
import com.aymaneapp.exeption.ResourceNotFound;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
   private final CustomerDao customerDao;
   public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
       this.customerDao = customerDao;
   }

   public List<Customer> getAllCustomers() {
      return customerDao.selectAllCustomers();
  }
   public Customer getCustomer(Integer id) {
       return customerDao.selectCustomerById(id).orElseThrow(()-> new ResourceNotFound(String.format("customer with id [%s] not found",id)));
    }
   public void addCustomer (Customer customer){
       boolean existsPersonWithEmail = customerDao.existsPersonWithEmail(customer.getEmail());
       if (existsPersonWithEmail){
           throw new DuplicateResourceException(
                   "mail exist deja"
           );
       }
       customerDao.insertCustomer(customer);
   }

   public void deleteCustomer (Integer id){
       Optional<Customer>optionalCustomerById = customerDao.selectCustomerById(id);
       if (optionalCustomerById.isPresent()){
            customerDao.deleteCustomerById(id);
       }else {
           throw new ResourceNotFound(String.format("customer with id [%s] not found",id));
       }

   }


}
