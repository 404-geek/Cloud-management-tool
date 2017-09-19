package com.yf.utils;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class Timezone
{
  static Logger LOGGER = Logger.getLogger(Timezone.class.getName());
  
  public static ArrayList<String> getZone()
  {
    ArrayList<String> sortedZones = new ArrayList(ZoneId.getAvailableZoneIds());
    Collections.sort(sortedZones);
    return sortedZones;
  }
}

