/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Model.InHouse;
import Model.Inventory;
import Model.Part;
import Model.Product;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author cjd
 */
public class MainViewController implements Initializable {
          
    //Part FXIDs
    @FXML private TableView<Part> PartsTable;
    @FXML private TableColumn<Part, Integer> PartId;
    @FXML private TableColumn<Part, String> PartName;
    @FXML private TableColumn<Part, Integer> PartStock;
    @FXML private TableColumn<Part, Double> PartPrice;
    
    @FXML private TextField PartsSearch;
    
    
    
    //Product FXIDs
    @FXML private TableView<Product> ProductsTable;
    @FXML private TableColumn<Product, Integer> ProductId;
    @FXML private TableColumn<Product, String> ProductName;
    @FXML private TableColumn<Product, Integer> ProductStock;
    @FXML private TableColumn<Product, Double> ProductPrice;
    
    @FXML private TextField ProductsSearch;

    static boolean loaded = false;
    
    
    
    /*
    
    NOTE: The following Methods are for Part Event Handling
    
    */
    public void ButtonSearchParts(ActionEvent event){
        
        ObservableList<Part> part = Inventory.lookupPart(PartsSearch.getText());
       
        PartsTable.setItems(part);
        
    }
    
    public void ButtonAddPart(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ViewController/AddPartView.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    public void ButtonModifyPart(ActionEvent event) throws IOException{
        
        
        try {
            
            Part part = PartsTable.getSelectionModel().getSelectedItem();

            ModifyPartViewController.PartIndex = part.getId();

            Parent root = FXMLLoader.load(getClass().getResource("/ViewController/ModifyPartView.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();

        }catch(Exception e){
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No item was selected from the Parts List");

            alert.showAndWait();
            
        }
        
    }
    
    public void ButtonDeletePart(ActionEvent event){

        //see Inventory.deletePart() for alerts
        Part part = PartsTable.getSelectionModel().getSelectedItem();
        Inventory.deletePart(part);

    }
    
    
    
    /*
    
    NOTE: The following Methods are for Product Event Handling
    
    */ 
    public void ButtonSearchProducts(ActionEvent event){
        ObservableList<Product> product = Inventory.lookupProduct(ProductsSearch.getText());
        ProductsTable.setItems(product);
    }
    
    public void ButtonAddProduct(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/ViewController/AddProductView.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        
    }
    
    public void ButtonModifyProduct(ActionEvent event) throws IOException{
        
        try{

            Product product = ProductsTable.getSelectionModel().getSelectedItem();

            ModifyProductViewController.ProductIndex=product.getId();

            Parent root = FXMLLoader.load(getClass().getResource("/ViewController/ModifyProductView.fxml"));
            Scene scene = new Scene(root);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        
        }catch(Exception e){
            
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No item was selected from the Products List");

            alert.showAndWait();
            
        }
    }
    
    public void ButtonDeleteProduct(ActionEvent event){
        
        Alert deleteProductAlert = new Alert(AlertType.CONFIRMATION);
        deleteProductAlert.setTitle("Confirmation Dialog");
        deleteProductAlert.setHeaderText("ALERT! you've selected to delete a product, deleting a product removes the part associations.");
        deleteProductAlert.setContentText("OK to Continue?");

        Optional<ButtonType> result = deleteProductAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            Product product = ProductsTable.getSelectionModel().getSelectedItem();
            Inventory.deleteProduct(product);
        } else {
            // ... user chose CANCEL or closed the dialog
        }
        

    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //loading initial data
        if(!loaded){
            
            for (int i = 0; i < 10; i++) {
                Part part = new InHouse(Inventory.getAllParts().size(), "Part" + String.valueOf(i), (double) i, i, i, i, i);
                Inventory.addPart((InHouse) part);

                Product product = new Product(Inventory.getAllProducts().size(), "Product" + String.valueOf(i), (double) i, i, i, i);
                Inventory.addProduct(product);
                
            }
            loaded = true;
        }
     
        PartId.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        PartName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        PartStock.setCellValueFactory(cellData -> cellData.getValue().getStockProperty().asObject());
        PartPrice.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty().asObject());

        PartsTable.setItems(Inventory.getAllParts());
        

        ProductId.setCellValueFactory(cellData -> cellData.getValue().getIdProperty().asObject());
        ProductName.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        ProductStock.setCellValueFactory(cellData -> cellData.getValue().getStockProperty().asObject());
        ProductPrice.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty().asObject());
        
        ProductsTable.setItems(Inventory.getAllProducts());

        
    }
}