package com.example.sample;

import com.example.sample.PedidosController;
import com.example.sample.StockController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private static StockController stockController;

    public Main() {
        primaryStage = new Stage();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        mostrarVentanaPrincipal();
    }

    public void mostrarVentanaPrincipal() throws IOException {
        FXMLLoader root = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(root.load(), 756, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mostrarVentanaStock() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("STOCK.fxml"));
        Scene scene = new Scene(loader.load(), 756, 550);
        primaryStage.setScene(scene);
        primaryStage.show();

        stockController = loader.getController();
    }

    public void mostrarVentanaPedirStock() throws IOException {
        FXMLLoader root = new FXMLLoader(getClass().getResource("PedirStock.fxml"));
        Scene scene = new Scene(root.load(), 756, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mostrarVentanapiezaNueva() throws IOException {
        FXMLLoader root = new FXMLLoader(getClass().getResource("piezaNueva.fxml"));
        Scene scene = new Scene(root.load(), 756, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }




    public void mostrarVentanaPedidos() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("pedidos.fxml"));
        Scene scene = new Scene(loader.load(), 756, 550);
        primaryStage.setScene(scene);
        primaryStage.show();

        PedidosController controller = loader.getController();
        controller.actualizarPedidos();
    }

    public void mostrarVentanaCrearPedido() throws IOException {
        FXMLLoader root = new FXMLLoader(getClass().getResource("crearPedido.fxml"));
        Scene scene = new Scene(root.load(), 756, 785);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mostrarVentanaAyuda() throws IOException {
        FXMLLoader root = new FXMLLoader(getClass().getResource("Ayuda.fxml"));
        Scene scene = new Scene(root.load(), 720, 550);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static StockController getStockController() {
        return stockController;
    }

    public static void main(String[] args) {
        launch(args);
    }


}