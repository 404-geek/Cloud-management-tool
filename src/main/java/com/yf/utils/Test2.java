package com.yf.utils;

import java.io.PrintStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;

public class Test2
{
  public static void main(String[] args)
  {
    ArrayList<String> sortedZones = new ArrayList(ZoneId.getAvailableZoneIds());
    
    System.out.println("Number of zones: " + sortedZones.size());
    System.out.println("");
    Collections.sort(sortedZones);
    for (String zone : sortedZones) {
      System.out.println(zone);
    }
  }
}
