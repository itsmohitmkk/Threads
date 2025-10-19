import java.util.Random;

public class ThreadCreation {

    public static void main(String[] args) {
        Random random = new Random(10000);
        Vault vault = new Vault(random.nextInt(10000));

        HackerThread ascendingHacker = new AscendingHackerThread(vault);

        HackerThread descendingHacker = new DescendingHackerThread(vault);

        ascendingHacker.start();
        descendingHacker.start();
        new PoliceThread().start();



    }
}

class Vault {
    private int password;
    Vault(int password){
        this.password = password;
    }

    public boolean tryPassword(int guess) throws InterruptedException {
        Thread.sleep(5000);
        return this.password == guess;
    }
}

abstract class HackerThread extends Thread {
    protected Vault vault;

    public HackerThread(Vault vault) {
        this.vault = vault;
        this.setName(this.getClass().getSimpleName());
        this.setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void start() {
        System.out.println("Following class is starting :" + this.getName()); // Some action needs to be done.
        super.start(); // parent start as it is.
    }
}

class AscendingHackerThread extends HackerThread {
    public AscendingHackerThread(Vault vault) {
        super(vault);
    }

    @Override
    public void run() {
        for (int guess = 0; guess <= 9999; guess++) {
            try {
                if (vault.tryPassword(guess)) {
                    System.out.println(this.getName() + " guessed the password " + guess);
                    System.exit(0);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class DescendingHackerThread extends HackerThread {
    public DescendingHackerThread(Vault vault) {
        super(vault);
    }

    @Override
    public void run() {
        for (int guess = 9999; guess >= 0; guess--) {
            try {
                if (vault.tryPassword(guess)) {
                    System.out.println(this.getName() + " guessed the password " + guess);
                    System.exit(0);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class PoliceThread extends Thread {
    @Override
    public void run() {
        for (int i = 0 ; i < 10 ; i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Police time " + i);
        }

        System.out.println("Police are coming!");
        System.exit(0);
    }
}

