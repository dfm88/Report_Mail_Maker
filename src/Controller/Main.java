package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(Main.class.getResource("/FXML_Files/HomePageFaxMail.fxml"));
        primaryStage.setTitle("Report Maker");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**************   ShowInfoView **********************************/
    public  void ShowInfoView() throws IOException {

        //creo un Loader per caricare questo layout nella mia classe Main
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/FXML_Files/InfoView.fxml"));

        //associo alla variabile loginLayout di tipo "AnchorPane" il file login.fxml
        Pane infoLayout = loader.load();

        //creo un nuovo stage per mostrare la nuova finestra Home
        Stage stage = new Stage();
        stage.setScene(new Scene(infoLayout));

        //blocco l'accesso alla la primaryStage del Login
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("Info");

        stage.setResizable(false);

        //mostro lo stage Homepage
        stage.show();

    }

    /**************   ShowSetting **********************************/
    public  void ShowSettingView() throws IOException {

        //creo un Loader per caricare questo layout nella mia classe Main
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/FXML_Files/SettingView.fxml"));

        //associo alla variabile loginLayout di tipo "AnchorPane" il file login.fxml
        Pane infoLayout = loader.load();

        //creo un nuovo stage per mostrare la nuova finestra Home
        Stage stage = new Stage();
        stage.setScene(new Scene(infoLayout));

        //blocco l'accesso alla la primaryStage del Login
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setResizable(false);

        stage.setTitle("Settings");

        //mostro lo stage Homepage
        stage.show();

    }

    /**************   ShowStatisticView **********************************/
    public  void ShowStatView() throws IOException {

        //creo un Loader per caricare questo layout nella mia classe Main
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/FXML_Files/StatView.fxml"));

        //associo alla variabile loginLayout di tipo "AnchorPane" il file login.fxml
        Pane infoLayout = loader.load();

        //creo un nuovo stage per mostrare la nuova finestra Home
        Stage stage = new Stage();
        stage.setScene(new Scene(infoLayout));

        //blocco l'accesso alla la primaryStage del Login
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setTitle("Statistics");

        stage.setResizable(false);

        //mostro lo stage Homepage
        stage.show();

    }

    public Alert generaAlert (Alert.AlertType tipoAlert, String titolo, String header, String messaggio)
    {
        Alert alert = (Alert)new Alert(tipoAlert);

        alert.setTitle(titolo);
        alert.setContentText(messaggio);
        alert.setHeaderText(header);



        //bordo
        alert.getDialogPane().setStyle("-fx-border-color: black");

        //alert.showAndWait();

        return alert;
    }




    public static void main(String[] args) {
        launch(args);
    }
}
