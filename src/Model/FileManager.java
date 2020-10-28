package Model;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.Properties;


public class FileManager {

    private FileInputStream fis;
    private Workbook wb;
    private Sheet sh;
    private FileOutputStream fos;
    private final String percorsoRisorseLavoro = "X:\\Sezione Radio\\Operating.Sez.Radio\\Margoni\\Margoni Personale\\Master\\FaxMailProjMASTER\\";
    private final String percorsoRisorseCasa = "C:\\Users\\user\\Documents\\Risorse_Software\\FaxMailProjectATHome\\";
    private String percorsoRisorse;



    public FileManager() throws IOException {

        //nb il master path lo leggo sempre dal jar, mai esternamnete, quindi se lo devo modificare entro nel JAR

        Properties propInternalInRes = getPropertiesFile("Config/config.properties");
        setPercorsoRisorse(propInternalInRes.getProperty("MasterPath"));



    }

    /**
     * Metodo per creare un nuovo file
     * @param percorso percorso di destinazione nel formato "C:\\Documents"
     * @param nomeFile preceduto nel metodo da \\
     * @param formato da indicare con il . davanti
     */
    public void createFile(String percorso, String nomeFile, String formato)
    {
        try {
            File f = new File(percorso+"\\"+nomeFile+formato);
            if (f.createNewFile()) {
                System.out.println("File created: " + f.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    /**
     * Metodo per scrivere in un file
     * @param percorso
     * @param testoDaScrivere
     */
    public void writeToFile(String percorso, String testoDaScrivere)
    {
        try {
            FileWriter myWriter = new FileWriter(percorso);
            myWriter.write(testoDaScrivere);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Metodo per leggere un file
     * @param percorso
     * @return ritorna la string letta nel ciclo While
     */
    public String readFile(String percorso) throws IOException {
        try {
            FileReader fr = new FileReader(percorso);
            BufferedReader br = new BufferedReader(fr);
            String testoLetto = br.readLine();

            fr.close();
            br.close();

            return testoLetto;

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo per leggere un file nela pacchetto JAR dalla
     * cartella Resources
     * @param fileName
     * @return ritorna L'Inputstream del file letto
     */
    public InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    public Properties getPropertiesFile(String path) throws IOException {
        Properties p = new Properties();
        InputStream is = getFileFromResourceAsStream(path);
        p.load(is);
        is.close();
        return p;

    }

    public Properties getPropertiesFileFromDISK(String path) throws IOException {
        Properties p = new Properties();
        InputStream is = getFileInputStream(path);
        p.load(is);
        is.close();
        return p;

    }

    public void setPropertiesFile(String key, String value, Properties p) throws IOException {
        // Properties p = new Properties();
        OutputStream os = new FileOutputStream("Config\\config.properties");
        p.setProperty(key, value);
        p.store(os, null);

    }

    public void setPropertiesFileOnDISK(String key, String value, Properties p) throws IOException {
        // Properties p = new Properties();
        OutputStream os = new FileOutputStream(percorsoRisorse+"Config\\config.properties");
        p.setProperty(key, value);
        p.store(os, null);

    }

    public Workbook getExcelWorkBook(InputStream is) throws IOException {

        return WorkbookFactory.create(is);

    }

    public void writeExcel(String Percorso, Workbook workbook) throws IOException {

        FileOutputStream fos = new FileOutputStream(Percorso);
        workbook.write(fos);
        fos.flush();
    }

    public void chiudiFileInputStream(FileInputStream fis) throws IOException {
        fis.close();
    }

    public void chiudiFileOutputStream(FileOutputStream fos) throws IOException {
        fos.close();
    }

    public void chiudiWorkbookExcel(Workbook wb) throws IOException {
        wb.getCreationHelper().createFormulaEvaluator().evaluateAll(); //evita che chieda il salvataggio del WorkBook Excel
        wb.close();
    }


    public void eliminaFile(String percorso, File file)
    {
        if(file.exists())
        {
            file.delete();
        }
    }


    public FileInputStream getFileInputStream(String percorso) throws IOException {

        return new FileInputStream(percorso);

    }



    public Workbook getExcelWorkBook(FileInputStream Fis) throws IOException {

        return WorkbookFactory.create(Fis);

    }

    public Sheet getExcelSheet(Workbook Wb) throws IOException {

        String nomePagina = Wb.getSheetName(0); //prende il nome della pagina excel

        return Wb.getSheet(nomePagina); //nome della pagina di excel da considerare

    }

    public String getPercorsoRisorse() {
        return percorsoRisorse;
    }

    public void setPercorsoRisorse(String percorsoRisorse) {
        this.percorsoRisorse = percorsoRisorse;
    }


    public static void main(String[] args) throws IOException {
      /*  File f = new File("C:\\Users\\user\\Documents\\Risorse_Software\\FaxMailProjectATHome\\File_TXT\\aaa.txt");
        if (!f.exists())
        {
            f.createNewFile();
            FileInputStream fis = new FileInputStream(f);

        }*/

      /*  //test Properties File //LEGGO DA JAR RESOURCES
        FileManager fm = new FileManager();
        Properties p = fm.getPropertiesFile("Config/config.properties");
        System.out.println(p.getProperty("MasterPath"));
      //  fm.setPropertiesFile("MasterPath", "ccc", p);

        //SCRIVO ESTERNAMENTE
        FileOutputStream out = new FileOutputStream(fm.percorsoRisorseCasa+"Config\\config.properties");
        p.setProperty("MasterPath", "bbb");
        p.store(out, "questo è un commento");
        System.out.println(p.getProperty("MasterPath"));*/

        FileManager fm = new FileManager();
        String s = fm.getPercorsoRisorse()+"Config\\lastID.properties";






    }
}
