package com.example.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PedirStockController {

    @FXML
    private ListView<String> nombreListView;
    @FXML
    private TextField cantidadField;

    private ObservableList<String> nombresPiezas;
    private double precioActual;
    @FXML
    private ImageView logoutButton;

    @FXML
    public void initialize() {
        nombresPiezas = FXCollections.observableArrayList();
        cargarNombresPiezas();
        nombreListView.setItems(nombresPiezas);
    }

    private void cargarNombresPiezas() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root")) {
            String query = "SELECT nombre FROM Stock";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                nombresPiezas.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void actualizarPrecio() {
        String nombreSeleccionado = nombreListView.getSelectionModel().getSelectedItem();
        if (nombreSeleccionado != null) {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root")) {
                String query = "SELECT precio FROM Stock WHERE nombre = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nombreSeleccionado);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    precioActual = resultSet.getDouble("precio");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void pedirStock() {
        String nombre = nombreListView.getSelectionModel().getSelectedItem();
        String cantidad = cantidadField.getText();

        if (nombre == null || cantidad.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Datos incompletos", "Por favor, seleccione un nombre y complete la cantidad.");
            return;
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root")) {
            /*String query = "INSERT INTO Stock (nombre, precio, cantidad) VALUES (?, ?, ?)";*/
            String query = "UPDATE Stock SET cantidad = ?, precio = ? WHERE nombre ='"+nombre+"'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            /*preparedStatement.setString(1, nombre);*/
            preparedStatement.setDouble(2, precioActual);
            preparedStatement.setInt(1, Integer.parseInt(cantidad) + getCantidad(nombre));
            preparedStatement.executeUpdate();

            // Llamar al método para actualizar la tabla de stock
            Main.getStockController().refrescarStockOnAction();

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Nuevo stock agregado exitosamente.");

            // Cerrar la ventana actual
            Stage stage = (Stage) cantidadField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error en la base de datos", "No se pudo agregar el nuevo stock.");
        }
    }


    public static int getCantidad(String nombre) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int cantidad = 0;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");

            String query = "SELECT cantidad FROM Stock WHERE nombre ='"+nombre+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery(query);

            while (resultSet.next()) {
                cantidad = resultSet.getInt("cantidad");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cantidad;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void abrirpiezaNueva() throws IOException {
        Main main = new Main();
        main.mostrarVentanapiezaNueva();
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
