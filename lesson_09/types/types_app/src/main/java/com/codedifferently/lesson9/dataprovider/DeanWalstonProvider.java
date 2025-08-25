package com.codedifferently.lesson9.dataprovider;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DeanWalstonProvider extends DataProvider {

  public String getProviderName() {
    return "deanwalston";
  }

  public Map<String, Class> getColumnTypeByName() {
    return Map.of(
        "column1", Long.class,
        "column2", Short.class,
        "column3", Double.class,
        "column4", Integer.class,
        "column5", Boolean.class,
        "column6", String.class,
        "column7", Float.class);
  }
}
