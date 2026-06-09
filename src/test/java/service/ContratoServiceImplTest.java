package service;

import model.Contrato;
import model.Funcionario;
import model.NivelSenioridade;
import model.Setor;
import salary.TipoAumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ContratoServiceImplTest {

    private ContratoDAOFake contratoDAOFake;
    private ContratoServiceImpl service;

    @BeforeEach
    void setUp() {
        contratoDAOFake = new ContratoDAOFake();
        service = new ContratoServiceImpl(contratoDAOFake);
    }

    // ========================
    // contratar()
    // ========================

    @Test
    void contratar_deveContratarComSucesso_quandoDadosValidos() throws Exception {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        Contrato dadosContrato = criarContratoValido(1);
        contratoDAOFake.setContratoAtivo(null);

        // Act
        service.contratar(funcionario, dadosContrato);

        // Assert
        assertNotNull(contratoDAOFake.ultimoCadastrado);
        assertNotNull(dadosContrato.getMatricula());
    }

    @Test
    void contratar_deveLancarExcecao_quandoFuncionarioJaPossuiContratoAtivo() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        Contrato dadosContrato = criarContratoValido(1);
        contratoDAOFake.setContratoAtivo(new Contrato());

        // Act & Assert
        Exception excecao = assertThrows(Exception.class, () -> service.contratar(funcionario, dadosContrato));
        assertTrue(excecao.getMessage().contains("já possui um contrato ativo"));
    }

    @Test
    void contratar_deveLancarExcecao_quandoDataAdmissaoEFutura() {
        // Arrange
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);
        Contrato dadosContrato = new Contrato();
        dadosContrato.setDataAdmissao(new Date(System.currentTimeMillis() + 86400000L));

        // Act & Assert
        Exception excecao = assertThrows(Exception.class, () -> service.contratar(funcionario, dadosContrato));
        assertTrue(excecao.getMessage().contains("não pode ser futura"));
    }

    // ========================
    // demitir()
    // ========================

    @Test
    void demitir_deveDemitirComSucesso_quandoContratoAtivo() throws Exception {
        // Arrange
        int idContrato = 10;
        Contrato contrato = criarContratoAtivo(idContrato);
        contratoDAOFake.setContratoPorId(contrato);
        Contrato contratoRef = new Contrato();
        contratoRef.setId(idContrato);

        // Act
        service.demitir(contratoRef, "Pedido de demissão", new Date());

        // Assert
        assertNotNull(contratoDAOFake.ultimoAtualizado);
        assertNotNull(contrato.getDataDemissao());
    }

    @Test
    void demitir_deveLancarExcecao_quandoContratoJaEncerrado() {
        // Arrange
        int idContrato = 10;
        Contrato contrato = criarContratoAtivo(idContrato);
        contrato.setDataDemissao(new Date());
        contratoDAOFake.setContratoPorId(contrato);
        Contrato contratoRef = new Contrato();
        contratoRef.setId(idContrato);

        // Act & Assert
        Exception excecao = assertThrows(Exception.class, () -> service.demitir(contratoRef, "motivo", new Date()));
        assertTrue(excecao.getMessage().contains("já está encerrado"));
    }

    // ========================
    // aplicarPromocao()
    // ========================

    @Test
    void aplicarPromocao_deveAplicarPercentualComSucesso_quandoContratoAtivo() throws Exception {
        // Arrange
        int idContrato = 10;
        Contrato contrato = criarContratoAtivo(idContrato);
        contrato.setSalarioBase(1000.0);
        contrato.setNivelSenioridade(NivelSenioridade.PLENO);
        contratoDAOFake.setContratoPorId(contrato);
        Contrato contratoRef = new Contrato();
        contratoRef.setId(idContrato);

        // Act
        service.aplicarPromocao(contratoRef, NivelSenioridade.SENIOR, TipoAumento.PERCENTUAL, 10.0);

        // Assert
        assertEquals(1100.0, contrato.getSalarioBase(), 0.01);
        assertEquals(NivelSenioridade.SENIOR, contrato.getNivelSenioridade());
        assertNotNull(contratoDAOFake.ultimoPromovido);
    }

    @Test
    void aplicarPromocao_deveAplicarBonusComSucesso_quandoContratoAtivo() throws Exception {
        // Arrange
        int idContrato = 10;
        Contrato contrato = criarContratoAtivo(idContrato);
        contrato.setSalarioBase(1000.0);
        contrato.setNivelSenioridade(NivelSenioridade.PLENO);
        contratoDAOFake.setContratoPorId(contrato);
        Contrato contratoRef = new Contrato();
        contratoRef.setId(idContrato);

        // Act
        service.aplicarPromocao(contratoRef, NivelSenioridade.SENIOR, TipoAumento.BONUS, 500.0);

        // Assert
        assertEquals(1500.0, contrato.getSalarioBase(), 0.01);
        assertEquals(NivelSenioridade.SENIOR, contrato.getNivelSenioridade());
        assertNotNull(contratoDAOFake.ultimoPromovido);
    }

    @Test
    void aplicarPromocao_deveLancarExcecao_quandoContratoEncerrado() {
        // Arrange
        int idContrato = 10;
        Contrato contrato = criarContratoAtivo(idContrato);
        contrato.setDataDemissao(new Date());
        contratoDAOFake.setContratoPorId(contrato);
        Contrato contratoRef = new Contrato();
        contratoRef.setId(idContrato);

        // Act & Assert
        Exception excecao = assertThrows(Exception.class,
            () -> service.aplicarPromocao(contratoRef, NivelSenioridade.SENIOR, TipoAumento.PERCENTUAL, 10.0));
        assertTrue(excecao.getMessage().contains("contrato encerrado"));
    }

    // ========================
    // Helpers
    // ========================

    private Contrato criarContratoValido(int idFuncionario) {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(idFuncionario);
        Setor setor = new Setor();
        setor.setId(1);
        Contrato contrato = new Contrato();
        contrato.setDataAdmissao(new Date(System.currentTimeMillis() - 86400000L));
        contrato.setSalarioBase(2000.0);
        contrato.setNivelSenioridade(NivelSenioridade.PLENO);
        contrato.setFuncionario(funcionario);
        contrato.setSetor(setor);
        return contrato;
    }

    private Contrato criarContratoAtivo(int idContrato) {
        Contrato contrato = criarContratoValido(1);
        contrato.setId(idContrato);
        return contrato;
    }
}
