package lk.pos.fx.shop.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.pos.fx.shop.model.OrderDetailDTO;
import lk.pos.fx.shop.model.OrdersDTO;
import lk.pos.fx.shop.util.CustomerManager;
import lk.pos.fx.shop.util.OrderDetailManager;
import lk.pos.fx.shop.util.OrdersManager;
import lk.pos.fx.shop.view.model.SearchOrderTB;

public class SearchOrderFormController implements Initializable{

    @FXML
    private AnchorPane searchOrder;

    @FXML
    private TableView<SearchOrderTB> tabSearchOrder;

    @FXML
    private AnchorPane goHome;

    @FXML
    private Label labHome;

    @FXML
    private AnchorPane txtPane;

    @FXML
    private JFXTextField txtOID;

   private ObservableList<SearchOrderTB>  observableList;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labHome.setVisible(false);
        txtOID.setOpacity(0.3);
        txtOID.requestFocus();

        tabSearchOrder.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("oid"));
        tabSearchOrder.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
        tabSearchOrder.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tabSearchOrder.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("name"));
        tabSearchOrder.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        loadTable();
        selectionOfTablee();
    }

   private void selectionOfTablee(){

        tabSearchOrder.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SearchOrderTB>() {
            @Override
            public void changed(ObservableValue<? extends SearchOrderTB> observable, SearchOrderTB oldValue, SearchOrderTB newValue) {
                   txtOID.setText(newValue.getOid());
                System.out.println(txtOID.getText());
            }
        });
   }

    @FXML
    void tbonClick(MouseEvent event) {
        try {
            SearchOrderTB selectedItem = tabSearchOrder.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/lk/pos/fx/shop/view/fxml/orderDetailView_Form.fxml"));
            Parent parent=fxmlLoader.load();
            OrderDetailView_Form_Controller controller = fxmlLoader.getController();
            controller.getFromOrderSearch(selectedItem.getOid(),selectedItem.getDate(),selectedItem.getCustomerId(),selectedItem.getName());
            String title=" Order DEtail viwer                          SHOP";
            goToPage(parent,title);


         } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public   void  goToPage(Parent root, String title){
            Scene scene=new Scene(root);
            Stage stage= (Stage) searchOrder.getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(title);
            stage.getIcons().add(new Image("/lk/pos/fx/shop/asset/icons8_Apple_28px.png"));
            stage.show();
            animation_maker(root,scene.getHeight() / 2);
    }


    @FXML
    void navigator(MouseEvent event) throws IOException {
        Parent root= FXMLLoader.load(this.getClass().getResource("/lk/pos/fx/shop/view/fxml/DashBoardForm.fxml"));
        goToPage(root," SHOP                                                                                       DASHBARD");
    }


    private void loadTable(){
       ArrayList<OrderDetailDTO> detailDB = OrderDetailManager.getOrderDetailDB();
       ObservableList<OrderDetailDTO> list = FXCollections.observableArrayList(detailDB);
        observableList = FXCollections.observableArrayList();
       for (OrderDetailDTO dto :list ) {
           String oid=dto.getOrderId();
           OrdersDTO order = OrdersManager.searchOrder(oid);
           SearchOrderTB searchOrderTB = new SearchOrderTB(dto.getOrderId(),
                   order.getDate(),
                   order.getCustomerId(),
                   CustomerManager.searchCustomer(order.getCustomerId()).getName(),
                   (double) dto.getQty() * dto.getUnitPrice()
           );
           observableList.add(searchOrderTB);
       }
      tabSearchOrder.setItems(observableList);
   }

    @FXML
    void mouse_Entered_Animation(MouseEvent event) {

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
           switch (id){
               case "goHome":labHome.setVisible(true);
                   break;
               case "txtPane" :txtOID.setOpacity(1);
                   txtOID.requestFocus();
                   break;
               default:break;
           }

    }

    @FXML
    void mouse_Exited_Animation(MouseEvent event) {

            AnchorPane anchorPane=(AnchorPane)event.getSource();
            String id = anchorPane.getId();
            ScaleTransition scaleTransition=new ScaleTransition(Duration.millis(1000),anchorPane);
            scaleTransition.setToY(1.0);
            scaleTransition.setToX(1.0);
            scaleTransition.play();
            anchorPane.setEffect(null);
            switch (id){
                case "goHome":labHome.setVisible(false);
                    break;
                case "txtPane" :txtOID.setOpacity(0.3);
                   goHome.requestFocus();
                    break;
                default:break;
            }

    }



    @FXML
    void setOderDetails(KeyEvent event) {


        ObservableList<SearchOrderTB> tempList = FXCollections.observableArrayList();
//        System.out.println("TEST : " + olOrders);
        for (SearchOrderTB searchOrder :  observableList) {
            if (searchOrder.getOid().startsWith(txtOID.getText())){
                tempList.add(searchOrder);
            }
        }

        tabSearchOrder.setItems(tempList);

    }

    public void animation_maker(Parent root,double number){
        FadeTransition fadeTransition=new FadeTransition(Duration.millis(2000),root);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(10);
        fadeTransition.play();

        TranslateTransition translateTransition=new TranslateTransition(Duration.millis(2000),root);
        translateTransition.setFromY(number/2);
        translateTransition.setToY(0);
        translateTransition.play();
    }

}


