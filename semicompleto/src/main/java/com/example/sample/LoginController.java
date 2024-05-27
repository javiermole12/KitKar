package com.example.sample;

import com.example.sample.Clases.Stock;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import com.example.sample.*;

public class LoginController implements Initializable {
    @FXML
    private Button cancelarButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField usuarioTextField;
    @FXML
    private PasswordField contrasenyaPasswordField;
    private Main a;
    public LoginController()
    {
        a = new Main();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("Images/KitCar.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        //brandingImageView.setImage(brandingImage);

        File lockFile = new File("Images/Diseño_sin_título-removebg-preview.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        //lockImageView.setImage(lockImage);
    }

    public void loginButtonOnAction(ActionEvent event) throws IOException {
        databaseConection connectNow = new databaseConection();
        Connection connectDB = connectNow.getConnection ( );

        String user = usuarioTextField.getText();
        String pass = contrasenyaPasswordField.getText();

        String url = "select username, contrasena, rol from Usuarios where username='"+user+"'";

        try {
            PreparedStatement ps = connectDB.prepareStatement(url);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                //Si existe el usuario
                String u = rs.getString("username");
                String p = rs.getString("contrasena");
                String r = rs.getString("rol");

                if (pass.equals(p)){
                    if (r.equals("Mozo de Almacen")){
                        cerrarVentana(event);
                        a.mostrarVentanaStock();
                    } else if (r.equals("Proveedor")) {
                        cerrarVentana(event);
                        a.mostrarVentanaPedidos();
                    }

                }else{
                    showAlert();
                }
            }else {
                //El usuario no existe
                showAlert();
            }
        }catch (SQLException e){
            System.out.println(e.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Alerta!");
        alert.setContentText("El usuario con el que está intentando logearse no tiene permisos para acceder a la aplicación, contacte con el CAU para poder solucionarlo.");
        alert.show();
    }

    public void cancelarButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelarButton.getScene().getWindow();
        stage.close();;
    }

    public boolean validateLogin(){
        databaseConection connectNow = new databaseConection();
        Connection connectDB = connectNow.getConnection ( );

        String verifyLogin = "select count(1) from Usuarios where userName = '" + usuarioTextField.getText() + "' AND contrasena ='"+ contrasenyaPasswordField.getText()+ "'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()){
                if (queryResult.getInt(1) == 1){
                    loginMessageLabel.setText("Enhorabuena!");
                    return true;
                }else {
                    loginMessageLabel.setText("Usuario erroneo pruebe otra vez.");
                    return false;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }


    public static void cerrarVentana (ActionEvent e){
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


}