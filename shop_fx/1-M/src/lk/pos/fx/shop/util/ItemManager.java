package lk.pos.fx.shop.util;

import lk.pos.fx.shop.model.ItemDTO;

import java.util.ArrayList;

public class ItemManager {

    static ArrayList<ItemDTO> itemDB=new ArrayList<>();

    public static boolean saveItem(ItemDTO dto){
        itemDB.add(dto);
        return true;
    }

    public static boolean updateItem(ItemDTO dto){

        for (ItemDTO cust:itemDB) {
            if(dto.getCode().trim().toUpperCase().equals(cust.getCode().trim().toUpperCase())){
                cust.setDescription(dto.getDescription());
                cust.setUnitPrice(dto.getUnitPrice());
                cust.setQtyOnHand(dto.getQtyOnHand());
                return true;
            }
        }
        return false;
    }

    public static boolean deleteItem(String code){
        for (ItemDTO itemDTO:itemDB) {
            if(itemDTO.getCode().trim().toUpperCase().equals(code.trim().toUpperCase())){
                itemDB.remove(itemDTO);
                return true;
            }
        }
        return false;
    }

    public static ItemDTO searchItem(String id){
        for (ItemDTO itemDTO:itemDB) {
            if(itemDTO.getCode().trim().toUpperCase().equals(id.trim().toUpperCase())){
                return itemDTO;
            }
        }
        return null;
    }

    public static void ItemQtymanager(String code,int qty){
        for (ItemDTO itemDTO:itemDB) {
            if(itemDTO.getCode().equals(code)){
                int qtyOnHand = itemDTO.getQtyOnHand()-qty;
                itemDTO.setQtyOnHand(qtyOnHand);
            }
        }
    }

    public static ArrayList<ItemDTO> getAllItems(){
        return itemDB;
    }

    static {
        itemDB.add(new ItemDTO("I001","Milk powder",400.00,500));
        itemDB.add(new ItemDTO("I002","Chocolate",100.00,500));
        itemDB.add(new ItemDTO("I003","Cheese",370.54,500));
        itemDB.add(new ItemDTO("I004","Ice-Cream",200.00,500));
    }
}
