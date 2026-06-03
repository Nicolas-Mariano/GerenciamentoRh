package service;

import model.Contrato;
import model.Funcionario;
import model.NivelSenioridade;
import model.Setor;
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
        int idFuncionario = 1;
        Contrato dadosContrato = criarContratoValido(idFuncionario);
        contratoDAOFake.setContratoAtivo(null);

        // Act
        service.contratar(idFuncionario, dadosContrato);

        // Assert
        assertNotNull(contratoDAOFake.ultimoCadastrado);
        assertNotNull(dadosContrato.getMatricula());
    }

    @Test
    void contratar_deveLancarExcecao_quandoFuncionarioJaPossuiContratoAtivo() {
        // Arrange
        int idFuncionario = 1;
        Contrato dadosContrato = criarContratoValido(idFuncionario);
        contratoDAOFake.setContratoAtivo(new Contrato());

        // Act & Assert
        Exception excecao = assertThrows(Exception.class, () -> service.contratar(idFuncionario, dadosContrato));
        assertTrue(excecao.getMessage().contains("já possui um contrato ativo"));
    }

    @Test
    void contratar_deveLancarExcecao_quandoDataAdmissaoEFutura() {
        // Arrange
        Contrato dadosContrato = new Contrato();
        dadosContrato.setDataAdmissao(new Date(System.currentTimeMillis() + 86400000L));

        // Act & Assert
        Exception excecao = assertThrows(Exception.class, () -> service.contratar(1, dadosContrato));
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

        // Act
        service.demitir(idContrato, "Pedido de demissão", new Date());

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

        // Act & Assert
        Exception excecao = assertThrows(Exception.class, () -> service.demitir(idContrato, "motivo", new Date()));
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

        // Act
        service.aplicarPromocao(idContrato, NivelSenioridade.SENIOR, "PERCENTUAL", 10.0);

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

        // Act
        service.aplicarPromocao(idContrato, NivelSenioridade.SENIOR, "BONUS", 500.0);

        // Assert
        assertEquals(1500.0, contrato.getSalarioBase(), 0.01);
        assertEquals(NivelSenioridade.SENIOR, contrato.getNivelSenioridade());
        assertNotNull(contratoDAOFake.ultimoPromovido);
    }

    @Test
    void aplicarPromocao_deveLancarExcecao_quandoTipoInvalido() {
        // Arrange
        int idContrato = 10;
        Contrato contrato = criarContratoAtivo(idContrato);
        contrato.setNivelSenioridade(NivelSenioridade.PLENO);
        contratoDAOFake.setContratoPorId(contrato);

        // Act & Assert
        Exception excecao = assertThrows(Exception.class,
            () -> service.aplicarPromocao(idContrato, NivelSenioridade.SENIOR, "INVALIDO", 100.0));
        assertTrue(excecao.getMessage().contains("Tipo de aumento inválido"));
    }

    @Test
    void aplicarPromocao_deveLancarExcecao_quandoContratoEncerrado() {
        // Arrange
        int idContrato = 10;
        Contrato contrato = criarContratoAtivo(idContrato);
        contrato.setDataDemissao(new Date());
        contratoDAOFake.setContratoPorId(contrato);

        // Act & Assert
        Exception excecao = assertThrows(Exception.class,
            () -> service.aplicarPromocao(idContrato, NivelSenioridade.SENIOR, "PERCENTUAL", 10.0));
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
