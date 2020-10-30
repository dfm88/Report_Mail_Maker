package Model;

import Controller.MainController;
import org.apache.poi.ss.usermodel.*;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GetDatiFromMattinale {

    private static Workbook wb;
    private static Sheet sh;
    private static FileInputStream fis;
    private static FileOutputStream fos;
    private static Row row;
    private static Cell cell;
    private static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.ITALY);

    private static String RagioneSociale, TipoSegnalazione, esitoEvento;
    private static Date dataEOra;
    private static String dataEOraString;
    private static int numDiRighe;
    private static ArrayList<String> listaRagioniSociali;
    private static ArrayList<Date> listaDateEOra;
    private static ArrayList<String> listaTipoAllarme;
    private static ArrayList<String> listaEsiti;
    private static String percorsoMattinale;
    private ArrayList<Row> eventiTotali;
    private String percorsoAbsolute;  // C:-Users-user-Documents-PROFaxMailProj
    private static FileManager fl;


    public void metodoCheSetta() throws IOException {

        fl = new FileManager();
        setPercorsoAbsolute(MainController.getCartellaDelFileInserito()); // C:-Users-user-Documents-PROFaxMailProj

        System.out.println();

        fis = fl.getFileInputStream(MainController.getFileInserito());

        wb = fl.getExcelWorkBook(fis);

        sh = fl.getExcelSheet(wb);

        String nomePagina = wb.getSheetName(0); //prende il nome della pagina excel

        sh = wb.getSheet(nomePagina); //nome della pagina di excel da considerare

        //prendo il numero di righe
        numDiRighe = sh.getLastRowNum();

    }

    public void metodoRagioneSociale()
    {
        listaRagioniSociali = new ArrayList<String>();

        //salvo la ragione sociale
        for (int i=15; i<numDiRighe; i++ )
        {
            int quantiDoppioni = 1;

            try {
                if (sh.getRow(i) != null)  //nullity test
                {

                    quantiDoppioni = 1;

                    RagioneSociale = sh.getRow(i).getCell(4).toString();

                    while (RagioneSociale.isEmpty()) //se ci sono più interventi di uno stesso cliente
                    {


                        RagioneSociale = sh.getRow(i - 1).getCell(4).toString();

                        quantiDoppioni++;

                        if (quantiDoppioni > 2) {
                            RagioneSociale = sh.getRow(i - (quantiDoppioni - 1)).getCell(4).toString();
                        }

                    }

                    listaRagioniSociali.add(i - 15, RagioneSociale);

                }
            } catch(NullPointerException e)
            {

            }




        }



    }

    public void metodoDataEOra()
    {
        listaDateEOra = new ArrayList<Date>();

        try {
            //salvo data e ora
            for (int i = 15; i < numDiRighe; i++) {
                if (sh.getRow(i) != null)  //nullity test
                {
                    dataEOra = sh.getRow(i).getCell(7).getDateCellValue();
                    dataEOraString = sh.getRow(i).getCell(7).getDateCellValue().toLocaleString();

                    listaDateEOra.add(i - 15, dataEOra);
                }
            }
        } catch(NullPointerException e){

        }


    }

    public void metodoTipoAllarme()
    {
        listaTipoAllarme = new ArrayList<String>();

        try
        {
            //salvo il tipo di allarme
            for(int i=15 ; i< numDiRighe; i++)
            {
                if(sh.getRow(i)!=null)  //nullity test
                {
                    TipoSegnalazione = sh.getRow(i).getCell(8).toString();

                    listaTipoAllarme.add(i - 15, TipoSegnalazione);
                }
            }
        } catch(NullPointerException e){

        }
    }

    public void metodoEsito()
    {
        listaEsiti = new ArrayList<String>();

        try
        {
            //salvo l'esito
            for(int i=15 ; i< numDiRighe; i++)
            {
                if(sh.getRow(i)!=null)  //nullity test
                {
                    esitoEvento = sh.getRow(i).getCell(10).toString();

                    listaEsiti.add(i - 15, esitoEvento);
                }
            }
        } catch(NullPointerException e){

        }
    }

    public void metodoCheFaTutto() throws Exception
    {

        metodoCheSetta();

        metodoRagioneSociale();
        metodoDataEOra();
        metodoTipoAllarme();
        metodoEsito();
    }


    public static Workbook getWb() {
        return wb;
    }

    public static void setWb(Workbook wb) {
        GetDatiFromMattinale.wb = wb;
    }

    public static Sheet getSh() {
        return sh;
    }

    public static void setSh(Sheet sh) {
        GetDatiFromMattinale.sh = sh;
    }

    public static FileInputStream getFis() {
        return fis;
    }

    public static void setFis(FileInputStream fis) {
        GetDatiFromMattinale.fis = fis;
    }

    public static FileOutputStream getFos() {
        return fos;
    }

    public static void setFos(FileOutputStream fos) {
        GetDatiFromMattinale.fos = fos;
    }

    public static Row getRow() {
        return row;
    }

    public static void setRow(Row row) {
        GetDatiFromMattinale.row = row;
    }

    public static Cell getCell() {
        return cell;
    }

    public static void setCell(Cell cell) {
        GetDatiFromMattinale.cell = cell;
    }

    public static DateFormat getDateFormat() {
        return dateFormat;
    }

    public static void setDateFormat(DateFormat dateFormat) {
        GetDatiFromMattinale.dateFormat = dateFormat;
    }

    public static String getRagioneSociale() {
        return RagioneSociale;
    }

    public static void setRagioneSociale(String ragioneSociale) {
        RagioneSociale = ragioneSociale;
    }

    public static String getTipoSegnalazione() {
        return TipoSegnalazione;
    }

    public static void setTipoSegnalazione(String tipoSegnalazione) {
        TipoSegnalazione = tipoSegnalazione;
    }

    public static String getEsitoEvento() {
        return esitoEvento;
    }

    public static void setEsitoEvento(String esitoEvento) {
        GetDatiFromMattinale.esitoEvento = esitoEvento;
    }

    public static Date getDataEOra() {
        return dataEOra;
    }

    public static void setDataEOra(Date dataEOra) {
        GetDatiFromMattinale.dataEOra = dataEOra;
    }

    public static String getDataEOraString() {
        return dataEOraString;
    }

    public static void setDataEOraString(String dataEOraString) {
        GetDatiFromMattinale.dataEOraString = dataEOraString;
    }

    public static int getNumDiRighe() {
        return numDiRighe;
    }

    public static void setNumDiRighe(int numDiRighe) {
        GetDatiFromMattinale.numDiRighe = numDiRighe;
    }

    public static ArrayList<String> getListaRagioniSociali() {
        return listaRagioniSociali;
    }

    public static void setListaRagioniSociali(ArrayList<String> listaRagioniSociali) {
        GetDatiFromMattinale.listaRagioniSociali = listaRagioniSociali;
    }

    public static ArrayList<Date> getListaDateEOra() {
        return listaDateEOra;
    }

    public static void setListaDateEOra(ArrayList<Date> listaDateEOra) {
        GetDatiFromMattinale.listaDateEOra = listaDateEOra;
    }

    public static ArrayList<String> getListaTipoAllarme() {
        return listaTipoAllarme;
    }

    public static void setListaTipoAllarme(ArrayList<String> listaTipoAllarme) {
        GetDatiFromMattinale.listaTipoAllarme = listaTipoAllarme;
    }

    public static ArrayList<String> getListaEsiti() {
        return listaEsiti;
    }

    public static void setListaEsiti(ArrayList<String> listaEsiti) {
        GetDatiFromMattinale.listaEsiti = listaEsiti;
    }

    public static String getPercorsoMattinale() {
        return percorsoMattinale;
    }

    public static void setPercorsoMattinale(String percorsoMattinale) {
        GetDatiFromMattinale.percorsoMattinale = percorsoMattinale;
    }

    public ArrayList<Row> getEventiTotali() {
        return eventiTotali;
    }

    public void setEventiTotali(ArrayList<Row> eventiTotali) {
        this.eventiTotali = eventiTotali;
    }

    public String getPercorsoAbsolute() {
        return percorsoAbsolute;
    }

    public void setPercorsoAbsolute(String abosolutePath) {
        this.percorsoAbsolute = abosolutePath;
    }

    public static void main(String[] args) throws Exception
    {
        GetDatiFromMattinale eFm = new GetDatiFromMattinale();
        eFm.metodoCheFaTutto();





        eFm.getDataEOra();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


    }


}
