package Controller;

import Model.CompilaRapporti;
import Model.FileManager;
import Model.GetDatiFromMattinale;
import Model.StatisticsMailConverted;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import Controller.Main;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static String FileInserito;
    private static String CartellaDelFileInserito;
    private static String CartellaDeiRapportiCreatiXLS;
    private static String CartellaDeiRapportiCreatiPDF;
    private static String nomRap;
    private static int contatore;
    private static int extTime;
    private static boolean processoFinito = false;
    private static boolean dropInCorso;
    Main main = new Main();
    @FXML
    private Text labelll;

    private static Alert al1;




    @FXML
    private ImageView ImmagineXlsVuoto;

    @FXML
    private Button avvioButton;

    @FXML
    private Text testo;

    @FXML
    private ImageView ImmagineXlsPiena;


    @FXML
    private Text testoRapporto;

    @FXML
    private Text tempoRimasto;

    @FXML
    private Text elementiCreati;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Text tempoEffett;

    @FXML
    private Text percentualeText;

    @FXML
    private Button statButton;

    private int inizio;
    private int fine;

    //  IntegerProperty rapportiABuonFine = new SimpleIntegerProperty();

    int rapportiABuonFine;

    Instant inizioConversione;
    Instant fineConversione;
    int rapportiAncoraDaProcessare;
    FileManager fm;




    @FXML
    void handleDragDropped(DragEvent event) {
        boolean b = false;

        Dragboard db = event.getDragboard();
        File file = db.getFiles().get(0);
        b = true;

        setFileInserito(file.getAbsolutePath());
        setCartellaDelFileInserito(file.getParent());

        dropInCorso = false;


        if (b) {
            fileAccettato();

            testo.setText(getFileInserito());

            ImmagineXlsVuoto.setVisible(false);
            ImmagineXlsPiena.setVisible(true);



        }

    }

    @FXML
    void showInfo(MouseEvent event) throws IOException {

        // labelll.setText(SettingController.getAaa().toString());
        main.ShowInfoView();

    }

    @FXML
    void showSettings(MouseEvent event) throws IOException {
        main.ShowSettingView();

    }

    void fileAccettato()
    {

        Tooltip.install(avvioButton, new Tooltip("Avvia il processo"));

        ImmagineXlsVuoto.setVisible(false);
        ImmagineXlsPiena.setVisible(true);



        avvioButton.setDisable(false);

    }


    @FXML
    void handleDragOver2(DragEvent event) {




        ImmagineXlsVuoto.setVisible(false);
        ImmagineXlsPiena.setVisible(true);

    }



    @FXML
    void handleDragOver(DragEvent event) {


        if(event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);



        }



    }

    @FXML
    void handleDragOverNOT(DragEvent event) {

        if(dropInCorso)
        {

            ImmagineXlsVuoto.setVisible(true);
            ImmagineXlsPiena.setVisible(false);
        } else
        {
            ImmagineXlsVuoto.setVisible(false);
            ImmagineXlsPiena.setVisible(true);
        }



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dropInCorso = true;
        percentualeText.setVisible(false);
        processoFinito=false;
        SettingController sett = new SettingController();
        try {
            fm = new FileManager();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    @FXML
    void avviaProcesso(MouseEvent event) throws Exception {
        avvioButton.setDisable(true);
        percentualeText.setVisible(true);
        testoRapporto.setFill(Color.BLACK);
        testoRapporto.setFont(Font.font("System", FontWeight.NORMAL, 14));
        statButton.setDisable(true);


        Instant startTime = Instant.now();

        //creo la cartella "Cartella Rapporti Excel" nella cartella delle risorse in modo poi da eliminarli
        new File(fm.getPercorsoRisorse() + "Cartella Rapporti Excel").mkdir();
        setCartellaDeiRapportiCreatiXLS(fm.getPercorsoRisorse() + "Cartella Rapporti Excel");

        //creo la cartella "Cartella Rapporti PDF" nello stesso percorso in cui è il mattinale
        new File(getCartellaDelFileInserito() + "\\Cartella Rapporti PDF").mkdir();
        setCartellaDeiRapportiCreatiPDF(getCartellaDelFileInserito() + "\\Cartella Rapporti PDF");
        Desktop.getDesktop().open(new File(getCartellaDelFileInserito() + "\\Cartella Rapporti PDF\\"));

        GetDatiFromMattinale gDm = new GetDatiFromMattinale();
        CompilaRapporti compRap = new CompilaRapporti();


        try {
            gDm.metodoCheFaTutto();
            compRap.metodoCheGeneraINomiDeiFile();
        } catch (Exception e) {
            e.printStackTrace();
            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setContentText("Errore nel caricamento del File");
            al.show();
        }





        Task<Void> task1 = new Task<Void>() {

            String nomeRapportProcessato;

            int variabileCiclo=0;

            @Override
            protected Void call() throws Exception {

                rapportiABuonFine=0;



                for (variabileCiclo = 0; variabileCiclo < compRap.getListaNomiFile().size(); variabileCiclo++) {
                    try {
                        rapportiAncoraDaProcessare = compRap.getListaNomiFile().size()-variabileCiclo;

                        inizioConversione = Instant.now();
                        nomeRapportProcessato = compRap.getListaNomiFile().get(variabileCiclo);

                        updateMessage(compRap.getListaNomiFile().get(variabileCiclo));
                        String esitoConvers = compRap.metodoCheCompilaIRapporti(variabileCiclo);
                        if(esitoConvers==null) //caso in cui la conversione non è riuscita
                        {
                            throw new Exception();

                        }
                        else
                        {
                            System.out.println("Esito conversione non dvorebbe essere null, infatti esitoCoversione=null dà : "+esitoConvers);
                            rapportiABuonFine++;

                            Platform.runLater(new Runnable() {
                                @Override public void run() {
                                    elementiCreati.setText(String.valueOf(rapportiABuonFine)+"/"+compRap.getListaNomiFile().size());
                                }
                            });

                        }
                        updateProgress(variabileCiclo+1, compRap.getListaNomiFile().size());
                        fineConversione = Instant.now();


                    } catch (Exception e)
                    {
                        System.out.println("Catch attivato dal Task1 del Controller");
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                al1 = main.generaAlert(Alert.AlertType.INFORMATION, "Task1 Error", "Rapporto non processato "+nomeRapportProcessato, e.getMessage());
                                al1.show();;
                            }
                        });

                        // throw e;



                    } finally {
                        Platform.runLater(new Runnable() {
                            @Override public void run() {

                                //aggiorna tempo stimato
                                Duration durataSingolaConversione = calcolaDurataTempo(inizioConversione, fineConversione);

                                //  tempoStimato.setText(durataDaStampare(durataSingolaConversione.multipliedBy(rapportiAncoraDaProcessare)));
                                tempoRimasto.setText(durataDaStampare(compRap.getDurataConversione().multipliedBy(rapportiAncoraDaProcessare)));

                                //aggiorna numero percentuale progressbar
                                float ciclo = (float)variabileCiclo;
                                float dim = (float)compRap.getListaNomiFile().size();
                                DecimalFormat df = new DecimalFormat("#.##");
                                percentualeText.setText(String.valueOf(df.format((ciclo/dim)*100))+"%");
                            }
                        });

                    }

                }

                return null;
            }
        };


        task1.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldSt, String newSt) {

                testoRapporto.setText(newSt + " ...");


            }
        });



        task1.setOnFailed(evt -> {
            System.err.println("The task failed with the following exception:");
            task1.getException().printStackTrace(System.err);
        });



        task1.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                avvioButton.setDisable(false);
                statButton.setDisable(false);

                Instant finishTime = Instant.now();


                Duration durata = calcolaDurataTempo(startTime, finishTime);

                tempoEffett.setText(durataDaStampare(durata));
                tempoRimasto.setText("00:00:00.00");


                try {
                    //compila Foglio statistiche
                    System.out.println("Rapporti a Buon fine "+rapportiABuonFine);

                    StatisticsMailConverted savStat = new StatisticsMailConverted();
                    savStat.recuperaFoglioStatistiche();
                    int rapportiFalliti = compRap.getListaNomiFile().size()-rapportiABuonFine;
                    savStat.compilaExcelStatistiche(compRap.getListaNomiFile().size(),rapportiFalliti, durata, compRap.ottieniUltimoRapportoID());
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }


                if ( rapportiABuonFine == compRap.getListaNomiFile().size())
                {
                    testoRapporto.setText("PROCESSO COMPLETATO!");
                } else {
                    testoRapporto.setFill(Color.RED);
                    testoRapporto.setFont(Font.font("System", FontWeight.BLACK, 17));
                    testoRapporto.setText("PROCESSO COMPLETATO CON ERRORI, CONTROLLARE QUALI NON HA CONVERTITO");
                }

                try {


                    compRap.chiudiTuttiFile();

                    compRap.eliminaFileExcel(getCartellaDeiRapportiCreatiXLS());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(task1.progressProperty());

        Thread th = new Thread(task1);
        th.setDaemon(true);
        th.start();


    }



    public Duration calcolaDurataTempo(Instant inizio, Instant fine)
    {
        return Duration.between(inizio, fine);
    }

    public String durataDaStampare (Duration durata)
    {
        int durataOre = durata.toHoursPart();
        int durataMin = durata.toMinutesPart();
        int durataSec = durata.toSecondsPart();
        int durataMill = durata.toMillisPart();
        String durataConOre = String.format("%02d:%02d:%02d.%d", durata.toHoursPart(), durata.toMinutesPart(), durata.toSecondsPart(), durata.toMillisPart());
        return durataConOre;
    }

    @FXML
    void showStatistics(MouseEvent event) throws IOException {
        main.ShowStatView();

    }



    public static String getFileInserito() {
        return FileInserito;
    }

    public static void setFileInserito(String fileInserito) {
        FileInserito = fileInserito;
    }

    public static String getCartellaDelFileInserito() {
        return CartellaDelFileInserito;
    }

    public static void setCartellaDelFileInserito(String cartellaDelFileInserito) {
        CartellaDelFileInserito = cartellaDelFileInserito;
    }

    public static String getCartellaDeiRapportiCreatiXLS() {
        return CartellaDeiRapportiCreatiXLS;
    }

    public static void setCartellaDeiRapportiCreatiXLS(String cartellaDeiRapportiCreatiXLS) {
        CartellaDeiRapportiCreatiXLS = cartellaDeiRapportiCreatiXLS;
    }

    public static String getNomRap() {
        return nomRap;
    }

    public static void setNomRap(String nomRap) {
        MainController.nomRap = nomRap;
    }

    public static int getContatore() {
        return contatore;
    }

    public static void setContatore(int contatore) {
        MainController.contatore = contatore;
    }

    public static int getExtTime() {
        return extTime;
    }

    public static void setExtTime(int extTime) {
        MainController.extTime = extTime;
    }

    public static String getCartellaDeiRapportiCreatiPDF() {
        return CartellaDeiRapportiCreatiPDF;
    }

    public static void setCartellaDeiRapportiCreatiPDF(String cartellaDeiRapportiCreatiPDF) {
        CartellaDeiRapportiCreatiPDF = cartellaDeiRapportiCreatiPDF;
    }

    public Text getTestoRapporto() {
        return testoRapporto;
    }

    public int getInizio() {
        return inizio;
    }

    public void setInizio(int inizio) {
        this.inizio = inizio;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }


}
