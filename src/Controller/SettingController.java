package Controller;

import Model.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class SettingController implements Initializable  {

    @FXML
    private Pane internalPane;

    @FXML
    private TextField masterResourcesField;

    @FXML
    private Text aaa;

    @FXML
    private PasswordField passwordArea;

    @FXML
    private Text passworsResultMessage;

    FileManager fm;

    @FXML
    void submitPass(ActionEvent event ) {

        verificaPassword();

    }

    public void verificaPassword()
    {
        String pass = "mais0529";

        passworsResultMessage.setVisible(true);

        if(passwordArea.getText().equals(pass))
        {
            internalPane.setVisible(true);
            internalPane.setDisable(false);
            passworsResultMessage.setFill(Color.DARKGREEN);
            passworsResultMessage.setText("Password corretta");
        } else
        {
            passworsResultMessage.setFill(Color.RED);
            passworsResultMessage.setText("Password errata");
        }

    }


    @FXML
    void salvaPercorsoMaster(MouseEvent event) throws IOException {

        //ATTENZIONE, deve necessariamente esistere il file esterno
        Properties propExternalResources = fm.getPropertiesFileFromDISK(fm.getPercorsoRisorse()+"Config\\config.properties");
        fm.setPropertiesFileOnDISK("MasterPath", masterResourcesField.getText(), propExternalResources);

    }

    public  Text getAaa() {
        return aaa;
    }

    public void setAaa(Text aaa) {
        this.aaa = aaa;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            passworsResultMessage.setVisible(false);

            internalPane.setVisible(false);
            internalPane.setDisable(true);

            fm = new FileManager();

            aaa.setText(fm.getPercorsoRisorse());
            masterResourcesField.setText(aaa.getText());


        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
