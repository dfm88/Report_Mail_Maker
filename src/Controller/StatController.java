package Controller;

import Model.StatisticsMailConverted;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatController implements Initializable {

    @FXML
    private Text lastSavedTimeText;

    @FXML
    private Text totalSavedTimeText;

    @FXML
    private Text lastConvNumbText;

    @FXML
    private Text totalConvNumText;

    @FXML
    private ImageView question1;

    @FXML
    private ImageView question2;

    @FXML
    private Circle circle1;

    @FXML
    private Circle circle2;

    @FXML
    private Text lastFailedConversionNum;

    @FXML
    private Text totalFailedConversionNum;

    StatisticsMailConverted smc;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            smc = new StatisticsMailConverted();
            lastSavedTimeText.setText(smc.lastConversionSavedTime());
            lastConvNumbText.setText(String.valueOf(smc.lastConversionNumber()));
            totalSavedTimeText.setText(smc.totalConversionsSavedTime());
            totalConvNumText.setText(String.valueOf(smc.totalConversionsNumber()));
            lastFailedConversionNum.setText(String.valueOf(smc.lastFailedNumber()));
            totalFailedConversionNum.setText(String.valueOf(smc.totalFailedNumber()));

            Tooltip tt  = new Tooltip("Calcolato stimando 20 sec. come\ntempo minimo necessario per\ncreare manualmente il rapporto");
            Tooltip.install(question1, tt);
            Tooltip.install(question2, tt);
            Tooltip.install(circle1, tt);
            Tooltip.install(circle2, tt);

        } catch (IOException | DatatypeConfigurationException e) {
            e.printStackTrace();
        }
    }
}
