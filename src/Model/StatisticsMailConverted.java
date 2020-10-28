package Model;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;

public class StatisticsMailConverted {

    private static FileManager fm;
    private static  Workbook wb;
    private static Sheet sh;
    private static FileOutputStream fos;
    private static FileInputStream fis;

    private String percorsoDelFileStatistiche;

    public StatisticsMailConverted() throws IOException {
        this.fm = new FileManager();
    }

    /*+++++++++++++++    STRUTTURA FILE    +++++++++++++*/

// |DATA|	|ORA|	|IP ADDRESS|	|HOSTNAME|	|NR RAPPORTI CREATI|    	|RAPPORTI FALLITI |       |TEMPO SE CREATI MANUALMENTE|	|TEMPO CON L'APPLICAZIONE|	|TEMPO RISPARMIATO|  ULTIMO ID RAPPORTO
//   0        1          2              3              4                             5                             6                       7                             8                  9



    public void recuperaFoglioStatistiche() throws IOException {

        //devo accedere al file Statistics, dovrebbe essere nella cartella esterna sul PC, ma se non c'è lo copio da Resources:
        setPercorsoDelFileStatistiche(this.percorsoDelFileStatistiche = fm.getPercorsoRisorse()+"Statistiche\\Statistics.xlsx"); //percorso nel PC


        //cerco il file nella cartella esterna
        File fileStatistiche = new File(getPercorsoDelFileStatistiche());

        //se non esiste nelle risorse esterne, lo copio io prendendolo dalla cartella Res del JAR (sarebbe da uploadare di tanto in tanto)
        if (!fileStatistiche.exists())
        {
            InputStream is = fm.getFileFromResourceAsStream("ExcelFiles/Statistics.xlsx"); //percorso nel JAR
            Files.copy(is, Path.of(getPercorsoDelFileStatistiche()));
            is.close();
        }

        //apro il file Statistiche dal computer
        fis = fm.getFileInputStream(getPercorsoDelFileStatistiche());
        wb = fm.getExcelWorkBook(fis);
        sh = fm.getExcelSheet(wb);
    }

    public void compilaExcelStatistiche(int numeroRapportiCreati, int numeroRapportiFalliti, Duration durataTotaleReale, int ultimoIDRapporto) throws IOException, InterruptedException {
        int ultiamRiga = sh.getLastRowNum();
        System.out.println(ultiamRiga);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Date d = new Date(timestamp.getTime());

        System.out.println(d);

        //salva timeStamp
        sh.createRow(ultiamRiga+1).createCell(0).setCellValue(timestamp.toString().substring(0, 10));
        //salva data conversione
        sh.getRow(ultiamRiga+1).createCell(1).setCellValue(timestamp.toString().substring(11, 19));
        //salva IP
        sh.getRow(ultiamRiga+1).createCell(2).setCellValue(getLocalIP());
        //salva local host
        sh.getRow(ultiamRiga+1).createCell(3).setCellValue(getLocalHost());
        //salva num rapporticreati
        sh.getRow(ultiamRiga+1).createCell(4).setCellValue(numeroRapportiCreati);
        //salva quanti rapporti sono falliti
        sh.getRow(ultiamRiga+1).createCell(5).setCellValue(numeroRapportiFalliti);
        //salva durata col vecchio metodo
        Duration tempoColeMetodoVecchio = tempoStimatoSeCreatoManualmente(numeroRapportiCreati);
        sh.getRow(ultiamRiga+1).createCell(6).setCellValue(tempoColeMetodoVecchio.toHours()+":"+tempoColeMetodoVecchio.toMinutesPart()+":"+tempoColeMetodoVecchio.toSecondsPart());
        //salva durata metodo nuovo
        sh.getRow(ultiamRiga+1).createCell(7).setCellValue(durataTotaleReale.toHours()+":"+durataTotaleReale.toMinutesPart()+":"+durataTotaleReale.toSecondsPart());
        //salva durata netta risparmiata
        Duration tempoNettoRisparmiato = tempoRisparmiatoStimato(durataTotaleReale, tempoColeMetodoVecchio);
        sh.getRow(ultiamRiga+1).createCell(8).setCellValue(tempoNettoRisparmiato.toHours()+":"+tempoNettoRisparmiato.toMinutesPart()+":"+tempoNettoRisparmiato.toSecondsPart());
        //salva ultimo ID rapporto
        sh.getRow(ultiamRiga+1).createCell(9).setCellValue(ultimoIDRapporto);

        fos = new FileOutputStream(getPercorsoDelFileStatistiche());
        wb.write(fos);

        //evitare che chieda di salvare il file alla chiusura
        wb.getCreationHelper().createFormulaEvaluator().evaluateAll();

        fos.flush();

        fos.close();
        fis.close();

    }



