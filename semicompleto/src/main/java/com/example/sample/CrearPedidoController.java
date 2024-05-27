package com.example.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.example.sample.Clases.Pedidos;
import com.example.sample.Clases.Stock;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class CrearPedidoController implements Initializable {
    @FXML
    private TableView<Stock> tableStock;
    @FXML
    private TableColumn<Stock, Double> colStockPrecio;
    @FXML
    private TableColumn<Stock, String> colStockNombre;
    @FXML
    private TableColumn<Stock, Integer> colStockCantidad;

    @FXML
    private TableView<Pedidos> tablePedido;
    @FXML
    private TableColumn<Pedidos, Double> colPedidoPrecio;
    @FXML
    private TableColumn<Pedidos, String> colPedidoNombre;
    @FXML
    private TableColumn<Pedidos, Integer> colPedidoCantidad;

    @FXML
    private TextField txtNomProv;
    @FXML
    private TextField txtApeProv;
    @FXML
    private TextField txtCantidad;
    @FXML
    private ImageView logoutButton;

    private ObservableList<Stock> stockList = FXCollections.observableArrayList();
    private ObservableList<Pedidos> pedidoList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colStockPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStockNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colStockCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        colPedidoPrecio.setCellValueFactory(new PropertyValueFactory<>("total"));
        colPedidoNombre.setCellValueFactory(new PropertyValueFactory<>("piezas"));
        colPedidoCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        tableStock.setItems(stockList);
        tablePedido.setItems(pedidoList);

        cargarStockDesdeBD();
    }

    private void cargarStockDesdeBD() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Stock");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double precio = resultSet.getDouble("precio");
                String nombre = resultSet.getString("nombre");
                int cantidad = resultSet.getInt("cantidad");

                Stock stock = new Stock(id, precio, nombre, cantidad);
                stockList.add(stock);
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
    private void anadirProducto() {
        Stock selectedStock = tableStock.getSelectionModel().getSelectedItem();

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        if (selectedStock != null) {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            Pedidos pedido = new Pedidos(selectedStock.getPrecio(), selectedStock.getNombre(), cantidad);
            pedidoList.add(pedido);
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");
                String upQuery = "UPDATE Stock SET cantidad = ? WHERE nombre ='"+pedido.getPiezas()+"'";
                preparedStatement = connection.prepareStatement(upQuery);
                preparedStatement.setInt(1, PedirStockController.getCantidad(pedido.getPiezas())-Integer.parseInt(txtCantidad.getText()));
                preparedStatement.executeUpdate();
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
    }

    @FXML
    private void finalizarPedido() {
        String nomProv = txtNomProv.getText();
        String apeProv = txtApeProv.getText();

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PiezasCoche", "root", "root");

            for (Pedidos pedido : pedidoList) {
                String query = "INSERT INTO Pedidos (nom_prov, ape_prov, piezas, total) VALUES (?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nomProv);
                preparedStatement.setString(2, apeProv);
                preparedStatement.setString(3, pedido.getPiezas());
                preparedStatement.setDouble(4, pedido.getTotal());
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

        // Cierra la ventana después de guardar
        Stage stage = (Stage) txtNomProv.getScene().getWindow();
        stage.close();


    }

    @FXML
    private void abrirAyuda() throws IOException {
        Main main = new Main();
        main.mostrarVentanaAyuda();
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
