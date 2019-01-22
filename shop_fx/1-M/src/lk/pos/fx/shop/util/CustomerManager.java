package lk.pos.fx.shop.util;

import lk.pos.fx.shop.model.CustomerDTO;

import java.util.ArrayList;

public class CustomerManager {
      static  ArrayList<CustomerDTO> customerDB=new ArrayList<>();

        public static boolean saveCustomer(CustomerDTO dto){
            customerDB.add(dto);
            return true;
        }

        public static boolean updateCustomer(CustomerDTO dto){

            for (CustomerDTO cust:customerDB) {
                if(dto.getId().trim().toUpperCase()==cust.getId().trim().toUpperCase()){
                    cust.setAddress(dto.getAddress());
                    cust.setName(dto.getName());
                    return true;
                }
            }
            return false;
        }

        public static boolean deleteCustomer(String id){
            for (CustomerDTO customerDTO:customerDB) {
                if(customerDTO.getId().trim().toUpperCase().equals(id.trim().toUpperCase())){
                    customerDB.remove(customerDTO);
                    return true;
                }
            }
            return false;
        }

        public static CustomerDTO searchCustomer(String id){
            for (CustomerDTO customerDTO:customerDB) {
                if(customerDTO.getId().trim().toUpperCase().equals(id.trim().toUpperCase())){
                    return customerDTO;
                }
            }
            return null;
        }

        public static ArrayList<CustomerDTO> getAllCustomer(){
         return customerDB;
        }


    static {
       customerDB.add( new CustomerDTO("C001","Ramesh","Bandaragama"));
        customerDB.add( new CustomerDTO("C002","Chathuranga","Panadura"));
        customerDB.add( new CustomerDTO("C003","Kasun","Horana"));
    }


}
