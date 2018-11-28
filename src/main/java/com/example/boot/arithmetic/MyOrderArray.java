package com.example.boot.arithmetic;

/**
 * 2018/11/17 13:31
 * 走路呼呼带风
 * 有序数组
 */
public class MyOrderArray {
    private int[] intArray;
    private int length = 0; // 数组元素个数

    // 构造方法，传入数组最大长度
    public MyOrderArray(int max) {
        intArray = new int[max];
    }

    // 用二分查找法定位某个元素，如果存在返回其下标，不存在则返回-1
    public int find(int target) {
        int lowerBound = 0; // 搜索段最小元素的小标
        int upperBound = length - 1; // 搜索段最大元素的下标
        int curIn; // 当前检测元素的下标

        if (upperBound < 0) { // 如果数组为空，直接返回-1
            return -1;
        }

        while (true) {
            curIn = (lowerBound + upperBound) / 2;

            if (target == intArray[curIn]) {
                return curIn;
            } else if (curIn == lowerBound) { // 在当前下标与搜索段的最小下标重合时，代表搜索段中只包含1个或2个元素
                // 既然走到该分支，证明上一个if分支不满足，即目标元素不等于低位元素
                if (target == intArray[upperBound]) { // 等于高位元素，返回
                    return upperBound;
                } else { // 高位元素也不等于目标元素，证明数组中没有该元素，搜索结束
                    return -1;
                }
            } else {// 搜索段中的元素至少有三个，且当前元素不等于目标元素
                if (intArray[curIn] < target) {
                    // 如果当前元素小于目标元素，则将下一个搜索段的最小下标置为当前元素的下标
                    lowerBound = curIn;
                } else {
                    // 如果当前元素大于目标元素，则将下一个搜索段的最大下标置为当前元素的下标
                    upperBound = curIn;
                }
            }
        }
    }

    // 插入
    public void insert(int elem) {
        int location = 0;

        // 判断应插入位置的下标
        for (; location < length; location++) {
            if (intArray[location] > elem)
                break;
        }
        // System.out.println(location);
        // 将插入下标之后的所有元素后移一位
        for (int i = length; i > location; i--) {
            intArray[i] = intArray[i - 1];
        }

        // 插入元素
        intArray[location] = elem;

        length++;
    }

    // 删除某个指定的元素值，删除成功则返回true，否则返回false
    public boolean delete(int target) {
        int index = -1;
        if ((index = find(target)) != -1) {
            for (int i = index; i < length - 1; i++) {
                // 删除元素之后的所有元素前移一位
                intArray[i] = intArray[i + 1];
            }
            length--;
            return true;
        } else {
            return false;
        }
    }

    // 列出所有元素
    public void display() {
        for (int i = 0; i < length; i++) {
            System.out.print(intArray[i] + "\t");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MyOrderArray orderArray = new MyOrderArray(4);

        orderArray.insert(3);
        orderArray.insert(4);
        orderArray.insert(6);
        orderArray.insert(8);

        int i = orderArray.find(8);
        System.out.println("在队列中的位置是" + i);
    }

}
