package com.mycompany.gerenciamentorh.resources;

import dao.DAOFactory;
import model.Contrato;
import model.NivelSenioridade;
import model.Setor;
import service.ServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/contratos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContratoResource {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    @POST
    public Response contratar(Map<String, Object> body) {
        try {
            int idFuncionario = toInt(body.get("idFuncionario"));
            Contrato c = extrairContrato(body);
            ServiceFactory.getContratoService().contratar(idFuncionario, c);
            Map<String, Object> resp = toMap(c);
            resp.put("mensagem", "Contrato criado com sucesso. Matrícula: " + c.getMatricula());
            return Response.status(Response.Status.CREATED).entity(resp).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @GET
    @Path("/ativo/{funcionarioId}")
    public Response buscarAtivo(@PathParam("funcionarioId") int funcionarioId) {
        try {
            Contrato c = ServiceFactory.getContratoService().buscarAtivo(funcionarioId);
            if (c == null) return Response.status(Response.Status.NOT_FOUND).entity(Map.of("mensagem", "Nenhum contrato ativo encontrado.")).build();
            return Response.ok(toMap(c)).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @GET
    @Path("/historico/{funcionarioId}")
    public Response buscarHistorico(@PathParam("funcionarioId") int funcionarioId) {
        try {
            List<Contrato> lista = ServiceFactory.getContratoService().buscarHistorico(funcionarioId);
            List<Map<String, Object>> resultado = lista.stream().map(this::toMap).collect(Collectors.toList());
            return Response.ok(resultado).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @POST
    @Path("/{id}/demitir")
    public Response demitir(@PathParam("id") int id, Map<String, Object> body) {
        try {
            String motivo = (String) body.getOrDefault("motivo", "");
            Date dataDemissao = null;
            if (body.containsKey("dataDemissao") && body.get("dataDemissao") != null) {
                dataDemissao = SDF.parse((String) body.get("dataDemissao"));
            }
            ServiceFactory.getContratoService().demitir(id, motivo, dataDemissao);
            return Response.ok(Map.of("mensagem", "Demissão registrada com sucesso.")).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @POST
    @Path("/{id}/aumento")
    public Response aplicarAumento(@PathParam("id") int id, Map<String, Object> body) {
        try {
            String tipo = (String) body.get("tipo");
            double valor = toDouble(body.get("valor"));
            ServiceFactory.getContratoService().aplicarAumento(id, tipo, valor);
            Contrato c = DAOFactory.getContratoDAO().consultarById(id);
            return Response.ok(Map.of("mensagem", "Aumento aplicado.", "novoSalario", c.getSalarioBase())).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    private Contrato extrairContrato(Map<String, Object> body) throws Exception {
        Contrato c = new Contrato();
        Object dataAdmissaoRaw = body.get("dataAdmissao");
        if (dataAdmissaoRaw != null && !dataAdmissaoRaw.toString().isBlank()) {
            c.setDataAdmissao(SDF.parse(dataAdmissaoRaw.toString()));
        }
        c.setSalarioBase(toDouble(body.get("salarioBase")));
        String nivelStr = (String) body.get("nivelSenioridade");
        if (nivelStr == null || nivelStr.isBlank()) {
            throw new Exception("Campo obrigatório ausente: nivelSenioridade.");
        }
        c.setNivelSenioridade(NivelSenioridade.valueOf(nivelStr.toUpperCase()));
        Setor s = new Setor();
        s.setId(toInt(body.get("idSetor")));
        c.setSetor(s);
        return c;
    }

    private Map<String, Object> toMap(Contrato c) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", c.getId());
        m.put("matricula", c.getMatricula());
        m.put("dataAdmissao", c.getDataAdmissao() != null ? SDF.format(c.getDataAdmissao()) : null);
        m.put("dataDemissao", c.getDataDemissao() != null ? SDF.format(c.getDataDemissao()) : null);
        m.put("motivoDesligamento", c.getMotivoDesligamento());
        m.put("salarioBase", c.getSalarioBase());
        m.put("nivelSenioridade", c.getNivelSenioridade() != null ? c.getNivelSenioridade().name() : null);
        m.put("ativo", c.isAtivo());
        if (c.getFuncionario() != null) {
            m.put("funcionarioId", c.getFuncionario().getId());
            m.put("funcionarioNome", c.getFuncionario().getNome());
        }
        if (c.getSetor() != null) {
            m.put("setorId", c.getSetor().getId());
            m.put("setorNome", c.getSetor().getNome());
        }
        return m;
    }

    private int toInt(Object value) {
        if (value == null) throw new IllegalArgumentException("Valor inteiro não pode ser nulo.");
        if (value instanceof Integer) return (Integer) value;
        return Integer.parseInt(value.toString());
    }

    private double toDouble(Object value) {
        if (value == null) throw new IllegalArgumentException("Valor numérico não pode ser nulo.");
        if (value instanceof Double) return (Double) value;
        if (value instanceof Integer) return ((Integer) value).doubleValue();
        return Double.parseDouble(value.toString());
    }

    private Response erro(String msg) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("erro", msg)).build();
    }
}
