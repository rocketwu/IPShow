/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipshow;

import java.lang.Thread.State;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    
    private void bIPInput(){
        b2d(bIP,dIP);
    }
    
    private void bMaskInput(){
        String temp=dMask.getText();
        b2d(bMask,dMask);
        if (!checkMask(bMask)) {
            dMask.setText(temp);
            for(TextField tf:bMask){
                badInput(tf);
            }
        }
        else{
            mask2cidr();
        }
    }
    
    private void b2d(TextField[] tfs, TextField tf){
        boolean update=true;
        String dec="";
        for(TextField f:tfs){
            if(f.getText().length()!=8) {
                badInput(f);
                update=false;
                continue;
            }
            try{
                int num=Integer.parseInt(f.getText(), 2);
                if(num<0||num>255){
                    badInput(f);
                    update=false;
                }else{
                    dec+=Integer.toString(num)+".";
                    goodInput(f);
                }
            }catch(NumberFormatException ex){
                badInput(f);
                update=false;
            }
        }
        if (update) {
            dec=dec.substring(0, dec.length()-1);
            tf.setText(dec);
            
        }
        
    }
    
    private boolean checkMask(TextField[] mask){
        String s="";
        for(TextField tf:mask){
            s+=tf.getText();
        }
        
        if (s.length()!=32){
            return false;
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
    
    private void badInput(TextField tf){
        if(tf.getText().isEmpty()) return;
        tf.setStyle("-fx-background-color: #FFCCCC");
    }
    
    private void goodInput(TextField tf){
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
    
    private void cidrInput(){
        for(TextField tf:bMask){
            tf.setText("");
        }
        try{
            int num=Integer.parseInt(cidr.getText());
            if(num<0||num>32){
                badInput(cidr);
                return;
            }
            for(int i=0;i<num;i++){
                bMask[i/8].setText(bMask[i/8].getText()+"1");
            }
            for(;num<32;num++){
                bMask[num/8].setText(bMask[num/8].getText()+"0");
            }
            b2d(bMask,dMask);
            goodInput(cidr);
        }catch(NumberFormatException ex){
            badInput(cidr);
        }
    }


    @FXML
    public void exit(){
        Stage stage = (Stage) HBoxOfBIP.getScene().getWindow();
        stage.close();
    }
    
    private ChangeListener<String> dIPListener;
    
    private ChangeListener<String> dMaskListener;
    
    private ChangeListener<String> bIPListener;
    
    private ChangeListener<String> bMaskListener;
    
    private ChangeListener<String> cidrListener;
    
    
    
    
    private char getType(String ip){
        try{
            Integer.parseInt(ip, 2);
        }catch(NumberFormatException ex){
            return ' ';
        }
        if(ip.length()!=8) return ' ';
        
        char res='A';
        res+=ip.indexOf("0");
        res=res<'A'?'E':res;
        //System.out.println(res+privateOrPublic());
        
        return res;
        
    }
    
    private String privateOrPublic (){
        String ip=dIP.getText();
        StringTokenizer s=new StringTokenizer(ip,".");
        String[] ary=new String[4];
        int index=0;
        while(s.hasMoreTokens()){
            ary[index]=s.nextToken();
            index++;
        }
        
        try{
            int ip1=Integer.parseInt(ary[0]);
            int ip2=Integer.parseInt(ary[1]);
            switch (ip1){
                case 10:
                    return "private";
                case 172:
                    if (ip2>=16&&ip2<=31) {
                        return "private";
                    }
                    break;
                case 192:
                    if (ip2==168) return "private";
                    break;
                default:
                    return "public";
            }
        }catch(NumberFormatException ex){
            return "";
        }
        
        return "public";
    }
    
    private long getNumOfSubnet() {
        char cType=getType(bIP[0].getText());
        if(checkMask(bMask)){
            String mask="";
            long _numOfSubnet=-1;
            for(TextField tf:bMask){
                mask+=tf.getText();
            }
                switch(cType){
                case 'A':
                    mask=mask.substring(8);
                    _numOfSubnet=(long)Math.pow(2,mask.indexOf("0"));
                    break;
                case 'B':
                    mask=mask.substring(16);
                    _numOfSubnet=(long)Math.pow(2,mask.indexOf("0"));
                    break;
                case 'C':
                    mask=mask.substring(24);
                    _numOfSubnet=(long)Math.pow(2,mask.indexOf("0"));
                    break;
                default:
                    _numOfSubnet=-1;
            }
            return _numOfSubnet;
        }
        return -1;
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
        
        initListener();
    }    
    
    private void initListener(){
        dIPListener=(observable, oldValue, newValue)->{
            for(TextField tf:bIP){
                tf.textProperty().removeListener(bIPListener);
             }
            dIPInput();
            for(TextField tf:bIP){
                tf.textProperty().addListener(bIPListener);
             }
        };
        
        dMaskListener=(observable, oldValue, newValue)->{
            for(TextField tf:bMask){
                tf.textProperty().removeListener(bMaskListener);
            }
            cidr.textProperty().removeListener(cidrListener);
            dMaskInput();
            cidr.textProperty().addListener(cidrListener);
            for(TextField tf:bMask){
                tf.textProperty().addListener(bMaskListener);
            }
        };
        
        bIPListener=(observable, oldValue, newValue)->{
            dIP.textProperty().removeListener(dIPListener);
            bIPInput();
            dIP.textProperty().addListener(dIPListener);
        };
        
        bMaskListener=(observable, oldValue, newValue)->{
            dMask.textProperty().removeListener(dMaskListener);
            cidr.textProperty().removeListener(cidrListener);
            bMaskInput();
            cidr.textProperty().addListener(cidrListener);
            dMask.textProperty().addListener(dMaskListener);            
        };
        
        cidrListener=(observable, oldValue, newValue)->{
            for(TextField tf:bMask){
                tf.textProperty().removeListener(bMaskListener);
            }
            dMask.textProperty().removeListener(dMaskListener);
            cidrInput();
            dMask.textProperty().addListener(dMaskListener);
            for(TextField tf:bMask){
                tf.textProperty().addListener(bMaskListener);
            }
        };
        
        dIP.textProperty().addListener(dIPListener);//添加监听        
        dMask.textProperty().addListener(dMaskListener);//添加监听
        cidr.textProperty().addListener(cidrListener);
        for(TextField tf:bIP){
            tf.textProperty().addListener(bIPListener);
        }
        for(TextField tf:bMask){
            tf.textProperty().addListener(bMaskListener);
        }
        
        bIP[0].textProperty().addListener((ob,ov,nv)->{
                char cType=getType(nv);
                if(cType!=' '){
                    type.setText("Class: "+cType+" ["+privateOrPublic()+"] ");
                }
            });
        cidr.textProperty().addListener((ob,ov,nv)->{
            
            if (!checkMask(bMask)){
                numOfSubnet.setText("# of Subnets: ");
                numOfHost.setText("# of Hosts per Subnet: ");
            }
            
            long subnets=getNumOfSubnet();
            numOfSubnet.setText("# of Subnets: "+(subnets!=-1?subnets:" "));
            long hosts=getNumOfHosts(subnets);
            numOfHost.setText("# of Hosts per Subnet: "+(hosts!=-1?hosts:" "));
        });
    }
    
    private long getNumOfHosts(long subnets){
        String mask="";
        if (!checkMask(bMask)||subnets==-1) {
            return -1;
        }
        for(TextField tf:bMask){
            mask+=tf.getText();
        }
        try{
            mask=mask.substring(mask.indexOf("0"));
            return (long)Math.pow(2, mask.length())-2;
        }catch(NumberFormatException ex){
            return -1;
        }
    }
    
    @FXML
    public void testUnit(){
        String s="0123456";
        s=s.substring(2);
        System.out.println(s);
    }



    
}
