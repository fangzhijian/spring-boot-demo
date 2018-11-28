package com.example.boot.arithmetic;

/**
 * 2018/11/17 13:16
 * 走路呼呼带风
 * 栈
 */
public class MyStack {
    private int size;                 //栈的大小
    private int top;                  //栈顶元素的下标
    public int [] stackArray;   //栈的容器

    //构造函数
    public MyStack(int size){
        stackArray = new int [size];
        top = -1; //初始化栈的时候，栈内无元素，栈顶下标设为-1
        this.size = size;
    }

    //入栈，同时，栈顶元素的下标加一
    public void push(int elem){
        stackArray[++top] = elem; //插入栈顶
    }

    //出栈，删除栈顶元素，同时，栈顶元素的下标减一
    public int pop(){
        return stackArray[top--];
    }

    //查看栈顶元素，但不删除
    public int peek(){
        return stackArray[top];
    }

    //判空
    public boolean isEmpty(){
        return (top == -1);
    }

    //判满
    public boolean isFull(){
        return (top == size-1);
    }

}
