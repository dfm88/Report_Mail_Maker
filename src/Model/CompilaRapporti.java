package Model;

import Controller.MainController;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Properties;

public class CompilaRapporti
{
    private static Workbook wb;
    private static Sheet sh;
    private static FileInputStream fis;
    private static FileOutputStream fos;
    private static Row row;
    private static Cell cell;
    private static Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private static GetDatiFromMattinale getDatiFromMattinale= new GetDatiFromMattinale();
    private static ArrayList<String> listaNomiFile;
    private int IDRapporto;
    private String percorsoMaster;
    final String estensioneExcel = ".xlsx";
    final String estensionePDF = ".pdf";
    // private static FileManager fll;

    String stringaDaTornare;
    Boolean conversioneRiuscita;


    Instant inizioConversione;
    Instant fineConversione;
    Duration durataConversione;

    private static FileManager fm;

    public CompilaRapporti() throws IOException {
        fm = new FileManager();
    }


    public void metodoCheGeneraINomiDeiFile() throws Exception {

/*
    //*****vecchio*******
        fll = new FileManager();

        File f = new File("");

        setPercorsoMaster(f.getAbsolutePath()+"\\PROFaxMailProj\\src\\FileMasterRapportiExcel\\MASTERRapportiVigilanza.xlsx");


        //apertura del file RAPPORTIVIGILANZA
        fis = fll.getFileInputStream(getPercorsoMaster());
        wb = fll.getExcelWorkBook(fis);
        sh = fll.getExcelSheet(wb);

        //Recupero l'ultimo IDRapporto Salvato 80761
        int id = (int) sh.getRow(11).getCell(5).getNumericCellValue();
        setIDRapporto(id);
*/
        listaNomiFile = new ArrayList<String>();

        for (int i = 0; i<getDatiFromMattinale.getListaRagioniSociali().size(); i++) {
            //	sh = wb.cloneSheet(0);

            //****vecchio**********/
            //         String nomePag = wb.getSheetName(0); //nome della pagina di excel da considerare
            //           sh = wb.getSheet(nomePag);
            //      File masterFile = new File(MainController.getFileInserito());


            String ragioneSoc = getDatiFromMattinale.getListaRagioniSociali().get(i);
            String orario = getDatiFromMattinale.getListaDateEOra().get(i).toLocaleString();


            String nomeFile = ragioneSoc;


            nomeFile = nomeFile.substring(0, nomeFile.indexOf("\n"));


            nomeFile = nomeFile.replaceAll("\\:", " ");
            nomeFile = nomeFile.replaceAll("\\?", " ");
            nomeFile = nomeFile.replaceAll("\\^", " ");
            nomeFile = nomeFile.replaceAll("\\\\", " ");
            nomeFile = nomeFile.replaceAll("\"", " ");
            nomeFile = nomeFile.replaceAll("\\/", " ");
            nomeFile = nomeFile.replaceAll("\\*", " ");
            nomeFile = nomeFile.replaceAll("\\’", "'");
            nomeFile = nomeFile.replaceAll("\\–", "-");
            nomeFile = nomeFile.replaceAll("\\|", " ");
            nomeFile = nomeFile.replaceAll("\n", " ").replace("\r", "");
            orario = orario.replaceAll("\\:", " ");

            String nomePagineOrario = nomeFile + " " + orario;





            listaNomiFile.add(nomePagineOrario + estensioneExcel);


/*           *****vecchio*****


            // if(!nuovoFile.exists())

            //  Files.copy(masterFile.toPath(), nuovoFile.toPath());  ***A COSA SERVE? BOOO

           /* fis.close();

            fos = new FileOutputStream(getPercorsoMaster());
            wb.write(fos);

            fos.close();
  */

        }


    }

