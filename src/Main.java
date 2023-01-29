import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    static ArrayBlockingQueue<String> a = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> b = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> c = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        Runnable completion = () -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    a.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    b.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    c.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Runnable maxA = () -> {
            int intA;
            try {
                intA = max(a,'a');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Максимальное количество символов a = " + intA);
        };
        Runnable maxB = () -> {
            int intB;
            try {
                intB = max(b,'b');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Максимальное количество символов b = " + intB);
        };
        Runnable maxC = () -> {
            int intC;
            try {
                intC = max(c,'c');
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Максимальное количество символов c = " + intC);
        };

        Thread thread = new Thread(completion);
        Thread thread1 = new Thread(maxA);
        Thread thread2 = new Thread(maxB);
        Thread thread3 = new Thread(maxC);
        thread.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();

    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    public static int max (ArrayBlockingQueue arrayBlockingQueue, char meaning) throws InterruptedException {
        int maxSize = 0;
        int t = 0;
        String str;
        for (int i = 0; i < 10_000; i++) {
            str = arrayBlockingQueue.take().toString();
            for (char ch: str.toCharArray()){
                if (ch == meaning){
                    t++;
                }
            }
            if (maxSize < t) {
                maxSize = t;
            }
        }
        return maxSize;
    }
}