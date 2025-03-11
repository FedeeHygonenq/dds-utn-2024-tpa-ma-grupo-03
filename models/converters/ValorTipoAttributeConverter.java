package ar.edu.utn.frba.dds.models.converters;

import ar.edu.utn.frba.dds.dominioGeneral.DTOs.TIPODTOOut;
import ar.edu.utn.frba.dds.dominioGeneral.datos.ValorGenerico;
import javax.persistence.AttributeConverter;


public class ValorTipoAttributeConverter<TIPO> implements AttributeConverter<ValorGenerico<TIPO>, TIPODTOOut> {

  @Override
  public TIPODTOOut convertToDatabaseColumn(ValorGenerico<TIPO> tValorGenerico) {
    TIPODTOOut ret = new TIPODTOOut();
    ret.setType(agregarTipo(tValorGenerico));
    ret.setValor(String.valueOf(tValorGenerico.getTipoObjeto()));
    return ret;
  }

  private String agregarTipo(ValorGenerico<TIPO> tValorGenerico) {
    TIPO tipo = tValorGenerico.getTipoObjeto();
    if (tipo instanceof Integer) {
      return "Integer";
    } else if (tipo instanceof Double) {
      return "Double";
    } else if (tipo instanceof Boolean) {
      return "Boolean";
    }
    throw new IllegalArgumentException("Tipo no soportado: " + tipo.getClass().getSimpleName());
  }

  @Override
  public ValorGenerico<TIPO> convertToEntityAttribute(TIPODTOOut valorGuardado) {
    try {
      return fromString(valorGuardado);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private ValorGenerico<TIPO> fromString(TIPODTOOut valor) {
    switch (valor.getType()) {
      case "Integer":
        return (ValorGenerico<TIPO>) new ValorGenerico<>(Integer.valueOf(valor.getValor()));
      case "Double":
        return (ValorGenerico<TIPO>) new ValorGenerico<>(Double.valueOf(valor.getValor()));
      case "Boolean":
        return (ValorGenerico<TIPO>) new ValorGenerico<>(Boolean.valueOf(valor.getValor()));
      default:
        throw new IllegalArgumentException("Tipo no soportado: " + valor.getType());
    }
  }

}
