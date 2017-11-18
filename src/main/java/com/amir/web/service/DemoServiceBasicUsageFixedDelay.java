package com.amir.web.service;
 
import java.util.Date;

public class DemoServiceBasicUsageFixedDelay
{
    public void demoServiceMethod()
    {
        System.out.println("Method executed at every 5 seconds. Current time is :: "+ new Date());
    }
}