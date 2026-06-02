[33mcommit 55011c51609704bf103baf5c9c3a3cfa2bf7cf08[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mFeature/adiciona-contrato[m[33m)[m
Author: Larissa <larissaesp02@gmail.com>
Date:   Sun May 24 09:12:11 2026 -0300

    fix: corrige parse null string em demissão e edição de endereço

[33mcommit 22d9da32d84e8679913e71b5947c425fe38a73e0[m
Author: Larissa <larissaesp02@gmail.com>
Date:   Sun May 24 09:07:47 2026 -0300

    Fix: Cannot parse null string em AtualizarFuncionario e EditarFuncionario
    
    DetalharFuncionarioAction e EditarFuncionarioAction liam id via
    getParameter mas recebiam o valor via setAttribute, resultando em
    Integer.parseInt(null). Agora ambas as actions leem o id de parametro
    OU de atributo de request, com fallback adicional para txtId em
    EditarFuncionarioAction. AtualizarFuncionarioAction agora seta o
    atributo id antes de delegar para ambas as actions no caminho
    de sucesso e no caminho de erro.
    
    Co-Authored-By: Claude Sonnet 4.6 <noreply@anthropic.com>

[33mcommit d700eb741544db5ec9e35748dc7999a943bb785e[m
Author: Larissa <larissaesp02@gmail.com>
Date:   Sun May 24 08:51:33 2026 -0300

    Conserta parse null string quando demite funcionario

[33mcommit 500d83545c049f433227e913d04c4a6eea026183[m[33m ([m[1;31morigin/Feature/adiciona-contrato[m[33m)[m
Author: Larissa <larissaesp02@gmail.com>
Date:   Fri May 22 21:57:46 2026 -0300

    Feature: Adiciona Spring Profile

[33mcommit 1cbc8fc8445fa53af0c354da45885decdf7eed81[m
Author: Larissa <larissaesp02@gmail.com>
Date:   Fri May 22 21:31:57 2026 -0300

    Feature/Adiciona classe contrato e popula migrations

[33mcommit 4a042ba5f21ebd6df010ab4e0f67bae32bac7ae6[m[33m ([m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Author: Larissa Santo <162848274+Larissa-Holy@users.noreply.github.com>
Date:   Thu May 21 19:52:49 2026 -0300

    Update README.md
    
    Atualização do README

[33mcommit 4d2722d7c42792e98eb728a90b99eeaa8c54188a[m
Author: Nicolas Mariano de Azevedo <azevedo.nicolas05@gmail.com>
Date:   Sat May 9 15:51:29 2026 -0300

    feat: implementa refatoracao arquitetural SOLID com camadas de Service e DAOFactory

[33mcommit 1ec6397b8520b50a8b2025eb0153657e246ba449[m
Merge: 6dc9728 19ca9e9
Author: Nicolas Mariano de Azevedo <azevedo.nicolas05@gmail.com>
Date:   Sat May 9 15:27:51 2026 -0300

    Merge das atualizacoes do repositorio remoto com as refatoracoes locais

[33mcommit 6dc972889e82006c2ea0fd0a4c77ddf1e16f3e61[m
Author: Nicolas Mariano de Azevedo <azevedo.nicolas05@gmail.com>
Date:   Sat May 9 15:25:00 2026 -0300

    refatoracao: corrige relacionamentos de composicao em Endereco, Funcionario e Setor

[33mcommit 19ca9e9749bd5ed48afdc062f26fa7b0538b7a79[m
Author: Nicolas Mariano de Azevedo <azevedo.nicolas05@gmail.com>
Date:   Thu May 7 21:42:55 2026 -0300

    Add usage instructions to README
    
    Added instructions for running and stopping the application using PowerShell.

[33mcommit 18abc22ce3a887063593561440e1cf685f982847[m
Author: Nicolas Mariano de Azevedo <azevedo.nicolas05@gmail.com>
Date:   Mon May 4 18:53:36 2026 -0300

    atualização geral

[33mcommit 0206ad8abbf84959687d204fdaf052efae135b41[m[33m ([m[1;31morigin/feature/interface[m[33m)[m
Author: Ana Raquel <anaraquel.dsc@gmail.com>
Date:   Wed Apr 15 19:39:17 2026 -0300

    feat: adiciona interface

[33mcommit b12732c99bd74664a5948608398c315fca6c6a69[m[33m ([m[1;31morigin/feature/padroes-projeto[m[33m)[m
Author: Ana Raquel <anaraquel.dsc@gmail.com>
Date:   Tue Apr 14 20:10:37 2026 -0300

    feat: implementacao dos padroes Command e Factory na versao terminal

[33mcommit a0f02248796831c604a8ffca6751f57a4bc7957b[m
Author: Nicolas Mariano de Azevedo <azevedo.nicolas05@gmail.com>
Date:   Sat Apr 11 15:52:55 2026 -0300

    feat: estrutura inicial do projeto com CRUD e migrations
