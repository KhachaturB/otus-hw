package ru.calculator;

/*
-Xms256m
-Xmx256m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/heapdump.hprof
-XX:+UseG1GC
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
*/

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SuppressWarnings("java:S106")
public class CalcDemo {
    public static void main(String[] args) {
        var calculationsNumber = 10;
        var average = IntStream.range(0, calculationsNumber)
                .mapToLong(it -> makeCalculation())
                .average();

        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("average spend msec: " + average.orElse(0));
        System.out.println("----------------------------------------------");
    }

    private static long makeCalculation() {
        int counter = 100_000_000;
        var summator = new Summator();
        long startTime = System.currentTimeMillis();

        for (var idx = 0; idx < counter; idx++) {
            var data = new Data(idx);
            summator.calc(data);

            if (idx % 10_000_000 == 0) {
                System.out.println(LocalDateTime.now() + " current idx:" + idx);
            }
        }

        long delta = System.currentTimeMillis() - startTime;
        System.out.println(summator.getPrevValue()); // 99999999
        System.out.println(summator.getPrevPrevValue()); // 99999998
        System.out.println(summator.getSumLastThreeValues()); // 299999994
        System.out.println(summator.getSomeValue()); // 655761157
        System.out.println(summator.getSum()); // 887459712
        System.out.println("spend msec:" + delta + ", sec:" + (delta / 1000));

        assert summator.getPrevValue() == 99999999;
        assert summator.getPrevPrevValue() == 99999998;
        assert summator.getSumLastThreeValues() == 299999994;
        assert summator.getSomeValue() == 655761157;
        assert summator.getSum() == 887459712;

        return delta;
    }
}
