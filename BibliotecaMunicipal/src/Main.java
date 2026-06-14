import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final String PASS_FUNC = "admin123";
    private static final LivroService livroService = new LivroService();
    private static final RequisitanteService reqService = new RequisitanteService();
    private static final ReservaService resService = new ReservaService();
    private static final AutorService autorService = new AutorService();


    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n|================================|");
            System.out.println("|      BIBLIOTECA MUNICIPAL      |");
            System.out.println("|================================|");
            System.out.println("| 1. Entrar como Funcionario     |");
            System.out.println("| 2. Entrar como Requisitante    |");
            System.out.println("| 3. Registar novo Requisitante  |");
            System.out.println("| 0. Sair                        |");
            System.out.println("|================================|");
            System.out.print("Opcao: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> loginFuncionario();
                case 2 -> menuRequisitante();
                case 3 -> registarRequisitante();
                case 0 -> System.out.println("\nA sair...");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);

        DatabaseConnection.fechar();
    }

    private static void loginFuncionario() {
        System.out.print("Password: ");
        String pass = sc.nextLine();
        if (!pass.equals(PASS_FUNC)) {
            System.out.println("Password incorreta.");
            return;
        }
        menuFuncionario();
    }

    private static void menuFuncionario() {
        int opcao;
        do {
            System.out.println("\n|================================|");
            System.out.println("|        AREA FUNCIONARIO        |");
            System.out.println("|================================|");
            System.out.println("| 1. Gerir Livros                |");
            System.out.println("| 2. Gerir Reservas              |");
            System.out.println("| 3. Gerir Requisitantes         |");
            System.out.println("| 4. Gerir Autores               |");
            System.out.println("| 0. Voltar                      |");
            System.out.println("|================================|");
            System.out.print("Opcao: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> menuLivros();
                case 2 -> menuReservas();
                case 3 -> menuRequisitantes();
                case 4 -> menuAutores();
                case 0 -> System.out.println("A voltar...");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void menuLivros() {
        int opcao;
        do {
            System.out.println("\n|================================|");
            System.out.println("|        GESTAO DE LIVROS        |");
            System.out.println("|================================|");
            System.out.println("| 1. Listar todos os livros      |");
            System.out.println("| 2. Listar livros disponiveis   |");
            System.out.println("| 3. Pesquisar por titulo        |");
            System.out.println("| 4. Adicionar livro             |");
            System.out.println("| 5. Atualizar livro             |");
            System.out.println("| 6. Eliminar livro              |");
            System.out.println("| 7. Ver autores de um livro     |");
            System.out.println("| 0. Voltar                      |");
            System.out.println("|================================|");
            System.out.print("Opcao: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> listarTodosLivros();
                case 2 -> listarLivrosDisponiveis();
                case 3 -> pesquisarLivro();
                case 4 -> adicionarLivro();
                case 5 -> atualizarLivro();
                case 6 -> eliminarLivro();
                case 7 -> verAutoresLivro();
                case 0 -> System.out.println("A voltar...");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void listarTodosLivros() {
        try {
            List<Livro> livros = livroService.listarTodos();
            if (livros.isEmpty()) { System.out.println("Nenhum livro encontrado."); return; }
            System.out.println("\n--- Lista de Livros ---");
            for(Livro livro : livros){
                System.out.println(livro);
            }
            System.out.println("Total: " + livros.size() + " exemplares.");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void listarLivrosDisponiveis() {
        try {
            List<Livro> livros = livroService.listarDisponiveis();
            if (livros.isEmpty()) { System.out.println("Nenhum livro disponivel."); return; }
            System.out.println("\n--- Livros Disponiveis ---");
            for (Livro livro : livros){
                System.out.println(livro);
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void pesquisarLivro() {
        System.out.print("Titulo a pesquisar: ");
        String termo = sc.nextLine();
        try {
            List<Livro> livros = livroService.pesquisarPorTitulo(termo);
            if (livros.isEmpty()) {
                System.out.println("Nenhum resultado encontrado.");
            }
            else{
                for (Livro livro : livros){
                    System.out.println(livro);
                }
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void adicionarLivro() {
        try {
            livroService.listarCategorias();
            System.out.print("ID Categoria: ");
            int idCat = sc.nextInt(); sc.nextLine();
            livroService.listarIdiomas();
            System.out.print("ID Idioma: ");
            int idIdioma = sc.nextInt(); sc.nextLine();
            livroService.listarEditoras();
            System.out.print("ID Editora: ");
            int idEdit = sc.nextInt(); sc.nextLine();
            livroService.listarGeneros();
            System.out.print("ID Genero: ");
            int idGenero = sc.nextInt(); sc.nextLine();
            System.out.print("ISBN: ");
            String isbn = sc.nextLine();
            System.out.print("Titulo: ");
            String titulo = sc.nextLine();
            System.out.print("Ano publicacao: ");
            int ano = sc.nextInt(); sc.nextLine();

            boolean ok = livroService.inserir(isbn, titulo, ano, idCat, idIdioma, idEdit, idGenero);
            if (ok) {
                System.out.println("Livro adicionado com sucesso!");
            } else {
                System.out.println("ao foi possivel adicionar.");
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void atualizarLivro() {
        System.out.print("ID do livro: "); int id = sc.nextInt(); sc.nextLine();
        System.out.print("Novo titulo: "); String titulo = sc.nextLine();
        try {
            boolean ok = livroService.atualizar(id, titulo);
            if (ok) {
                System.out.println("Livro atualizado.");
            } else {
                System.out.println("Livro nao encontrado.");
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void eliminarLivro() {
        System.out.print("ID do livro a eliminar: "); int id = sc.nextInt(); sc.nextLine();
        System.out.print("Tem a certeza? (s/n): ");
        if (sc.nextLine().equalsIgnoreCase("s")) {
            try {
                boolean ok = livroService.eliminar(id);

                if (ok) {
                    System.out.println("Livro eliminado.");
                } else {
                    System.out.println("Livro nao encontrado.");
                }

            } catch (SQLException e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void verAutoresLivro() {
        System.out.print("Nome do livro a pesquisar: ");
        String nomeLivro = sc.nextLine();
        try {
            List<Autor> autores = autorService.listarPorLivro(nomeLivro);
            if (autores.isEmpty()){
                System.out.println("Este livro nao tem autores associados.");
            }
            else {
                System.out.println("\n--- Autores do Livro ---");
                for (Autor a : autores) {
                    System.out.println(a);
                }
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }


    private static void menuReservas() {
        int opcao;
        do {
            System.out.println("\n|================================|");
            System.out.println("|       GESTAO DE RESERVAS       |");
            System.out.println("|================================|");
            System.out.println("| 1. Listar todas as reservas    |");
            System.out.println("| 2. Listar reservas por devolver|");
            System.out.println("| 3. Listar reservas atrasadas   |");
            System.out.println("| 4. Reservas de um requisitante |");
            System.out.println("| 5. Nova reserva                |");
            System.out.println("| 6. Registar devolucao          |");
            System.out.println("| 0. Voltar                      |");
            System.out.println("|================================|");
            System.out.print("Opcao: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> listarTodasReservas();
                case 2 -> listarReservasEmAberto();
                case 3 -> listarReservasAtrasadas();
                case 4 -> reservasPorRequisitante();
                case 5 -> novaReserva();
                case 6 -> registarDevolucao();
                case 0 -> System.out.println("A voltar...");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void listarTodasReservas() {
        try {
            List<Reserva> lista = resService.listarTodas();
            if (lista.isEmpty()) { System.out.println("Nenhuma reserva encontrada."); return; }
            System.out.println("\n--- Todas as Reservas ---");
            for (Reserva reserva : lista){
                System.out.println(reserva);
            }
            System.out.println("Total: " + lista.size() + " reserva(s).");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void listarReservasEmAberto() {
        try {
            List<Reserva> lista = resService.listarEmAberto();
            if (lista.isEmpty()) { System.out.println("Nenhuma reserva em aberto."); return; }
            System.out.println("\n--- Reservas em Aberto ---");
            for (Reserva reserva : lista){
                System.out.println(reserva);
            }
            System.out.println("Total: " + lista.size() + " reserva(s) em aberto.");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void listarReservasAtrasadas() {
        try {
            List<Reserva> lista = resService.listarAtrasadas();
            if (lista.isEmpty()) { System.out.println("Nenhuma reserva atrasada."); return; }
            System.out.println("\n--- Reservas ATRASADAS ---");
            for (Reserva reserva : lista){
                System.out.println(reserva);
            }
            System.out.println("Total: " + lista.size() + " reserva(s) atrasada(s).");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void reservasPorRequisitante() {
        System.out.print("ID do requisitante: "); int id = sc.nextInt(); sc.nextLine();
        try {
            List<Reserva> lista = resService.listarPorRequisitante(id);
            if (lista.isEmpty()){
                System.out.println("Nenhuma reserva encontrada.");
            }
            else{
                for (Reserva reserva : lista){
                    System.out.println(reserva);
                }
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void novaReserva() {
        try {
            System.out.print("Seu Id de funcionario: ");
            int idFunc = sc.nextInt(); sc.nextLine();
            System.out.print("ID do requisitante: ");
            int idReq = sc.nextInt(); sc.nextLine();

            System.out.println("\n--- Livros com exemplares disponiveis ---");
            List<Livro> livrosDisponiveis = livroService.listarDisponiveis();
            for (Livro livro : livrosDisponiveis) {
                System.out.println(livro);
            }

            System.out.print("\nID do livro: "); int idLivro = sc.nextInt(); sc.nextLine();
            resService.listarExemplaresDisponiveis(idLivro);
            System.out.print("ID do exemplar: "); int idExemplar = sc.nextInt(); sc.nextLine();

            if (!resService.exemplarDisponivel(idExemplar)) {
                System.out.println("Exemplar nao disponivel.");
                return;
            }

            LocalDate hoje = LocalDate.now();
            System.out.println("Data de saida: " + hoje);

            LocalDate prevista = null;
            while (prevista == null || !prevista.isAfter(hoje)) {
                System.out.print("Data devolucao prevista (AAAA-MM-DD): ");
                String input = sc.nextLine().trim();
                try {
                    prevista = LocalDate.parse(input);
                    if (!prevista.isAfter(hoje)) {
                        System.out.println("A data deve ser superior a data de saida (" + hoje + ").");
                    }
                } catch (Exception e) {
                    System.out.println("Formato invalido. Use AAAA-MM-DD.");
                }
            }

            System.out.print("Confirmar? (s/n): ");
            if (!sc.nextLine().equalsIgnoreCase("s")) {
                System.out.println("Reserva cancelada.");
                return;
            }

            boolean ok = resService.criar(hoje, prevista, idReq, idFunc, idExemplar);
            if (ok) {
                System.out.println("Reserva criada com sucesso!");
            } else {
                System.out.println("Nao foi possivel criar a reserva.");
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void registarDevolucao() {
        try {
            List<Reserva> lista = resService.listarEmAberto();
            System.out.println("\n--- Reservas em Aberto ---");
            for (Reserva reserva : lista) {
                System.out.println(reserva);
            }
            System.out.print("\nID da reserva a devolver: "); int id = sc.nextInt(); sc.nextLine();
            boolean ok = resService.registarDevolucao(id);
            if (ok) {
                System.out.println("Devolucao registada!");
            } else {
                System.out.println("Reserva nao encontrada ou ja devolvida.");
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void menuRequisitantes() {
        int opcao;
        do {
            System.out.println("\n|================================|");
            System.out.println("|    GESTAO DE REQUISITANTES     |");
            System.out.println("|================================|");
            System.out.println("| 1. Listar todos                |");
            System.out.println("| 2. Pesquisar por nome          |");
            System.out.println("| 3. Adicionar requisitante      |");
            System.out.println("| 4. Atualizar requisitante      |");
            System.out.println("| 5. Eliminar requisitante       |");
            System.out.println("| 0. Voltar                      |");
            System.out.println("|================================|");
            System.out.print("Opcao: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> listarTodosRequisitantes();
                case 2 -> pesquisarRequisitante();
                case 3 -> adicionarRequisitante();
                case 4 -> atualizarRequisitante();
                case 5 -> eliminarRequisitante();
                case 0 -> System.out.println("A voltar...");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void listarTodosRequisitantes() {
        try {
            List<Requisitante> lista = reqService.listarTodos();
            if (lista.isEmpty()) { System.out.println("Nenhum requisitante encontrado."); return; }
            System.out.println("\n--- Lista de Requisitantes ---");
            for (Requisitante r : lista){
                System.out.println(r);
            }
            System.out.println("Total: " + lista.size() + " requisitante(s).");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void pesquisarRequisitante() {
        System.out.print("Nome a pesquisar: ");
        String termo = sc.nextLine();
        try {
            List<Requisitante> lista = reqService.pesquisarPorNome(termo);
            if (lista.isEmpty()){
                System.out.println("Nenhum resultado encontrado.");
            }
            else{
                for (Requisitante r : lista){
                    System.out.println(r);
                }
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void adicionarRequisitante() {
        try {
            System.out.print("Nome: ");
            String nome = sc.nextLine();
            System.out.print("Contacto: ");
            String contacto = sc.nextLine();
            System.out.print("Codigo Postal: ");
            String nCodigoPostal = sc.nextLine();
            System.out.print("Localidade: ");
            String localidade = sc.nextLine();
            boolean ok = reqService.inserir(nome, contacto, nCodigoPostal, localidade);
            if (ok) {
                System.out.println("Requisitante adicionado!");
            } else {
                System.out.println("Nao foi possivel adicionar.");
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void atualizarRequisitante() {
        System.out.print("ID do requisitante: "); int id = sc.nextInt(); sc.nextLine();
        System.out.print("Novo nome: ");          String nome     = sc.nextLine();
        System.out.print("Novo contacto: ");      String contacto = sc.nextLine();
        try {
            boolean ok = reqService.atualizar(id, nome, contacto);
            if (ok) {
                System.out.println("Requisitante atualizado!");
            } else {
                System.out.println("Nao encontrado.");
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void eliminarRequisitante() {
        System.out.print("ID do requisitante a eliminar: "); int id = sc.nextInt(); sc.nextLine();
        System.out.print("Tem a certeza? (s/n): ");
        if (sc.nextLine().equalsIgnoreCase("s")) {
            try {
                boolean ok = reqService.eliminar(id);
                if (ok) {
                    System.out.println("Requisitante eliminado.");
                } else {
                    System.out.println("Nao encontrado.");
                }
            } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        }
    }

    private static void registarRequisitante() {
        System.out.println("\n|================================|");
        System.out.println("|    REGISTO DE REQUISITANTE     |");
        System.out.println("|================================|");
        adicionarRequisitante();
    }

    private static void menuRequisitante() {
        System.out.print("Qual o seu ID de requisitante: ");
        int idReq = sc.nextInt(); sc.nextLine();
        try {
            Requisitante req = reqService.buscarPorId(idReq);
            if (req == null) { System.out.println("Requisitante nao encontrado."); return; }
            System.out.println("Bem-vindo/a, " + req.getNome() + "!");

            int opcao;
            do {
                System.out.println("\n|================================|");
                System.out.println("|       AREA REQUISITANTE        |");
                System.out.println("|================================|");
                System.out.println("| 1. Ver livros disponiveis      |");
                System.out.println("| 2. Pesquisar livro por titulo  |");
                System.out.println("| 3. Ver as minhas reservas      |");
                System.out.println("| 4. Ver autores de um livro     |");
                System.out.println("| 0. Voltar                      |");
                System.out.println("|================================|");
                System.out.print("Opcao: ");
                opcao = sc.nextInt();
                sc.nextLine();

                switch (opcao) {
                    case 1 -> listarLivrosDisponiveis();
                    case 2 -> pesquisarLivro();
                    case 3 -> {
                        try {
                            List<Reserva> reservas = resService.listarPorRequisitante(idReq);
                            if (reservas.isEmpty()){
                                System.out.println("Nenhuma reserva encontrada.");
                            }
                            else{
                                for (Reserva r : reservas){
                                    System.out.println(r);
                                }
                            }
                        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
                    }
                    case 4 -> verAutoresLivro();
                    case 0 -> System.out.println("Ate logo!");
                    default -> System.out.println("Opcao invalida.");
                }
            } while (opcao != 0);

        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void menuAutores() {
        int opcao;
        do {
            System.out.println("\n|================================|");
            System.out.println("|        GESTAO DE AUTORES       |");
            System.out.println("|================================|");
            System.out.println("| 1. Listar todos os autores     |");
            System.out.println("| 2. Pesquisar autor por nome    |");
            System.out.println("| 0. Voltar                      |");
            System.out.println("|================================|");
            System.out.print("Opcao: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1 -> listarTodosAutores();
                case 2 -> pesquisarAutor();
                case 0 -> System.out.println("A voltar...");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void listarTodosAutores() {
        try {
            List<Autor> lista = autorService.listarTodos();
            if (lista.isEmpty()) { System.out.println("Nenhum autor encontrado."); return; }
            System.out.println("\n--- Lista de Autores ---");
            for (Autor a : lista){
                System.out.println(a);
            }
            System.out.println("Total: " + lista.size() + " autor(es).");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void pesquisarAutor() {
        System.out.print("Nome a pesquisar: "); String termo = sc.nextLine();
        try {
            List<Autor> lista = autorService.pesquisarPorNome(termo);
            if (lista.isEmpty()){
                System.out.println("Nenhum resultado encontrado.");
            }
            else{
                for (Autor a : lista){
                    System.out.println(a);
                }
            }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}