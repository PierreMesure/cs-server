package pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class BusTime implements Serializable {

    private String line;

    private String destinationA;

    private String destinationB;

    private List<Date> busTimesA;

    private List<Date> busTimesB;

    public static List<BusTime> createFromResponse(Response response) {
        List<Transport> buses = response.getResponseData().getBuses();
        List<String> lines = buses.stream().map(Transport  ::  getLineNumber).distinct().collect(Collectors.toList());

        List<BusTime> busTimes = new ArrayList<>();

        for (String line : lines) {
            List<Transport> lineBuses =
                    buses.stream().filter(b -> line.equals(b.getLineNumber())).collect(Collectors.toList());

            List<String> destinations =
                    lineBuses.stream().map(Transport::getDestination).distinct().collect(Collectors.toList());


            String destinationA = destinations.get(0);
            String destinationB = destinations.size() > 1 ? destinations.get(1) : null;


            List<Date> busTimesA = lineBuses.stream().filter(b -> b.getDestination().equals(destinationA))
                    .map(Transport::getExpectedDateTime).collect(Collectors.toList());

            List<Date> busTimesB = lineBuses.stream().filter(b -> b.getDestination().equals(destinationB))
                    .map(Transport::getExpectedDateTime).collect(Collectors.toList());


            busTimes.add(new BusTime(line, destinationA, destinationB, busTimesA, busTimesB));
        }

        return busTimes;
    }
}
