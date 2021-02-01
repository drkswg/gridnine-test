package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Filtration {
    private Set<Flight> filtered = new HashSet<>();
    private LocalDateTime now = LocalDateTime.now();
    private AtomicInteger status = new AtomicInteger();

    // проверка на вылет до текущего момента времени
    public Set<Flight> departureUntilNow(List<Flight> flights) {
        filtered.clear();

        for (Flight flight : flights) {
            for (Segment segment : flight.getSegments()) {
                if (segment.getDepartureDate().isAfter(now)) {
                   filtered.add(flight);
                }
            }
        }

        return filtered;
    }

    // проверка на сегменты с датой прилета раньше даты вылета
    public Set<Flight> arrivalBeforeDeparture(List<Flight> flights) {
        filtered.clear();
        boolean status = false;

        for (Flight flight : flights) {
            for (Segment segment : flight.getSegments()) {
                if (segment.getArrivalDate().isAfter(segment.getDepartureDate())) {
                    filtered.add(flight);
                }
            }
        }

        return filtered;
    }

    // фильтр, отсекающий перелеты с временем на земле (временем между сегментами),
    // большим, чем заданное кол-во часов
    public Set<Flight> groundStayingTime(List<Flight> flights, int hours) {
        filtered.clear();

        for (Flight flight : flights) {
            if (flight.getSegments().size() <= 1) {
                continue;
            } else {
                IntStream.range(0, flight.getSegments().size() - 1).forEach(i -> {
                    if(timeBetweenSegmentsCheck(flight.getSegments().get(i),
                            flight.getSegments().get(i + 1), hours)) {
                        status.set(1);
                    } else {
                        status.set(0);
                    }
                });

                if(status.get() == 0) {
                    filtered.add(flight);
                }
            }
        }

        return filtered;
    }

    // вспомогательный метод для groundStayingTime(List<Flight> flights),
    // вычисляющий разницу во времени между сегментами
    private boolean timeBetweenSegmentsCheck(Segment firstSegment, Segment secondSegment, int hours) {
        Duration timeBetweenSegments = Duration.between(firstSegment.getArrivalDate(),
                secondSegment.getDepartureDate());
        long timeDifference = Math.abs(timeBetweenSegments.toHours());
        boolean status = false;

        if(timeDifference > hours){
            status = true;
        }

        return status;
    }

    // фильтр, отсекающий перелеты с большим количеством сегментов, чем указано
    public Set<Flight> numberOfSegments(List<Flight> flights, int number) {
        filtered.clear();

        for(Flight flight : flights) {
            if(flight.getSegments().size() > number) {
                filtered.add(flight);
            }
        }

        return filtered;
    }

    // фильтр, отсекающий перелеты, протекающие дольше указанного времени (в часах)
    public Set<Flight> totalFlightTime(List<Flight> flights, int hours) {
        filtered.clear();

        for(Flight flight : flights) {
            Duration timeBetweenDepartureAndArrival =
                    Duration.between(flight.getSegments().get(0).getDepartureDate(),
                    flight.getSegments().get(flight.getSegments().size() - 1).getArrivalDate());
            long timeDifference = Math.abs(timeBetweenDepartureAndArrival.toHours());

            if(timeDifference <= hours) {
                filtered.add(flight);
            }
        }

        return filtered;
    }
}
