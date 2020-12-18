package Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XlsToPDF {
    private String percorsoMacro;
    private String percorsoOrigineExcel;
    private String percorsoDestinazionePDF;

    private static FileManager fm;


    /**
     * Metodo che ritorna false se e solo se lo script non va a buon fine
     * @param percorsoOrigineExcel
     * @param percorsoDestinazionePDF
     * @return
     * @throws IOException
     */
    public static boolean writeNewScript(String percorsoOrigineExcel, String percorsoDestinazionePDF) throws IOException
    {
        fm = new FileManager();

        String percorsoMacro = fm.getPercorsoRisorse()+"Script\\macro.vbs"; //percorso nel PC
        File macro = new File("");
        System.out.println(percorsoMacro);





        macro = new File(percorsoMacro);
        if(!macro.exists())
        {
            macro.createNewFile();
        }
        //necessarie virgolette quando leggo il percorso dal file CONFIG, altrimenti lo script dà errore negli spazi bianchi del percorso file
        percorsoMacro = ("\""+percorsoMacro+"\"");
        System.out.println(percorsoMacro);

        FileWriter fw = new FileWriter(macro, false);

        BufferedWriter bw = new BufferedWriter(fw);

        Boolean esitoConversione = true;


        bw.write(compilaScript(percorsoOrigineExcel, percorsoDestinazionePDF));
        bw.close();

        try
        {
            Process p = Runtime.getRuntime().exec( "wscript "+percorsoMacro );
            System.out.println("Waiting for batch file ...");
            p.waitFor();
            if(p.exitValue()==0) //errore nella conversione
            {
                System.out.println("lo script ha ritornato '"+p.exitValue()+"' quindi ritorno false");
                esitoConversione = false;
            } else
            {
                System.out.println("lo script ha ritornato '"+p.exitValue()+"' quindi ritorno true");
                esitoConversione = true;
            }

        }   catch(IOException | InterruptedException e )
        {
            esitoConversione = false;

        }


        return esitoConversione;

    }



    public static String compilaScript(String fielXLS, String filePdf)
    {

        System.out.println("\"");


        return 	"Dim Excel\r\n" +
                "Dim ExcelDoc\r\n" +
                "\r\n" +
                "Set Excel = CreateObject(\"Excel.Application\")\r\n" +
                "\r\n" +
                "'Open the Document\r\n" +
                "Set ExcelDoc = Excel.Workbooks.open(\""+fielXLS+"\")\r\n" +
                "Excel.ActiveSheet.ExportAsFixedFormat 0, \""+filePdf+"\" ,0, 1, 0,,,0\r\n" +
                "Excel.ActiveWorkbook.Close\r\n" + //se non va ritorna 0
                "Excel.Application.Quit\n" +
                "WScript.quit 1";
    }





    public String getPercorsoOrigineExcel() {
        return percorsoOrigineExcel;
    }

    public void setPercorsoOrigineExcel(String percorsoOrigineExcel) {
        this.percorsoOrigineExcel = percorsoOrigineExcel;
    }

    public String getPercorsoDestinazionePDF() {
        return percorsoDestinazionePDF;
    }

    public void setPercorsoDestinazionePDF(String percorsoDestinazionePDF) {
        this.percorsoDestinazionePDF = percorsoDestinazionePDF;
    }

    public static void main(String[] args) throws IOException {
        Boolean b = null;
         b = XlsToPDF.writeNewScript("C:\\Users\\user\\Documents\\PerAnagrafica DB.xlsx",
                  "C:\\Users\\user\\Documents\\PerAnagrafica DB.pdf");


    }


}
