package lk.pos.fx.shop.util;

import lk.pos.fx.shop.model.OrdersDTO;

import java.util.ArrayList;

public class OrdersManager {
   private static ArrayList<OrdersDTO>orderDB=new ArrayList<>();

   public static boolean saveOrder(OrdersDTO dto){
       orderDB.add(dto);
       boolean isOrderDetailSave = OrderDetailManager.saveOrderDetail(dto.getOrderDetailDTOS());
       System.out.println(dto);
       if(isOrderDetailSave){
           return true;
       }
       return false;
   }

    public static boolean updateOrder(OrdersDTO dto){
        for (OrdersDTO dto1:orderDB) {
            if(dto1.getOrderId().trim().toUpperCase().equals(dto.getOrderId().trim().toUpperCase())){
                dto1.setDate(dto.getDate());
                dto1.setCustomerId(dto.getCustomerId());
                return true;
            }
        }
        return false;
    }

    public static boolean deleteOrder(String oid){
        for (OrdersDTO dto1:orderDB) {
            if(dto1.getOrderId().trim().toUpperCase().equals(oid.trim().toUpperCase())){
               orderDB.remove(dto1);
                return true;
            }
        }
        return false;
    }

    public static OrdersDTO searchOrder(String oid){
        for (OrdersDTO dto1:orderDB) {
            if(dto1.getOrderId().trim().toUpperCase().equals(oid.trim().toUpperCase())){

                return dto1;
            }
        }
        return null;
    }

    public static ArrayList<OrdersDTO> getOrder(){
      return orderDB;
    }


}
