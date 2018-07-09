/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipshow;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author whyfj
 */
public class IPShowController implements Initializable {
    
    @FXML
    private TextField dIP;
    @FXML
    private TextField dMask;
    @FXML
    private TextField cidr;

    private TextField[] bIP;

    private TextField[] bMask;
    @FXML
    private Label type;
    @FXML
    private Label numOfSubnet;
    @FXML
    private Label numOfHost;
    @FXML
    private HBox HBoxOfBIP;
    @FXML
    private HBox HBoxOfBMask;
    
    
    
    
    

    @FXML
    public void exit(){
        Stage stage = (Stage) HBoxOfBIP.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //ObservableList<Node> BIPs = HBoxOfBIP.getChildren();
        List<Node> BIPs =new ArrayList<>();
        BIPs.addAll(HBoxOfBIP.getChildren());   //将node全部拷贝到list之中，以防误删
        BIPs.remove(0);
        bIP=BIPs.toArray(new TextField[0]); //特殊的toArray函数，参数表达了要cast的数组类型

        List<Node> BMasks=new ArrayList<>();
        BMasks.addAll(HBoxOfBMask.getChildren());
        BMasks.remove(0);
        bMask=BMasks.toArray(new TextField[0]);
    }    
    
}
