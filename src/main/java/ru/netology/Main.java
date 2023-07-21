package ru.netology;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    private static final BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    // Генератор текстов
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void main(String[] args) {

        Thread generatorThread = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queueA.put(text);
                    queueB.put(text);
                    queueC.put(text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                queueA.put("END");
                queueB.put("END");
                queueC.put("END");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        generatorThread.start();

        Thread threadA = new Thread(() -> {
            try {
                int maxCountA = 0;
                String maxTextA = "";
                while (true) {
                    String text = queueA.take();
                    if (text.equals("END")) {
                        break;
                    }
                    int count = countCharacter(text, 'a');
                    if (count > maxCountA) {
                        maxCountA = count;
                        maxTextA = text;
                    }
                }
                System.out.println("Самое большое количество повторений 'a' " + maxCountA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                int maxCountB = 0;
                String maxTextB = "";
                ;
                while (true) {
                    String text = queueB.take();
                    if (text.equals("END")) {
                        break;
                    }
                    int count = countCharacter(text, 'b');
                    if (count > maxCountB) {
                        maxCountB = count;
                        maxTextB = text;
                    }
                }
                System.out.println("Самое большое количество повторений 'b' " + maxCountB);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread threadC = new Thread(() -> {
            try {
                int maxCountC = 0;
                String maxTextC = "";
                ;
                while (true) {
                    String text = queueC.take();
                    if (text.equals("END")) {
                        break;
                    }
                    int count = countCharacter(text, 'c');
                    if (count > maxCountC) {
                        maxCountC = count;
                        maxTextC = text;
                    }
                }
                System.out.println("Самое большое количество повторений 'c' " + maxCountC);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadA.start();
        threadB.start();
        threadC.start();

        try {
            generatorThread.join();
            threadA.join();
            threadB.join();
            threadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int countCharacter(String text, char character) {
        int count = 0;
        for (char c : text.toCharArray()) {
            if (c == character) {
                count++;
            }
        }
        return count;
    }


}
