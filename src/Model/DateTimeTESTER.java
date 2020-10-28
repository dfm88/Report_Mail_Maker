package Model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeTESTER {

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






    public static void main(String[] args) throws InterruptedException {

        DateTimeTESTER ddd = new DateTimeTESTER();

        LocalDateTime startTime = LocalDateTime.now();
        long startTime2 = System.nanoTime();
        System.out.println(startTime);
        System.out.println(startTime.getMinute()+":"+startTime.getSecond()+":"+startTime.getNano()+":---");


       /* for(long i=0; i<185;i++)
        {
            Thread.sleep(1000);
        }*/
        LocalDateTime finishTime = LocalDateTime.now();
        long finishTime2 = System.nanoTime();

        int ss = (finishTime.getNano()-startTime.getNano());


        System.out.println("Tempo conversione = "+ (finishTime.getMinute()-startTime.getMinute())+":"+(finishTime.getSecond()-startTime.getSecond())+":"+(finishTime.getNano()-startTime.getNano()));

        String time1 = LocalDateTime.now().toLocalTime().toString();
        LocalTime t1 = LocalTime.parse(time1);

        /*****inizio OK*****/
        Instant inizio = Instant.now();

        Thread.sleep(1001);

        Instant fine = Instant.now();

        Duration durata = ddd.calcolaDurataTempo(inizio, fine);

        System.out.println("Durata normale "+ddd.durataDaStampare(durata));
        System.out.println("Durata per 60 "+ddd.durataDaStampare(durata.multipliedBy(60)));

        System.out.println("BBB "+String.format("%02d:%02d:%02d.%d", durata.toHoursPart(), durata.toMinutesPart(), durata.toSecondsPart(), durata.toMillisPart()));
        //prova a moltiplicare la durata per una costante
        Duration durataMOltiplicata = durata.multipliedBy(60);
        System.out.println("Durata "+durata+" moltiplicata per 5 = "+durataMOltiplicata);

        /*****fine OK*****/




        String time2 = LocalDateTime.now().toLocalTime().toString();
        LocalTime t2 = LocalTime.parse(time2);
        Duration diff = Duration.between(t1, t2);
        System.out.println(String.format("%02d", diff.toMillis()));






    }
}
