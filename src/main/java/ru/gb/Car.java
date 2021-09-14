package ru.gb;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;
    private static CyclicBarrier cb;
    private static boolean winner = false;

    public Car(Race race, int speed, CyclicBarrier cb) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cb = cb;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            cb.await(); // 1 - ждем пока все будут готовы

            cb.await(); // 2 - ждем пока выведется на печать, что гонка начата

            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }

            printWinner(this);
            cb.await(); // ждем пока все машины финишируют
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void printWinner(Car car) {
        if (!winner) {
            System.out.println(car.getName() + " WIN");
            winner = true;
        }
    }

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }
}