    /**
     * Metodo che crea i file excel compilati e all'interno chiama il metodo per convertirli
     * @param i numero file da considerare nel ciclo
     * @return ritorna il nome del file se tutto è andato a buon fine, null se c'è stato un errore
     * @throws Exception
     */
    public String metodoCheCompilaIRapporti(int i) throws Exception
    {

        //devo accedere al file MasterRapportiVigilanza, dovrebbe essere nella cartella esterna sul PC, ma se non c'è lo copio da Resources
        percorsoMaster = fm.getPercorsoRisorse()+"Util_Excel_Files\\MASTERRapportiVigilanza.xlsx"; //percorso nel PC

        setPercorsoMaster(percorsoMaster);

        //cerco il file nella cartella esterna
        File masterRapporti = new File(percorsoMaster);

        //se non esiste nelle risorse esterne, lo copio io prendendolo dalla cartella Res del JAR
        if (!masterRapporti.exists())
        {
            InputStream is = fm.getFileFromResourceAsStream("ExcelFiles/MASTERRapportiVigilanza.xlsx"); //percorso nel JAR
            Files.copy(is, Path.of(percorsoMaster));
            is.close();
        }

        //apro il file Rapporto Vigilanza
        fis = fm.getFileInputStream(percorsoMaster);
        wb = fm.getExcelWorkBook(fis);
        sh = fm.getExcelSheet(wb);


        //    for(int i = 0; i< getDatiFromMattinale.getListaRagioniSociali().size();i++) {

        //salvo e incremento l'IDRapporto
        //Recupero l'ultimo IDRapporto Salvato 80761 - solo al primo ciclo lo legge dal file
        if(i==0)
        {
            setIDRapporto(ottieniUltimoRapportoID());
        }

        Cell cellIDRapporto = sh.getRow(11).getCell(5);
        setIDRapporto(getIDRapporto() + 1);
        cellIDRapporto.setCellValue(getIDRapporto());


        //salvo l'ID del rapporto sul FILe solo all'ultimo ciclo, per evitare di scriverlo ad ogni iterazione
        if(listaNomiFile.size()==i+1)
        {
            salvaUltimoRapportoID(getIDRapporto());
        }


        //salvo la ragione sociale
        Cell cellRagioneSociale = sh.getRow(4).getCell(8);

        cellRagioneSociale.setCellValue(getDatiFromMattinale.getListaRagioniSociali().get(i));


        //salvo data e ora dell'evento
        Cell cellDataEOra = sh.getRow(17).getCell(4);

        cellDataEOra.setCellValue(getDatiFromMattinale.getListaDateEOra().get(i).toLocaleString());

        //faccio il time stamp della creazione del file
        Cell cellTimeStamp = sh.getRow(8).getCell(1);

        cellTimeStamp.setCellValue(timestamp.toString().substring(0, 19));//per tagliare i millesimi

        //salvo il tipo di segnalazione
        Cell cellTipoSegnalazione = sh.getRow(18).getCell(2);

        cellTipoSegnalazione.setCellValue(getDatiFromMattinale.getListaTipoAllarme().get(i));


        //salvo l'esito
        Cell cellEsito = sh.getRow(22).getCell(2);

        cellEsito.setCellValue(getDatiFromMattinale.getListaEsiti().get(i));

        String percorsoRapportExcelCreato = MainController.getCartellaDeiRapportiCreatiXLS() + "\\" + listaNomiFile.get(i);



        fos = new FileOutputStream(percorsoRapportExcelCreato);
        wb.write(fos);

        //evitare che chieda di salvare il file alla chiusura

        fos.flush();


        fos.close();

        fis.close();


        conversioneRiuscita = metodoCheConverte(listaNomiFile.get(i), percorsoRapportExcelCreato);

        if(!conversioneRiuscita)
        {
            stringaDaTornare = null;
        } else
        {
           stringaDaTornare = listaNomiFile.get(i);


        }

        return  stringaDaTornare;
    }

    public boolean metodoCheConverte(String nomeFiledaConvertire, String percorsoRapportExcelCreato) throws IOException {

        String nomeFileSenzaXLS = nomeFiledaConvertire.replaceAll(".xlsx", "");
        // xls2PDF.writeNewScript(percorsoRapportExcelCreato, getPercorsoMaster()+"\\Pdf_WHATAFUCK\\"+listaNomiFile.get(i));
        //JAVAFX

        inizioConversione = Instant.now();

        conversioneRiuscita = (XlsToPDF.writeNewScript(percorsoRapportExcelCreato, MainController.getCartellaDeiRapportiCreatiPDF() + "\\" + nomeFileSenzaXLS + estensionePDF));

        fineConversione = Instant.now();

        setDurataConversione(Duration.between(inizioConversione, fineConversione));

        return  conversioneRiuscita;

    }

    public int ottieniUltimoRapportoID() throws IOException {

        //recupero il properties in cui salvo l'ultimo ID rapporto per scriverlo nel file. Se il txt non esiste nella cartella esterna, lo copio dal JAR
        //anche se copiandolo dal JAR non sarà al valore giusto - parte da 80981
        String percorsoProperties = fm.getPercorsoRisorse()+"Config\\config.properties"; //percorso nel PC

        //cerco il file nella cartella esterna
        File masterIDRap = new File(percorsoProperties);

        //se non esiste nelle risorse esterne, lo copio io prendendolo dalla cartella Res del JAR (anche se non sarà aggiornato
        if (!masterIDRap.exists())
        {
            InputStream is = fm.getFileFromResourceAsStream("Config\\config.properties"); //percorso nel JAR
            Files.copy(is, Path.of(percorsoProperties));
            is.close();
        }

        //leggo l'id dal file esterno
        Properties p = fm.getPropertiesFileFromDISK(percorsoProperties);
        String idLetto = p.getProperty("UltimoIDRapporto");
        setIDRapporto(Integer.parseInt(idLetto));

        return getIDRapporto();

    }