    public Duration tempoStimatoSeCreatoManualmente(int rapportiCreati) throws InterruptedException {

        Duration d = Duration.ofSeconds(20); //tempo minimo di creazione manuale di un rapporto
        return d.multipliedBy(rapportiCreati);
    }

    public Duration tempoRisparmiatoStimato(Duration durataReale, Duration durataConMetodoVecchio)
    {
        if(durataConMetodoVecchio.minus(durataReale).isNegative())
        {
            return Duration.ofSeconds(0);
        } else
        {
            return durataConMetodoVecchio.minus(durataReale);
        }


    }

    public int lastConversionNumber() throws IOException {
        recuperaFoglioStatistiche();
        int ultiamRiga = this.sh.getLastRowNum();
        return (int)this.sh.getRow(ultiamRiga).getCell(4).getNumericCellValue();

    }

    public int totalConversionsNumber() throws IOException {
        recuperaFoglioStatistiche();
        int ultiamRiga = this.sh.getLastRowNum();
        int totaleRapportiConvertiti = 0;
        for(int i=1; i<ultiamRiga;i++)
        {
            if(sh.getRow(i)!=null)
            {
                totaleRapportiConvertiti += (int)this.sh.getRow(i).getCell(4).getNumericCellValue();
            }

        }

        return  totaleRapportiConvertiti;
    }

    public int lastFailedNumber() throws IOException {
        recuperaFoglioStatistiche();
        int ultiamRiga = this.sh.getLastRowNum();
        return (int)this.sh.getRow(ultiamRiga).getCell(5).getNumericCellValue();

    }

    public int totalFailedNumber() throws IOException {
        recuperaFoglioStatistiche();
        int ultiamRiga = this.sh.getLastRowNum();
        int totaleRapportiConvertiti = 0;
        for(int i=1; i<ultiamRiga;i++)
        {
            if(sh.getRow(i)!=null)
            {
                totaleRapportiConvertiti += (int)this.sh.getRow(i).getCell(5).getNumericCellValue();
            }

        }

        return  totaleRapportiConvertiti;
    }

    public String lastConversionSavedTime() throws IOException {
        recuperaFoglioStatistiche();
        int ultiamRiga = this.sh.getLastRowNum();

        return this.sh.getRow(ultiamRiga).getCell(8).getStringCellValue();

    }

    public String totalConversionsSavedTime() throws IOException, DatatypeConfigurationException {
        recuperaFoglioStatistiche();
        int ultiamRiga = this.sh.getLastRowNum();
        Duration totalDuration = Duration.ZERO;
        String savedTime;
        LocalTime fake = LocalTime.of(00,00,00);
        LocalTime locTi;
        for(int i=1; i<ultiamRiga;i++)
        {
            if(sh.getRow(i)!=null && sh.getRow(i).getCell(8)!=null )
            {
                savedTime = this.sh.getRow(i).getCell(8).getStringCellValue();
                int hours = Integer.parseInt((savedTime.substring(0,savedTime.indexOf(":"))));
                int min = Integer.parseInt(savedTime.substring(savedTime.indexOf(":") + 1, savedTime.lastIndexOf(":")));
                int sec = Integer.parseInt((savedTime.substring(savedTime.lastIndexOf(":")+1,savedTime.length())));
                boolean h = hours<0;
                boolean m = min<0;
                boolean s = sec<0;
                if(hours<0||min<0||sec<0)
                {
                    locTi = LocalTime.of(0, 0, 0);
                } else {
                    locTi = LocalTime.of(hours, min, sec);
                }

                Duration d = Duration.between(fake, locTi);
                totalDuration = totalDuration.plus(d);
            }

        }

        String totalDurationString = totalDuration.toHours()+":"+totalDuration.toMinutesPart()+":"+totalDuration.toSecondsPart();

        return  totalDurationString;



    }





    public String getLocalIP() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();

    }

    public String getLocalHost() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostName();

    }

    public String getPercorsoDelFileStatistiche() {
        return percorsoDelFileStatistiche;
    }

    public void setPercorsoDelFileStatistiche(String percorsoDelFileStatistiche) {
        this.percorsoDelFileStatistiche = percorsoDelFileStatistiche;
    }

    public static void main(String[] args) throws IOException, InterruptedException, DatatypeConfigurationException {
        StatisticsMailConverted ss = new StatisticsMailConverted();
      /*  ss.recuperaFoglioStatistiche();
        ss.compilaExcelStatistiche(5);*/
        System.out.println(ss.lastConversionNumber());
        System.out.println(ss.totalConversionsNumber());;
        System.out.println(ss.lastConversionSavedTime());
        System.out.println("Risparmiato totale  "+ss.totalConversionsSavedTime());


    }

}
