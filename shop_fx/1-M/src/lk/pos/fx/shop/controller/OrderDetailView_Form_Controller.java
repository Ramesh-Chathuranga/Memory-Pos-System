package lk.pos.fx.shop.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.pos.fx.shop.model.OrderDetailDTO;
import lk.pos.fx.shop.util.ItemManager;
import lk.pos.fx.shop.util.OrderDetailManager;
import lk.pos.fx.shop.view.model.PlaceOrdersTB;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderDetailView_Form_Controller implements Initializable {


    @FXML
    private AnchorPane ODForm;

    @FXML
    private TableView<PlaceOrdersTB> tableOrders;

    @FXML
    private Label labelTotal;

    @FXML
    private AnchorPane buttonHome;

    @FXML
    private ImageView home1;

    @FXML
    private Label labHome;

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
    private JFXComboBox<String> comboCode;

    @FXML
    private JFXTextField txtCustomerId;

    @FXML
    private JFXTextField txtOID;

    @FXML
    private JFXDatePicker datePicker;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
            txtCustomerId.setEditable(false);
            txtDescription.setEditable(false);
            txtName.setEditable(false);
            txtOID.setEditable(false);
            txtQty.setEditable(false);
            txtUnitPrice.setEditable(false);
            txtQtyOnHand.setEditable(false);
            datePicker.setEditable(false);
            txtName.setStyle("-fx-text-fill: white");
            txtDescription.setStyle("-fx-text-fill: white");
            txtOID.setStyle("-fx-text-fill: white");
            txtQty.setStyle("-fx-text-fill: Orange ;-fx-background-color: white; -fx-font-size: 15px");
            txtUnitPrice.setStyle("-fx-text-fill: white");
            txtQtyOnHand.setStyle("-fx-text-fill: white");
            txtCustomerId.setStyle("-fx-text-fill: white");
            datePicker.setStyle("-fx-text-fill: white ; -fx-background-color: white");
            labHome.setVisible(false);
            comboCode.setStyle(" -fx-background-color: white");

            tableOrders.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
            tableOrders.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
            tableOrders.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            tableOrders.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
            tableOrders.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

            loadComboBox();
            getSelectedRaw();
            loadTable();
            selectComboBox();
            calculateTotal();
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
        labHome.setVisible(true);
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
        labHome.setVisible(false);
    }

    @FXML
    void navigator(MouseEvent event) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/lk/pos/fx/shop/view/fxml/SearchOrderForm.fxml"));
        Scene scene=new Scene(root);
        Stage window = (Stage) ODForm.getScene().getWindow();
        window.setScene(scene);
        window.setTitle("Order_FORM                                                                         SHOP");
        window.show();
    }

    private void loadTable(){
        ArrayList<OrderDetailDTO> odetailByOrderId = OrderDetailManager.getOdetailByOrderId(this.txtOID.getText());
        ObservableList<OrderDetailDTO> arrayList = FXCollections.observableArrayList(odetailByOrderId);
        ObservableList<PlaceOrdersTB> objects = FXCollections.observableArrayList();
        for (OrderDetailDTO dto :arrayList) {
            objects.add(new PlaceOrdersTB(dto.getItemcode(),dto.getDescription(),dto.getUnitPrice(),dto.getQty(),(double)dto.getQty()*dto.getUnitPrice()));
        }
        tableOrders.setItems(objects);
    }


    private void getSelectedRaw(){
        tableOrders.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PlaceOrdersTB>() {
            @Override
            public void changed(ObservableValue<? extends PlaceOrdersTB> observable, PlaceOrdersTB oldValue, PlaceOrdersTB newValue) {
                try{

                    txtQty.setText(newValue.getQty()+"");
                    txtUnitPrice.setText(newValue.getUnitPrice()+"");
                    txtDescription.setText(newValue.getDescription());
                    txtQtyOnHand.setText(ItemManager.searchItem(newValue.getCode()).getQtyOnHand()+"");
                }catch (NullPointerException e){}
            }
        });
    }

    private  void loadComboBox(){
        comboCode.getItems().removeAll(comboCode.getItems());
        ArrayList<OrderDetailDTO> odetailByOrderId = OrderDetailManager.getOdetailByOrderId(this.txtOID.getText());
        System.out.println(odetailByOrderId.size());
        for (OrderDetailDTO dto :odetailByOrderId) {
            comboCode.getItems().add(dto.getItemcode());
        }

    }

    private  void selectComboBox(){
        comboCode.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<PlaceOrdersTB> tbs = tableOrders.getItems();
                for (PlaceOrdersTB tb :tbs) {
                    if(tb.getCode().equals(newValue)){
                       tableOrders.getSelectionModel().select(tb);
                        PlaceOrdersTB selectedItem = tableOrders.getSelectionModel().getSelectedItem();
                        txtQty.setText(selectedItem.getQty()+"");
                        txtUnitPrice.setText(selectedItem.getUnitPrice()+"");
                        txtDescription.setText(selectedItem.getDescription());
                        txtQtyOnHand.setText(ItemManager.searchItem(newValue).getQtyOnHand()+"");
                    }
                }
            }
        });
    }

    private  void calculateTotal(){
        double total=0.00;
        for (PlaceOrdersTB tb:tableOrders.getItems()) {
            total+=tb.getTotal();
        }
        labelTotal.setText(total+"");
    }

    public void getFromOrderSearch(String oid, LocalDate date, String customerId, String name){

     this.txtOID.setText(oid);
     this.txtCustomerId.setText(customerId);
     this.txtName.setText(name);
     datePicker.setValue(date);
     loadTable();
     loadComboBox();
     selectComboBox();
     calculateTotal();
    }
    public void animation_maker(Parent root, double number){
        FadeTransition fadeTransition=new FadeTransition(Duration.millis(2000),root);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(10);
        fadeTransition.play();

        TranslateTransition translateTransition=new TranslateTransition(Duration.millis(2000),root);
        translateTransition.setFromX(number/2);
        translateTransition.setToX(0);
        translateTransition.play();
    }


}
