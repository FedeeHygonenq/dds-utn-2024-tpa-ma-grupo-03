package ar.edu.utn.frba.dds.utilidades.lectorProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LectorProperties {

  private static String pathProperties = "src/main/resources/app.properties";

  public static String getStringPropertie(String key) {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(pathProperties));
      return properties.getProperty(key);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Integer getIntegerPropertie(String key) {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(pathProperties));
      return Integer.parseInt(properties.getProperty(key));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Boolean getBooleanPropertie(String key) {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(pathProperties));
      return Boolean.parseBoolean(properties.getProperty(key));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Double getDoublePropertie(String key) {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(pathProperties));
      return Double.parseDouble(properties.getProperty(key));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getStringPropertie(String key, String path) {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(path));
      return properties.getProperty(key);
    } catch (IOException e) {
       throw new RuntimeException(e);
    }
  }

    public static Integer getIntegerPropertie(String key, String path) {
        Properties properties = new Properties();
        try {
        properties.load(new FileInputStream(path));
        return Integer.parseInt(properties.getProperty(key));
        } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }

    public static Boolean getBooleanPropertie(String key, String path) {
        Properties properties = new Properties();
        try {
        properties.load(new FileInputStream(path));
        return Boolean.parseBoolean(properties.getProperty(key));
        } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }

    public static Double getDoublePropertie(String key, String path) {
        Properties properties = new Properties();
        try {
        properties.load(new FileInputStream(path));
        return Double.parseDouble(properties.getProperty(key));
        } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }
}
