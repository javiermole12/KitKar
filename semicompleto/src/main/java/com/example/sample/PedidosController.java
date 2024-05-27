package com.example.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.example.sample.Clases.Pedidos;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class PedidosController implements Initializable {
    private ObservableList<Pedidos> pedidosList = FXCollections.observableArrayList();

    @FXML
    private TableView<Pedidos> table_PEDIDOS;
    @FXML
    private TableColumn<Pedidos, Integer> col_ID;
    @FXML
    private TableColumn<Pedidos, String> col_NOM_PROV;
    @FXML
    private TableColumn<Pedidos, String> col_APE_PROV;
    @FXML
    private TableColumn<Pedidos, String> col_PIEZAS;
    @FXML
    private TableColumn<Pedidos, Double> col_TOTAL;
    @FXML
    private ImageView logoutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_NOM_PROV.setCellValueFactory(new PropertyValueFactory<>("nomProv"));
        col_APE_PROV.setCellValueFactory(new PropertyValueFactory<>("apeProv"));
        col_PIEZAS.setCellValueFactory(new PropertyValueFactory<>("piezas"));
        col_TOTAL.setCellValueFactory(new PropertyValueFactory<>("total"));

        table_PEDIDOS.setItems(pedidosList);

        cargarPedidosDesdeBD();

        // Asociar el evento de clic en el botón de logout
        logoutButton.setOnMouseClicked(event -> handleLogout());
    }

    private void cargarPedidosDesdeBD() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Pedidos");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nomProv = resultSet.getString("nom_prov");
                String apeProv = resultSet.getString("ape_prov");
                String piezas = resultSet.getString("piezas");
                double total = resultSet.getDouble("total");

                Pedidos pedido = new Pedidos(id, nomProv, apeProv, piezas, total, 0);
                pedidosList.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void eliminarPedido() {
        Pedidos selectedPedido = table_PEDIDOS.getSelectionModel().getSelectedItem();
        if (selectedPedido != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar Pedido");
            alert.setHeaderText("Confirmar Eliminación");
            alert.setContentText("¿Estás seguro de que deseas eliminar el pedido seleccionado?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");
                    String query = "DELETE FROM Pedidos WHERE id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, selectedPedido.getId());
                    preparedStatement.executeUpdate();

                    pedidosList.remove(selectedPedido);
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
            alert.setTitle("Eliminar Pedido");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un pedido para eliminar.");
            alert.showAndWait();
        }
    }

    @FXML
    private void abrirCrearPedido() throws IOException {
        Main main = new Main();
        main.mostrarVentanaCrearPedido();
    }

    public void actualizarPedidos() {
        pedidosList.clear();
        cargarPedidosDesdeBD();
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