    public void salvaUltimoRapportoID(int IDultimo) throws IOException {


        //recupero il properties in cui salvo l'ultimo ID rapporto per scriverlo nel file. Se il txt non esiste nella cartella esterna, lo copio dal JAR
        //anche se copiandolo dal JAR non sarà al valore giusto - parte da 80981
        String percorsoProperties = fm.getPercorsoRisorse()+"Config\\config.properties"; //percorso nel PC

        //cerco il file nella cartella esterna
        File masterIDRap = new File(percorsoProperties);

        //se non esiste nelle risorse esterne, lo copio io prendendolo dalla cartella Res del JAR (anche se non sarà aggiornato
        if (!masterIDRap.exists())
        {
            InputStream is = fm.getFileFromResourceAsStream("Config\\config.properties"); //percorso nel JAR
            Files.copy(is, Path.of(percorsoProperties));
            is.close();
        }

        //leggo l'id dal file esterno
        Properties p = fm.getPropertiesFileFromDISK(percorsoProperties);
        //scrivo l'id sul file
        fm.setPropertiesFileOnDISK("UltimoIDRapporto", String.valueOf(IDultimo), p);


    }

    public void chiudiTuttiFile() throws IOException, IOException {
        //evitare che chieda di salvare il file alla chiusura
        wb.getCreationHelper().createFormulaEvaluator().evaluateAll();
        wb.close();
        fos.close();	//********** ATTENZIONE NUOVI PER PROVARE CHIUSURA FILE *************
        fis.close();
        // System.exit(0);
    }



    public void eliminaFileExcel(String percorsoDeiFiledaEliminare)
    {
        for (int i=0 ; i<listaNomiFile.size(); i++)
        {
            File ff = new File(percorsoDeiFiledaEliminare + "\\"+listaNomiFile.get(i));
            ff.delete();
        }
        // System.exit(0);
    }





    public static void main(String[] args) throws Exception
    {
        //  getDatiFromMattinale.metodoCheFaTutto();

        CompilaRapporti cmprap = new CompilaRapporti();


        //    cmprap.metodoCheGeneraINomiDeiFile();

        // cmprap.metodoCheCompilaIRapporti();




        //  cmprap.metodoCheAttivaLaMacro();

        //   cmprap.chiudiTuttiFile();

        //  cmprap.SCriptMOthaFucka();

        cmprap.eliminaFileExcel("C:\\Users\\user\\Documents\\Risorse_Software\\FaxMailProjectATHome\\Cartella Rapporti Excel");

    }



    public String getPercorsoMaster() {
        return percorsoMaster;
    }

    public void setPercorsoMaster(String percorsoMaster) {
        this.percorsoMaster = percorsoMaster;
    }

    public static Workbook getWb() {
        return wb;
    }

    public static void setWb(Workbook wb) {
        CompilaRapporti.wb = wb;
    }

    public static Sheet getSh() {
        return sh;
    }

    public static void setSh(Sheet sh) {
        CompilaRapporti.sh = sh;
    }

    public static FileInputStream getFis() {
        return fis;
    }

    public static void setFis(FileInputStream fis) {
        CompilaRapporti.fis = fis;
    }

    public static FileOutputStream getFos() {
        return fos;
    }

    public static void setFos(FileOutputStream fos) {
        CompilaRapporti.fos = fos;
    }

    public static Row getRow() {
        return row;
    }

    public static void setRow(Row row) {
        CompilaRapporti.row = row;
    }

    public static Cell getCell() {
        return cell;
    }

    public static void setCell(Cell cell) {
        CompilaRapporti.cell = cell;
    }

    public static Timestamp getTimestamp() {
        return timestamp;
    }

    public static void setTimestamp(Timestamp timestamp) {
        CompilaRapporti.timestamp = timestamp;
    }

    public static GetDatiFromMattinale getGetDatiFromMattinale() {
        return getDatiFromMattinale;
    }

    public static void setGetDatiFromMattinale(GetDatiFromMattinale getDatiFromMattinale) {
        CompilaRapporti.getDatiFromMattinale = getDatiFromMattinale;
    }

    public static ArrayList<String> getListaNomiFile() {
        return listaNomiFile;
    }

    public static void setListaNomiFile(ArrayList<String> listaNomiFile) {
        CompilaRapporti.listaNomiFile = listaNomiFile;
    }

    public int getIDRapporto() {
        return IDRapporto;
    }

    public void setIDRapporto(int IDRapporto) {
        this.IDRapporto = IDRapporto;
    }

    public Instant getInizioConversione() {
        return inizioConversione;
    }

    public void setInizioConversione(Instant inizioConversione) {
        this.inizioConversione = inizioConversione;
    }

    public Instant getFineConversione() {
        return fineConversione;
    }

    public void setFineConversione(Instant fineConversione) {
        this.fineConversione = fineConversione;
    }

    public Duration getDurataConversione() {
        return durataConversione;
    }

    public void setDurataConversione(Duration durataConversione) {
        this.durataConversione = durataConversione;
    }
}
