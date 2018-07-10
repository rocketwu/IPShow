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
import java.util.StringTokenizer;
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
    private String dipText;
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
    
    private void dIPInput()
    {
        d2b(dIP,bIP);
        
    }
    
    private void dMaskInput() {
        d2b(dMask,bMask);
        if (checkMask(bMask)) {
            //goodInput(dMask);
            mask2cidr();
        }
        else{
            badInput(dMask);
            for(TextField btf:bMask){
                btf.setText(null);
            }
            cidr.setText(null);
        }
        
    }
    
    private void d2b (TextField tf, TextField[] tfs){
        for(TextField btf: tfs){
            btf.setText(null);      //清空
        }
        String text=tf.getText();
        StringTokenizer s=new StringTokenizer(text,".");
        int index=0;
        while(s.hasMoreTokens()){
            
            try{
                Integer num=Integer.parseInt(s.nextToken());
                if (num<0||num>255) {
                    badInput(tf);
                    return;
                }
                else{
                    goodInput(tf);
                    String binary=("00000000"+Integer.toBinaryString(num));     //给二进制ip补足前缀
                    tfs[index].setText(binary.substring(binary.length()-8));
                    index++;
                }
            }catch(NumberFormatException ex){
                badInput(tf);
                return;
            }
        }
    }
    
    private boolean checkMask(TextField[] mask){
        String s="";
        for(TextField tf:mask){
            s+=tf.getText();
        }
        if (!s.contains("0")) {
            //no 0s in mask (255.255.255.255)
            return true;
        }
        else{
            s=s.substring(s.indexOf("0"));
            return (!s.contains("1"));
        }

    }
    
    private void badInput(Node tf){
        tf.setStyle("-fx-background-color: #FFCCCC");
    }
    
    private void goodInput(Node tf){
        tf.setStyle(null);
    }
    
    private void mask2cidr(){
        String s="";
        for(TextField tf:bMask){
            s+=tf.getText();
        }
        int i=(s.indexOf("0"));
        cidr.setText(Integer.toString(i==-1?32:i));
    }


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
        
        dIP.textProperty().addListener((observable, oldValue, newValue)->{
            dIPInput();
        });//添加监听
        
        dMask.textProperty().addListener((observable, oldValue, newValue)->{
            dMaskInput();
        });//添加监听
    }    

    @FXML
    public void testUnit(){
        String s="123456";
        s=s.substring(6);
        System.out.println(s);
    }

    
}
