package com.gridnine.testing;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Filtration filter = new Filtration();
        List<Flight> flightList = FlightBuilder.createFlights();

// вывод условий отдельными списками
        System.out.println("Полный список перелетов: " + flightList);
        System.out.println("Исключены перелеты с вылетом до текущего момента: " +
                filter.departureUntilNow(flightList));
        System.out.println("Исключены перелеты с сегментами с датой вылета раньше даты прилета: " +
                filter.arrivalBeforeDeparture(flightList));
        System.out.println("Исключены перелеты с интервалом между сегментами больше 2 часов" +
                filter.groundStayingTime(flightList, 2));
    }
}
