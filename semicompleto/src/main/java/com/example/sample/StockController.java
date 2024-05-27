package com.example.sample;

import com.example.sample.Clases.Stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StockController {

    @FXML
    private TableView<Stock> tablaStock;
    @FXML
    private TableColumn<Stock, Integer> idColumn;
    @FXML
    private TableColumn<Stock, Double> precioColumn;
    @FXML
    private TableColumn<Stock, String> nombreColumn;
    @FXML
    private TableColumn<Stock, Integer> cantidadColumn;
    @FXML
    private ImageView logoutButton;

    private ObservableList<Stock> listaStock;


    public void initialize() {
        listaStock = FXCollections.observableArrayList();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        cargarDatosStock();

        tablaStock.setItems(listaStock);

        // Aplicar el estilo personalizado a las filas basándose en la columna cantidad
        tablaStock.setRowFactory(tv -> new TableRow<Stock>() {
            @Override
            protected void updateItem(Stock item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getCantidad() < 100) {
                        setStyle("-fx-background-color: #FFA07A;"); // Naranja claro
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }


    private void cargarDatosStock() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");

            String query = "SELECT * FROM Stock";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double precio = resultSet.getDouble("precio");
                String nombre = resultSet.getString("nombre");
                int cantidad = resultSet.getInt("cantidad");

                Stock stock = new Stock(id, precio, nombre, cantidad);
                listaStock.add(stock);
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
    }

    @FXML
    private void abrirPedidos() throws IOException {
        Main main = new Main();
        main.mostrarVentanaPedidos();
    }

    @FXML
    public void refrescarStockOnAction() {
        listaStock.clear();
        cargarDatosStock();
    }

    @FXML
    public void abrirStock(ActionEvent actionEvent) throws IOException {
        Main main = new Main();
        main.mostrarVentanaPedirStock();
    }

    @FXML
    private void eliminarStock() {
        Stock selectedStock = tablaStock.getSelectionModel().getSelectedItem();
        if (selectedStock != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Stock");
            alert.setHeaderText("Confirmar Eliminación");
            alert.setContentText("¿Estás seguro de que deseas eliminar el stock seleccionado?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");
                    String query = "DELETE FROM Stock WHERE id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, selectedStock.getId());
                    preparedStatement.executeUpdate();

                    listaStock.remove(selectedStock);

                    // Restar el stock correspondiente
                    actualizarStockDespuesDePedido(selectedStock);
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (preparedStatement != null) preparedStatement.close();
                        if (connection != null) connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Eliminar Stock");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un stock para eliminar.");
            alert.showAndWait();
        }
    }

    private void actualizarStockDespuesDePedido(Stock stockPedido) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");

            String updateQuery = "UPDATE Stock SET cantidad = cantidad + ? WHERE nombre = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, stockPedido.getCantidad());
            preparedStatement.setString(2, stockPedido.getNombre());
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                // Si no se actualiza ninguna fila, significa que el producto no estaba en la tabla, así que lo insertamos
                String insertQuery = "INSERT INTO Stock (nombre, cantidad) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, stockPedido.getNombre());
                preparedStatement.setInt(2, stockPedido.getCantidad());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
