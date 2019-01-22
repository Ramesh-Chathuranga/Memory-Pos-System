package lk.pos.fx.shop.util;

import lk.pos.fx.shop.model.UsersDTO;

import java.util.ArrayList;

public class UserManager {
  private static  ArrayList<UsersDTO> usersDB =new ArrayList<>();
  static UsersDTO currentUser=null;
  static int currentUserId=0;


    public static boolean addUsers(UsersDTO dto){
        usersDB.add(dto);
        currentUser=dto;
        return true;
    }

    public static UsersDTO getCurrentUser(){
        return currentUser;
    }

    public static void deleteUser(int id){
        usersDB.remove(id);
    }

    static {
        usersDB.add(new UsersDTO("Ramesh","chathuranga"));
    }
    public static boolean searchUsers(String name){
        String nameUp=name.toUpperCase();
        for (UsersDTO user: usersDB) {
          if(user.getName().trim().toUpperCase().equals(nameUp)){
              return true;
          }
        }
        return false;
    }

    public static int getUserId(String name,String password){
        int  i=0;
        for (UsersDTO user: usersDB) {
            if(user.getName().trim().toUpperCase().equals(name.toUpperCase()) && user.getPassword().trim().equals(password)){
               currentUserId=i;
               currentUser=user;
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int getUserId(String name){
        String nameUp=name.toUpperCase();
        int i=0;
        for (UsersDTO user: usersDB) {
            if(user.getName().trim().toUpperCase().equals(nameUp)){
                return i;
            }
            i++;
        }
        return -1;
    }

}
