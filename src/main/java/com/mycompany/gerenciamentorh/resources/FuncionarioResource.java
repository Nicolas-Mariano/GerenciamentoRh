package com.mycompany.gerenciamentorh.resources;

import dao.DAOFactory;
import model.Endereco;
import model.Funcionario;
import service.ServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/funcionarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FuncionarioResource {

    @GET
    public Response listar() {
        try {
            List<Funcionario> lista = DAOFactory.getFuncionarioDAO().consultarTodos();
            List<Map<String, Object>> resultado = lista.stream()
                .map(this::toMap)
                .collect(Collectors.toList());
            return Response.ok(resultado).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            Funcionario f = DAOFactory.getFuncionarioDAO().consultarById(id);
            if (f == null) return Response.status(Response.Status.NOT_FOUND).entity(Map.of("erro", "Funcionário não encontrado")).build();
            return Response.ok(toMap(f)).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @POST
    public Response cadastrar(Map<String, Object> body) {
        try {
            Funcionario f = extrairFuncionario(body);
            Endereco e = extrairEndereco(body);
            ServiceFactory.getFuncionarioService().cadastrar(f, e);
            Map<String, Object> resp = toMap(f);
            resp.put("mensagem", "Funcionário cadastrado com sucesso.");
            return Response.status(Response.Status.CREATED).entity(resp).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, Map<String, Object> body) {
        try {
            Funcionario f = DAOFactory.getFuncionarioDAO().consultarById(id);
            if (f == null) return Response.status(Response.Status.NOT_FOUND).entity(Map.of("erro", "Funcionário não encontrado")).build();

            if (body.containsKey("nome")) f.setNome((String) body.get("nome"));
            if (body.containsKey("cpf")) f.setCpf(((String) body.get("cpf")).replaceAll("[^0-9]", ""));
            if (body.containsKey("telefone")) f.setTelefone(((String) body.get("telefone")).replaceAll("[^0-9]", ""));
            if (body.containsKey("email")) f.setEmail((String) body.get("email"));

            Endereco e = null;
            if (f.getEndereco() != null && f.getEndereco().getId() > 0) {
                e = DAOFactory.getEnderecoDAO().consultarById(f.getEndereco().getId());
                if (body.containsKey("logradouro")) e.setLogradouro((String) body.get("logradouro"));
                if (body.containsKey("bairro")) e.setBairro((String) body.get("bairro"));
                if (body.containsKey("cidade")) e.setCidade((String) body.get("cidade"));
                if (body.containsKey("estado")) e.setEstado((String) body.get("estado"));
                if (body.containsKey("cep")) e.setCep(((String) body.get("cep")).replaceAll("[^0-9]", ""));
                if (body.containsKey("numEndereco")) e.setNumEndereco((String) body.get("numEndereco"));
                if (body.containsKey("complemento")) e.setComplemento((String) body.get("complemento"));
            } else {
                e = extrairEndereco(body);
            }

            ServiceFactory.getFuncionarioService().atualizar(f, e);
            return Response.ok(Map.of("mensagem", "Funcionário atualizado com sucesso.")).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") int id) {
        try {
            DAOFactory.getFuncionarioDAO().deletar(id);
            return Response.ok(Map.of("mensagem", "Funcionário excluído com sucesso.")).build();
        } catch (Exception e) {
            return erro(e.getMessage());
        }
    }

    private Funcionario extrairFuncionario(Map<String, Object> body) {
        return Funcionario.getBuilder()
            .comNome((String) body.get("nome"))
            .comCpf(body.containsKey("cpf") ? ((String) body.get("cpf")).replaceAll("[^0-9]", "") : null)
            .comTelefone(body.containsKey("telefone") ? ((String) body.get("telefone")).replaceAll("[^0-9]", "") : null)
            .comEmail((String) body.get("email"))
            .constroi();
    }

    private Endereco extrairEndereco(Map<String, Object> body) {
        return Endereco.getBuilder()
            .comLogradouro((String) body.get("logradouro"))
            .comBairro((String) body.get("bairro"))
            .comCidade((String) body.get("cidade"))
            .comEstado((String) body.get("estado"))
            .comCep(body.containsKey("cep") ? ((String) body.get("cep")).replaceAll("[^0-9]", "") : null)
            .comNumEndereco((String) body.get("numEndereco"))
            .comComplemento((String) body.getOrDefault("complemento", null))
            .constroi();
    }

    private Map<String, Object> toMap(Funcionario f) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", f.getId());
        m.put("nome", f.getNome());
        m.put("cpf", f.getCpfFormatado());
        m.put("telefone", f.getTelefoneFormatado());
        m.put("email", f.getEmail());
        return m;
    }

    private Response erro(String msg) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("erro", msg)).build();
    }
}
