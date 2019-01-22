package lk.pos.fx.shop.util;

import lk.pos.fx.shop.model.OrderDetailDTO;

import java.util.ArrayList;

public class OrderDetailManager {
    private static ArrayList<OrderDetailDTO>orderDetailDB=new ArrayList<>();
    public static boolean saveOrderDetail(ArrayList<OrderDetailDTO> dtos){
        for (OrderDetailDTO detailDTO :dtos) {
            orderDetailDB.add(new OrderDetailDTO(detailDTO.getOrderId(),
                                                 detailDTO.getItemcode(),
                                                 detailDTO.getDescription(),
                                                 detailDTO.getQty(),
                                                 detailDTO.getUnitPrice()));
        }

        return true;
    }
    public static ArrayList<OrderDetailDTO> getOrderDetailDB(){
        return orderDetailDB;
    }

    public static ArrayList<OrderDetailDTO> getOdetailByOrderId(String code){
        ArrayList<OrderDetailDTO> list = new ArrayList<>();
        for (OrderDetailDTO od:orderDetailDB) {
            if(od.getOrderId().equals(code)){
                list.add(new OrderDetailDTO(od.getOrderId(),
                        od.getItemcode(),
                        od.getDescription(),
                        od.getQty(),
                        od.getUnitPrice()));
            }
        }
        return list;
    }


}
