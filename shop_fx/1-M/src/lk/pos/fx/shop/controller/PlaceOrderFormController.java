package lk.pos.fx.shop.controller;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.pos.fx.shop.model.CustomerDTO;
import lk.pos.fx.shop.model.ItemDTO;
import lk.pos.fx.shop.model.OrderDetailDTO;
import lk.pos.fx.shop.model.OrdersDTO;
import lk.pos.fx.shop.util.CustomerManager;
import lk.pos.fx.shop.util.ItemManager;
import lk.pos.fx.shop.util.OrdersManager;
import lk.pos.fx.shop.view.model.PlaceOrdersTB;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements  Initializable{

    @FXML
    private AnchorPane anchorPaneOrderForm;

    @FXML
    private TableView<PlaceOrdersTB> tableOrders;

    @FXML
    private Label labelTotal;

    @FXML
    private JFXDatePicker txtCalander;

    @FXML
    private AnchorPane buttonHome;

    @FXML
    private ImageView home1;

    @FXML
    private Label labHome;

    @FXML
    private AnchorPane buttonBuy;

    @FXML
    private Label labBuy;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXComboBox<String> comboId;

    @FXML
    private JFXComboBox<String> comboCode;

    @FXML
    private AnchorPane buttonAdd;

    @FXML
    private Label labAdd;

    @FXML
    private AnchorPane buttonRemove;

    @FXML
    private Label labRemove;

    @FXML
    private AnchorPane butNew;

    @FXML
    private Label labNew;

    @FXML
    private Label labOrderID;

    private ArrayList<ItemDTO> tempItemsDB ;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateOrderId();
        buttonRemove.setDisable(true);
        buttonBuy.setDisable(true);
        buttonAdd.setDisable(true);
        labRemove.setVisible(false);
        labAdd.setVisible(false);
        labHome.setVisible(false);
        labBuy.setVisible(false);
        labNew.setVisible(false);

        txtName.setEditable(false);
        txtDescription.setEditable(false);
        txtQtyOnHand.setEditable(false);
        txtUnitPrice.setEditable(false);



        tableOrders.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tableOrders.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tableOrders.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tableOrders.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tableOrders.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        loadComboBox();
        selectCustomer();
        selectItem();
        selectTBRaw();
        tempItemsDB=ItemManager.getAllItems();
        loadtable();
    }

    private  void loadtable(){
        tableOrders.getItems().addListener(new ListChangeListener<PlaceOrdersTB>() {
            @Override
            public void onChanged(Change<? extends PlaceOrdersTB> c) {
               calculateTotal();
               if(tableOrders.getItems().size()==0){
                   buttonBuy.setDisable(true);
               }else{
                   buttonBuy.setDisable(false);
               }
            }
        });
    }

    private  void loadComboBox(){
        comboCode.getItems().removeAll(this.comboCode.getItems());
        comboId.getItems().removeAll(this.comboId.getItems());
        ArrayList<CustomerDTO>customerDTOS= CustomerManager.getAllCustomer();
        ArrayList<ItemDTO>itemDTOSDTOS= ItemManager.getAllItems();
        for (CustomerDTO customerDTO : customerDTOS) {
            this.comboId.getItems().add(customerDTO.getId());
        }

        for (ItemDTO itemDTO : itemDTOSDTOS) {
            this.comboCode.getItems().add(itemDTO.getCode());
        }
    }

    private void selectCustomer(){
        this.comboId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    String cId=newValue;
                    if(newValue.isEmpty()){
                        return;
                    }
                    CustomerDTO dto = CustomerManager.searchCustomer(cId);
                    String name = dto.getName();
                    comboId.getSelectionModel().select(cId);
                    txtName.setText(name);


                        txtCalander.requestFocus();

                }catch (NullPointerException e){

                }
            }
        });
    }

    private void selectItem(){
        this.comboCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

               try{
                 String code=newValue;
                   //String code=observable.getValue();
                   ItemDTO dto = ItemManager.searchItem(code);
                   txtDescription.setText(dto.getDescription());
                   txtUnitPrice.setText(Double.toString(dto.getUnitPrice()));
                   txtQtyOnHand.setText(Integer.toString(dto.getQtyOnHand()));
                   buttonAdd.setDisable(false);
                   buttonRemove.setDisable(true);

                       txtQty.requestFocus();

                 //  tableOrders.getSelectionModel().clearSelection();
               }catch (NullPointerException e){

               }
            }
        });
    }
     private  void selectTBRaw(){
       tableOrders.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PlaceOrdersTB>() {
           @Override
           public void changed(ObservableValue<? extends PlaceOrdersTB> observable, PlaceOrdersTB oldValue, PlaceOrdersTB newValue) {
               if(newValue==null){
                   return;
               }
               comboCode.getSelectionModel().select(observable.getValue().getCode());
               txtDescription.setText(newValue.getDescription());
               txtQty.setText(Integer.toString(newValue.getQty()));
               txtUnitPrice.setText(Double.toString(newValue.getUnitPrice()));
               int qtyOnHand=ItemManager.searchItem(newValue.getCode().trim()).getQtyOnHand()+newValue.getQty();
               txtQtyOnHand.setText(Integer.toString(qtyOnHand));
               buttonRemove.setDisable(false);
           }
       });
     }



    @FXML
    void createNewField(MouseEvent event) {
     resetForm();
     generateOrderId();

        txtCalander.setEditable(true);
    }

    @FXML
    void mouseEnteredAnimation(MouseEvent event) {

        AnchorPane anchorPane=(AnchorPane)event.getSource();
        String id=anchorPane.getId();
        ScaleTransition scaleTransition=new ScaleTransition(Duration.millis(1000),anchorPane);
        scaleTransition.setToY(1.2);
        scaleTransition.setToX(1.2);
        scaleTransition.play();
        DropShadow dropShadow=new DropShadow();
        dropShadow.setColor(Color.CORNFLOWERBLUE);
        dropShadow.setWidth(30);
        dropShadow.setHeight(30);
        dropShadow.setRadius(30);
        anchorPane.setEffect(dropShadow);

        try{
           switch (id){
               case "butNew":labNew.setVisible(true);
                   break;
               case "buttonRemove":labRemove.setVisible(true);
                   break;
               case "buttonAdd":labAdd.setVisible(true);
                   break;
               case "buttonBuy": labBuy.setVisible(true);
                   break;
               case "buttonHome":labHome.setVisible(true);
                   break;
               default:
                   return;
           }
       }catch (NullPointerException e){}
    }

    @FXML
    void mouseExitedAnimation(MouseEvent event) {
        AnchorPane anchorPane=(AnchorPane)event.getSource();
        String id = anchorPane.getId();
        ScaleTransition scaleTransition=new ScaleTransition(Duration.millis(1000),anchorPane);
        scaleTransition.setToY(1.0);
        scaleTransition.setToX(1.0);
        scaleTransition.play();
        anchorPane.setEffect(null);
       try{

           switch (id){
               case "butNew":labNew.setVisible(false);
                   break;
               case "buttonRemove":labRemove.setVisible(false);
                   break;
               case "buttonAdd":labAdd.setVisible(false);
                   break;
               case "buttonBuy": labBuy.setVisible(false);
                   break;
               case "buttonHome":labHome.setVisible(false);
                   break;
               default:
                   return;
           }
       }catch (NullPointerException e){

       }
    }

    @FXML
    void setTextFielClear(MouseEvent event) {
      resetField();
      tableOrders.getSelectionModel().clearSelection();
    }

    @FXML
    void navigator(MouseEvent event) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/lk/pos/fx/shop/view/fxml/DashBoardForm.fxml"));
        Scene scene=new Scene(root);
        Stage stage=(Stage)anchorPaneOrderForm.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        test();
    }

    @FXML
    void placeOrder(MouseEvent event) {

       try{
           System.out.println(comboId.getValue());
            String customerId=comboId.getValue();
           LocalDate date=txtCalander.getValue();
           if(customerId.isEmpty()){
               alertMaker("Please Select user Id ","User id Not Set",ButtonType.CLOSE);
               comboId.requestFocus();
               return;
           }
           else if(date==null){
               alertMaker("Please Select Order Date before Place Order ","Date Not Selected",ButtonType.CLOSE);
               txtCalander.requestFocus();
               return;
           }
           ArrayList<OrderDetailDTO>list=new ArrayList<>();
           for (PlaceOrdersTB tb :tableOrders.getItems()) {
               list.add(new OrderDetailDTO(labOrderID.getText(),tb.getCode(),tb.getDescription(),tb.getQty(),tb.getUnitPrice()));
               ItemManager.ItemQtymanager(tb.getCode(),tb.getQty());
           }
           OrdersDTO dto=new OrdersDTO(labOrderID.getText(),date,customerId,list);
           boolean isOrderSave = OrdersManager.saveOrder(dto);
           if(isOrderSave){
               String message="Order Save Successfully";
               alertMaker(message,"    Save"  ,ButtonType.CLOSE);
           }else{
               String message="Order Save UnSuccessfully";
               alertMaker(message,"    Not Save"  ,ButtonType.CLOSE);
           }
       }catch (NullPointerException e){
           System.out.println("nullpoint exception"+e);
           alertMaker(e.toString(),"    Save"  ,ButtonType.CLOSE);
       }
       finally {
           resetForm();

           txtCalander.requestLayout();
           labelTotal.setText("");
           labOrderID.setText("");
       }
    }

    public void test(){
        ArrayList<OrdersDTO> order = OrdersManager.getOrder();
        for (OrdersDTO dto:order) {
            System.out.println(dto);
        }
    }

     public void resetField(){
         comboCode.getSelectionModel().clearSelection();
         txtDescription.clear();
         txtQtyOnHand.clear();
         txtUnitPrice.clear();
         txtQty.clear();

     }

    public void resetForm(){
        resetField();
        comboId.getSelectionModel().clearSelection();
        txtName.clear();
        tableOrders.getSelectionModel().clearSelection();
        buttonAdd.setDisable(false);
        comboId.requestFocus();
        buttonBuy.setDisable(true);
        buttonRemove.setDisable(true);
        tableOrders.getItems().removeAll();
        tableOrders.getItems().clear();

    }


    private boolean isInteger(String qty){
        char[] chars =qty.toCharArray();
        for (char aChar : chars) {
            if (!Character.isDigit(aChar)) {
                return false;
            }
        }
        return true;
    }

    @FXML
    void addUpdateToTable(MouseEvent event) {
      System.out.println("ok");
      String code=comboCode.getValue();
      String description=txtDescription.getText();
      double unitPrice=Double.parseDouble(txtUnitPrice.getText());
      int qtyOnHand=Integer.parseInt(txtQtyOnHand.getText());
      int qty=Integer.parseInt(txtQty.getText());

            if(code.isEmpty()){
                alertMaker("this item is not selected ","       Error Report" ,ButtonType.FINISH);
                comboCode.requestFocus();
                return;
            }
            else if(txtQty.getText().trim().isEmpty()){
                alertMaker("qty is not added  ","       Error Report" ,ButtonType.FINISH);
                txtQty.requestFocus();
                return;
            } else if(!isInteger(txtQty.getText())){
                alertMaker("qty is not added  ","       Error Report" ,ButtonType.FINISH);
                txtQty.requestFocus();
                return;
            }else if(qty<0){
                alertMaker("add value to qty more than Zero ","       Error Report" ,ButtonType.FINISH);
                txtQty.requestFocus();
                return;
            } else if(qty>=qtyOnHand){
                alertMaker("qty is"+qty+ "more than Qty On Hand:"+qtyOnHand+" plase checke and add value To Qty","       Error Report" ,ButtonType.FINISH);
                txtQty.requestFocus();
                return;
            }

           if(tableOrders.getSelectionModel().isEmpty()){
               //save
               if(tableOrders.getItems().isEmpty()){
                   //table empty;
                   tableOrders.getItems().add(new PlaceOrdersTB(code,description,unitPrice,qty,unitPrice*(double)qty));
               }else {
                   //table not empty
                   boolean isHastable = notSelectedrawButHastable(code);
                   if(!isHastable){
                       tableOrders.getItems().add(new PlaceOrdersTB(code,description,unitPrice,qty,unitPrice*(double)qty));
                   }else {
                       String i=tableOrders.getSelectionModel().getSelectedItem().getCode();
                       System.out.println(i+"    i     ");
                       qty+=tableOrders.getSelectionModel().getSelectedItem().getQty();
                       double total=(double)qty*tableOrders.getSelectionModel().getSelectedItem().getUnitPrice();
                       tableOrders.getSelectionModel().getSelectedItem().setQty(qty);
                       tableOrders.getSelectionModel().getSelectedItem().setTotal(total);
                       System.out.println(i+"    i     qty :"+qty);

                   }
               }
           }else{
               //update
               if(tableOrders.getSelectionModel().getSelectedItem().getCode().equals(code)){
                   qtyOnHandOfTempDBUpdate(code);
                   PlaceOrdersTB item = tableOrders.getSelectionModel().getSelectedItem();
                   item.setQty(qty);
                   item.setTotal(qty*unitPrice);
               }else{
                   tableOrders.getItems().add(new PlaceOrdersTB(code,description,unitPrice,qty,unitPrice*(double)qty));
               }

           }

            setTempQty(code,qty);
            tableOrders.refresh();
            resetField();
       // comboCode.getSelectionModel().clearSelection();
           calculateTotal();

    }

    private boolean notSelectedrawButHastable(String code){
        for (PlaceOrdersTB tb:tableOrders.getItems()) {
            if(tb.getCode().equals(code)){
              tableOrders.getSelectionModel().select(tb);
              return true;
            }
        }
        return false;
    }



    private void setTempQty(String code, int qty) {
        for (ItemDTO item : tempItemsDB) {
            if (item.getCode().equals(code)) {
                item.setQtyOnHand(item.getQtyOnHand() - qty);
                break;
            }
        }
    }


    private void qtyOnHandOfTempDBUpdate(String code){
        ItemDTO dto = ItemManager.searchItem(code);
        for (ItemDTO itemDTO:tempItemsDB) {
            if(itemDTO.getCode().equals(code)){
                itemDTO.setQtyOnHand(dto.getQtyOnHand());
            }
        }
    }

    private void calculateTotal(){
        double total=0.00;
        for (PlaceOrdersTB tb:tableOrders.getItems()) {
            total+=tb.getTotal();
        }
        labelTotal.setText(total+"");
    }

    @FXML
    void removeFromTable(MouseEvent event) {
        boolean isDeleteOK = alertMaker("Are You sure To Delete This Item From Your List", " COMFIRMATION MESSAGE ", ButtonType.OK, ButtonType.NO);
        if(isDeleteOK) {
            PlaceOrdersTB item = tableOrders.getSelectionModel().getSelectedItem();
            tableOrders.getItems().remove(item);
            qtyOnHandOfTempDBUpdate(item.getCode());
        }
    }


    private void generateOrderId() {
        ArrayList<OrdersDTO> getAllOrders=OrdersManager.getOrder();
        int count=getAllOrders.size();
        if(getAllOrders==null || getAllOrders.isEmpty()){
            OrdersDTO oDto=new OrdersDTO();
            oDto.setOrderId("D000");
            labOrderID.setText("D001");
            return;
        }
        OrdersDTO oDTO=getAllOrders.get(count-1);
        String orderid=oDTO.getOrderId();
        String firstIndex=orderid.substring(0,1);
        String otherIndexs=orderid.substring(1);
        int setOrdeIdInt=0;
        String setOrderId=null;
        int calNumber=0;
        System.out.println(orderid.length()+""+otherIndexs.length());
        if(Integer.parseInt(orderid.substring(1, 2))==0){
            calNumber=Integer.parseInt(orderid.substring(1))+1;
            if(Integer.parseInt(orderid.substring(2, 3))==0){
                setOrderId=firstIndex+0+0+calNumber;
            }else{
                setOrderId=firstIndex+0+calNumber;
            }
        }else{
            calNumber=Integer.parseInt(orderid.substring(1))+1;
            setOrderId=firstIndex+calNumber;
        }
        labOrderID.setText(setOrderId);
    }

    public boolean alertMaker(String message, String title, ButtonType... type){
        System.out.println(message);
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION ,message, type);
        Image image=new Image("/lk/pos/fx/shop/asset/icons8_Apple_28px.png");
        Stage stage=(Stage) alert.getDialogPane().getScene().getWindow();
        alert.setTitle("SHOP"+"                                            "+title);
        stage.getIcons().add(image);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.get()==ButtonType.OK){
            return true;
        }else  if(buttonType.get()==ButtonType.NO){
            return  false;
        }
        return true;
    }

}
