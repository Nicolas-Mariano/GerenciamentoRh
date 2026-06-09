package service;

import model.Contrato;
import model.Setor;

public interface ISetorService {
    void vincularGerente(Setor setor, Contrato contrato) throws Exception;
}
