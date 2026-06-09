package com.mycompany.gerenciamentorh.resources;

import dao.DAOFactory;
import model.Contrato;
import model.Setor;
import service.ServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/setores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SetorResource {

    @POST
    public Response cadastrar(Map<String, Object> body) {
        try {
            Setor s = new Setor();
            s.setNome((String) body.get("nome"));
            DAOFactory.getSetorDAO().cadastrar(s);
            return Response.status(Response.Status.CREATED).entity(Map.of("mensagem", "Setor criado com sucesso.")).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, Map<String, Object> body) {
        try {
            Setor s = DAOFactory.getSetorDAO().consultarById(id);
            if (s == null) return Response.status(Response.Status.NOT_FOUND).entity(Map.of("erro", "Setor não encontrado.")).build();
            if (body.containsKey("nome")) s.setNome((String) body.get("nome"));
            DAOFactory.getSetorDAO().atualizar(s);
            return Response.ok(Map.of("mensagem", "Setor atualizado com sucesso.")).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            Setor s = new Setor();
            s.setId(id);
            DAOFactory.getSetorDAO().deletar(s);
            return Response.ok(Map.of("mensagem", "Setor excluído com sucesso.")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT).entity(Map.of("erro", e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}/gerente")
    public Response vincularGerente(@PathParam("id") int id, Map<String, Object> body) {
        try {
            int idContrato = toInt(body.get("contratoId"));
            Setor setorRef = new Setor();
            setorRef.setId(id);
            Contrato contratoRef = new Contrato();
            contratoRef.setId(idContrato);
            ServiceFactory.getSetorService().vincularGerente(setorRef, contratoRef);
            return Response.ok(Map.of("mensagem", "Gerente vinculado ao setor com sucesso.")).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    private Map<String, Object> toMap(Setor s) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", s.getId());
        m.put("nome", s.getNome());
        if (s.getContratoResponsavel() != null) {
            m.put("idContratoResponsavel", s.getContratoResponsavel().getId());
            if (s.getContratoResponsavel().getFuncionario() != null) {
                m.put("nomeResponsavel", s.getContratoResponsavel().getFuncionario().getNome());
            }
        }
        return m;
    }

    private int toInt(Object value) {
        if (value instanceof Integer) return (Integer) value;
        return Integer.parseInt(value.toString());
    }

    private Response erro(String msg) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("erro", msg)).build();
    }
}
