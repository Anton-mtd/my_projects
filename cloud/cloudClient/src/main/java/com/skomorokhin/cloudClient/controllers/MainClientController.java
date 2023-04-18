package com.skomorokhin.cloudClient.controllers;

import com.skomorokhin.cloudClient.ClientApp;
import com.skomorokhin.cloudClient.Network;
import com.skomorokhin.cloudClient.TransferFile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainClientController implements Initializable {

    DirWinServerController serverDirectoryWin;
    DirWinClientController clientDirectoryWin;

    @FXML
    public Button fromServer;

    @FXML
    public Button toServer;

    @FXML
    public MenuItem dirServer;

    @FXML
    public MenuItem dirClient;

    @FXML
    public TreeView<File> clientTreeView;

    @FXML
    public TreeView<File> serverTreeView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Network.getInstance();

    }

    @FXML
    private void openServerDirWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApp.class.getResource("/fxmlFiles/DirServerWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Server");

        serverDirectoryWin = fxmlLoader.getController();
        serverDirectoryWin.setMainClientController(this);
        serverDirectoryWin.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openClientDirWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApp.class.getResource("/fxmlFiles/DirClientWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Client");

        clientDirectoryWin = fxmlLoader.getController();
        clientDirectoryWin.setMainClientController(this);
        clientDirectoryWin.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    protected void initServerTree(TreeView<File> tree) {
        String path = serverDirectoryWin.getDirectory();
        fillTree(tree, new File(path));
    }

    protected void initClientTree(TreeView<File> tree) {
        String path = clientDirectoryWin.getDirectory();
        fillTree(tree, new File(path));
    }

    private void fillTree(TreeView<File> tree, File file) {
        tree.setRoot(getNodesForDirectory(file));
    }

    private TreeItem<File> getNodesForDirectory(File directory) {
        TreeItem<File> root = new TreeItem<File>(directory);
        if (directory.listFiles() == null) {
            root.getChildren().add(new TreeItem<File>(directory));
            return root;
        }
        for (File node : directory.listFiles()) {
            if (node.isDirectory()) {
                root.getChildren().add(getNodesForDirectory(node));
            } else {
                root.getChildren().add(new TreeItem<File>(node));
            }
        }
        return root;
    }

    public TreeView<File> getClientTreeView() {
        return clientTreeView;
    }

    public TreeView<File> getServerTreeView() {
        return serverTreeView;
    }


    @FXML
    public void pushButtonFromServer(MouseEvent mouseEvent) {
        File fileFromServer = serverTreeView.getSelectionModel().getSelectedItem().getValue();
        TransferFile tf = createTransferFile(fileFromServer);
        tf.setDest(clientTreeView.getSelectionModel().getSelectedItem().getValue().getPath());
        Network.getInstance().setTransferFile(tf);
        clientTreeView.getSelectionModel()
                .getSelectedItem()
                .getChildren().add(new TreeItem<File>(new File(tf.getDest()
                        + File.separator
                        + tf.getFile_md5())));
    }

    @FXML
    public void pushButtonToServer(MouseEvent mouseEvent) {
        File fileToServer = clientTreeView.getSelectionModel().getSelectedItem().getValue();
        TransferFile tf = createTransferFile(fileToServer);
        tf.setDest(serverTreeView.getSelectionModel().getSelectedItem().getValue().getPath());
        Network.getInstance().setTransferFile(tf);
        serverTreeView.getSelectionModel()
                .getSelectedItem()
                .getChildren().add(new TreeItem<File>(new File(tf.getDest()
                        + File.separator
                        + tf.getFile_md5())));
    }

    private TransferFile createTransferFile(File file) {
        TransferFile transferFile = new TransferFile();
        transferFile.setFile(file);
        transferFile.setFile_md5(file.getName());
        transferFile.setStarPos(0); // Начальная позиция файла
        return transferFile;
    }
}