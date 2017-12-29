package com.xwtec.tools.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTest {
    private static class TestTask extends RecursiveTask<List<Integer>>{
        int begin;
        int end;
        TestTask(int begin,int end){
            this.begin = begin;
            this.end = end;
        }
        @Override
        protected List<Integer> compute() {
            int total = end - begin;
            if (total < 10){
                ArrayList<Integer> l = new ArrayList<>();
                System.out.println(begin + "..." + end);
                return l;
            }
            int average = total / 2;
            TestTask leftTask = new TestTask(begin,begin + average);
            leftTask.fork();
            TestTask rightTask = new TestTask(begin + average , end);
            rightTask.fork();
            List<Integer> left = leftTask.join();
            List<Integer> right = rightTask.join();
            left.addAll(right);
            return left;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            TestTask task = new TestTask(0, 1_00_0);
            task.fork();
            List<Integer> join = task.join();
            //join.forEach(System.out::println);
            int sum = join.stream().mapToInt(Integer::intValue).sum();

            System.out.println(sum);
            //System.out.println(count);
            //System.out.println(join.size());
            //join.forEach(System.out::println);
        }
    }
}
