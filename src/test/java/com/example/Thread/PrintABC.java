package com.example.Thread;



import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: 走路呼呼带风
 * Date: 2018/11/22
 * Time: 18:31
 */
public class PrintABC {



    @Test
    public void ss() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Print print = new Print();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        executorService.submit(()->{
            for (int i = 0; i <3 ; i++) {
                print.printA();
            }
            countDownLatch.countDown();
        });
        executorService.submit(()->{
            for (int i = 0; i <3 ; i++) {
                print.printB();
            }
            countDownLatch.countDown();
        });
        executorService.submit(()->{
            for (int i = 0; i <3 ; i++) {
                print.printC();
            }
            countDownLatch.countDown();
        });
        countDownLatch.await();
        executorService.shutdown();

    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Print print = new Print();
        executorService.submit(()->{
            for (int i = 0; i <3 ; i++) {
                print.printA();
            }
        });
        executorService.submit(()->{
            for (int i = 0; i <3 ; i++) {
                print.printB();
            }
        });
        executorService.submit(()->{
            for (int i = 0; i <3 ; i++) {
                print.printC();
            }
        });
        executorService.shutdown();
    }



    static class Print{

        int  num = 1;
        private Lock lock = new ReentrantLock();
        private Condition condition1 = lock.newCondition();
        private Condition condition2 = lock.newCondition();
        private Condition condition3= lock.newCondition();

        private void printA(){
            lock.lock();
            if (num != 1){
                try {
                    condition1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("A");
            num = 2;
            condition2.signal();
            lock.unlock();
        }

        private void printB(){
            lock.lock();
            if (num != 2){
                try {
                    condition2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("B");
            num = 3;
            condition3.signal();
            lock.unlock();
        }

        private void printC(){
            lock.lock();
            if (num != 3){
                try {
                    condition3.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("C");
            num = 1;
            condition1.signal();
            lock.unlock();
        }

    }

}
