import java.math.BigInteger;
import java.util.List;

public class Join {
    public static void main(String[] args) throws InterruptedException {
        List<FactorialThread> threads = List.of(
                new FactorialThread(5),
                new FactorialThread(500),
                new FactorialThread(7000),
                new FactorialThread(100000)
        );

        // This makes sure that main thread waits for all the threads to complete their execution.
        // 2000ms means application will wait for 2 seconds for each thread to complete its execution.
        // If thread is not finished in 2 seconds, main thread will continue its execution.
        // This is useful when we don't want to wait indefinitely for a thread to finish.

        // NOTE : The application will not exit until all non-daemon threads are finished.
        // The main application will be in running state if the thread takes lot of time even though we have called join with timeout.


        // To overcome this we can set those threads as daemon threads. OR throw an interrupt from main thread after certain time.

        for (FactorialThread thread : threads) {
            thread.join(2000);
        }

        for (FactorialThread thread : threads) {
            thread.start();
        }


        for (FactorialThread thread : threads) {
           if (thread.isFinished()){
               System.out.println("Factorial of is " + thread.getResult());
           }
           else{
                System.out.println("Thread is still not finished");
           }
        }

    }


}

class FactorialThread extends Thread{
    private int number;
    private long result;
    private boolean isFinished = false;

    public FactorialThread(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        result = factorial(number);
        isFinished = true;
    }

    private long factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public boolean isFinished() {
        return isFinished;
    }

    public long getResult() {
        return result;
    }
}


class ComplexCalculation {
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
        BigInteger result = BigInteger.ZERO;
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */

        PowerCalculatingThread t1 = new PowerCalculatingThread(base1 , power1);
        PowerCalculatingThread t2 = new PowerCalculatingThread(base2 , power2);

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        t1.start();
        t2.start();

        result = result.add(t1.getResult());



        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            while (power.signum() == 1){
                result = result.multiply(base);
                power = power.subtract(BigInteger.ONE);
            }
        }

        public BigInteger getResult() { return result; }
    }
}