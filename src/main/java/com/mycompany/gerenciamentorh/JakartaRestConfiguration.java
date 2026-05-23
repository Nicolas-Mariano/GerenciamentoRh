package com.mycompany.gerenciamentorh;

import com.mycompany.gerenciamentorh.resources.ContratoResource;
import com.mycompany.gerenciamentorh.resources.FuncionarioResource;
import com.mycompany.gerenciamentorh.resources.SetorResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("api")
public class JakartaRestConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(FuncionarioResource.class);
        classes.add(ContratoResource.class);
        classes.add(SetorResource.class);
        return classes;
    }
}
