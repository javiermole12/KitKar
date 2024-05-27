package com.example.sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class piezaNuevaController {

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField cantidadTextField;

    @FXML
    private TextField precioTextField;
    @FXML
    private ImageView logoutButton;

    /*@FXML
    private void agregarPieza() {
        String nombre = nombreTextField.getText();
        String cantidad = cantidadTextField.getText();
        String precio = precioTextField.getText();

        if (nombre == null || nombre.isEmpty() || cantidad == null || cantidad.isEmpty() || precio == null || precio.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Datos incompletos", "Por favor, complete todos los campos.");
            return;
        }

        int cantidadInt;
        double precioDouble;
        try {
            cantidadInt = Integer.parseInt(cantidad);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Formato inválido", "La cantidad debe ser un número entero.");
            return;
        }

        try {
            precioDouble = Double.parseDouble(precio);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Formato inválido", "El precio debe ser un número decimal.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root")) {
            String query = "INSERT INTO Stock (nombre, cantidad, precio) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombre);
            preparedStatement.setInt(2, cantidadInt);
            preparedStatement.setDouble(3, precioDouble);
            preparedStatement.executeUpdate();

            // Llamar al método para actualizar la tabla de stock
            Main.getStockController().refrescarStockOnAction();

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Nuevo stock agregado exitosamente.");

            // Cerrar la ventana actual
            Stage stage = (Stage) precioTextField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error en la base de datos", "No se pudo agregar el nuevo stock.");
        }
    }*/

    @FXML
    private void agregarPieza() {
        String nombre = nombreTextField.getText();
        String cantidad = cantidadTextField.getText();
        String precio = precioTextField.getText();

        if (nombre == null || nombre.isEmpty() || cantidad == null || cantidad.isEmpty() || precio == null || precio.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Datos incompletos", "Por favor, complete todos los campos.");
            return;
        }

        int cantidadInt;
        double precioDouble;
        try {
            cantidadInt = Integer.parseInt(cantidad);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Formato inválido", "La cantidad debe ser un número entero.");
            return;
        }

        try {
            precioDouble = Double.parseDouble(precio);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Formato inválido", "El precio debe ser un número decimal.");
            return;
        }
        if (!existeSi(nombre)) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root")) {
                String query = "INSERT INTO Stock (nombre, cantidad, precio) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nombre);
                preparedStatement.setInt(2, cantidadInt);
                preparedStatement.setDouble(3, precioDouble);
                preparedStatement.executeUpdate();

                // Llamar al método para actualizar la tabla de stock
                Main.getStockController().refrescarStockOnAction();

                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Nuevo stock agregado exitosamente.");

                // Cerrar la ventana actual
                Stage stage = (Stage) precioTextField.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error en la base de datos", "No se pudo agregar el nuevo stock.");
            }
        }else{
            showAlert(Alert.AlertType.ERROR, "El producto ya existe.", "Para añadir Stock que ya existe utilice la otra pantalla.");
        }
    }


    public boolean existeSi(String nombre){
        Connection connection = null;

        String query = "SELECT * FROM Stock WHERE nombre ='"+nombre+"'";

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }else {
                return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void limpiarCampos() {
        nombreTextField.clear();
        cantidadTextField.clear();
        precioTextField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText("Confirmar Cierre de Sesión");
        alert.setContentText("¿Estás seguro de que deseas cerrar sesión?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Cierra la ventana actual
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.close();

            // Muestra la ventana de login
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml")); // Cambia si hello-view.fxml no es la pantalla de login
                Scene scene = new Scene(fxmlLoader.load());
                Stage loginStage = new Stage();
                loginStage.setScene(scene);
                loginStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
