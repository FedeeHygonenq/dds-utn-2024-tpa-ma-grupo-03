package ar.edu.utn.frba.dds.models.converters;

import ar.edu.utn.frba.dds.services.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion.ComunicacionMail;
import ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion.ComunicacionTelegram;
import ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion.ComunicacionWhatsApp;
import ar.edu.utn.frba.dds.utilidades.mediosDeComunicacion.MedioDeComunicacion;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MedioDeComunicacionAttributeConverter implements AttributeConverter<MedioDeComunicacion, String> {

  @Override
  public String convertToDatabaseColumn(MedioDeComunicacion medioDeComunicacion) {
    String medio = "";

    if (medioDeComunicacion instanceof ComunicacionTelegram) {
      medio = "comunicacionTelegram";
    }
    if (medioDeComunicacion instanceof ComunicacionWhatsApp) {
      medio = "comunicacionWhatsApp";
    }
    if (medioDeComunicacion instanceof ComunicacionMail) {
      medio = "comunicacionMail";
    }
    return medio;

  }

  @Override
  public MedioDeComunicacion convertToEntityAttribute(String s) {
    MedioDeComunicacion medio = null;

    if (s.compareTo("comunicacionMail") == 0) {
      medio = ServiceLocator.comunicacionMail();
    }

    if (s.compareTo("comunicacionTelegram") == 0) {
      medio = ServiceLocator.comunicacionTelegram();
    }
    if (s.compareTo("comunicacionWhatsApp") == 0) {
      medio = ServiceLocator.comunicacionWhatsApp();
    }

    return medio;

  }
}
