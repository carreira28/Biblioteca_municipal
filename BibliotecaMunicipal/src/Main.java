import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final String PASS_FUNC = "admin123";
    private static final LivroService livroService  = new LivroService();
    private static final RequisitanteService reqService = new RequisitanteService();
    private static final ReservaService resService = new ReservaService();

    public static void main(String[] args) {
        int opcao;
        do {
            System.out.println("\n================================");
            System.out.println("   BIBLIOTECA MUNICIPAL     ");
            System.out.println("================================");
            System.out.println(" 1. Entrar como Funcionario");
            System.out.println(" 2. Entrar como Requisitante");
            System.out.println(" 0. Sair");
            System.out.println("================================");
            System.out.print("Opcao: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> loginFuncionario();
                case 2 -> menuRequisitante();
                case 0 -> System.out.println("\nAte logo!");
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
            System.out.println("\n================================");
            System.out.println("        AREA FUNCIONARIO        ");
            System.out.println("================================");
            System.out.println(" 1. Gerir Livros");
            System.out.println(" 2. Gerir Reservas");
            System.out.println(" 3. Gerir Requisitantes");
            System.out.println(" 0. Voltar");
            System.out.println("================================");
            System.out.print("Opcao: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> menuLivros();
                case 2 -> menuReservas();
                case 3 -> menuRequisitantes();
                case 0 -> System.out.println("A voltar...");
                default -> System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);
    }

    private static void menuLivros() {
        int opcao;
        do {
            System.out.println("\n================================");
            System.out.println("        GESTAO DE LIVROS        ");
            System.out.println("================================");
            System.out.println(" 1. Listar todos os livros");
            System.out.println(" 2. Listar livros disponiveis");
            System.out.println(" 3. Pesquisar por titulo");
            System.out.println(" 4. Adicionar livro");
            System.out.println(" 5. Atualizar livro");
            System.out.println(" 6. Eliminar livro");
            System.out.println(" 0. Voltar");
            System.out.println("================================");
            System.out.print("Opcao: ");
            opcao = lerInteiro();

            switch (opcao) {
                case 1 -> listarTodosLivros();
                case 2 -> listarLivrosDisponiveis();
                case 3 -> pesquisarLivro();
                case 4 -> adicionarLivro();
                case 5 -> atualizarLivro();
                case 6 -> eliminarLivro();
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
            livros.forEach(System.out::println);
            System.out.println("Total: " + livros.size() + " livro(s).");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void listarLivrosDisponiveis() {
        try {
            List<Livro> livros = livroService.listarDisponiveis();
            if (livros.isEmpty()) { System.out.println("Nenhum livro disponivel."); return; }
            System.out.println("\n--- Livros Disponiveis ---");
            livros.forEach(System.out::println);
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void pesquisarLivro() {
        System.out.print("Titulo a pesquisar: ");
        String termo = sc.nextLine();
        try {
            List<Livro> livros = livroService.pesquisarPorTitulo(termo);
            if (livros.isEmpty()) System.out.println("Nenhum resultado encontrado.");
            else livros.forEach(System.out::println);
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void adicionarLivro() {
        try {
            livroService.listarCategorias();
            System.out.print("ID Categoria: ");  int idCat    = lerInteiro();
            livroService.listarIdiomas();
            System.out.print("ID Idioma: ");     int idIdioma = lerInteiro();
            livroService.listarEditoras();
            System.out.print("ID Editora: ");    int idEdit   = lerInteiro();
            livroService.listarGeneros();
            System.out.print("ID Genero: ");     int idGenero = lerInteiro();
            System.out.print("ISBN: ");          String isbn   = sc.nextLine();
            System.out.print("Titulo: ");        String titulo = sc.nextLine();
            System.out.print("Stock inicial: "); int stock     = lerInteiro();
            System.out.print("Ano publicacao: "); int ano      = lerInteiro();

            boolean ok = livroService.inserir(isbn, titulo, stock, ano,
                    idCat, idIdioma, idEdit, idGenero);
            System.out.println(ok ? "Livro adicionado com sucesso!" : "Nao foi possivel adicionar.");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void atualizarLivro() {
        System.out.print("ID do livro: "); int id    = lerInteiro();
        System.out.print("Novo titulo: "); String titulo = sc.nextLine();
        System.out.print("Novo stock: ");  int stock = lerInteiro();
        try {
            boolean ok = livroService.atualizar(id, titulo, stock);
            System.out.println(ok ? "Livro atualizado!" : "Livro nao encontrado.");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void eliminarLivro() {
        System.out.print("ID do livro a eliminar: "); int id = lerInteiro();
        System.out.print("Tem a certeza? (s/n): ");
        if (sc.nextLine().equalsIgnoreCase("s")) {
            try {
                boolean ok = livroService.eliminar(id);
                System.out.println(ok ? "Livro eliminado." : "Livro nao encontrado.");
            } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        }
    }

    private static void menuReservas() {
        int opcao;
        do {
            System.out.println("\n================================");
            System.out.println("       GESTAO DE RESERVAS       ");
            System.out.println("================================");
            System.out.println(" 1. Listar todas as reservas");
            System.out.println(" 2. Listar reservas em aberto");
            System.out.println(" 3. Listar reservas atrasadas");
            System.out.println(" 4. Reservas de um requisitante");
            System.out.println(" 5. Nova reserva");
            System.out.println(" 6. Registar devolucao");
            System.out.println(" 0. Voltar");
            System.out.println("================================");
            System.out.print("Opcao: ");
            opcao = lerInteiro();

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
            lista.forEach(System.out::println);
            System.out.println("Total: " + lista.size() + " reserva(s).");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void listarReservasEmAberto() {
        try {
            List<Reserva> lista = resService.listarEmAberto();
            if (lista.isEmpty()) { System.out.println("Nenhuma reserva em aberto."); return; }
            System.out.println("\n--- Reservas em Aberto ---");
            lista.forEach(System.out::println);
            System.out.println("Total: " + lista.size() + " reserva(s) em aberto.");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void listarReservasAtrasadas() {
        try {
            List<Reserva> lista = resService.listarAtrasadas();
            if (lista.isEmpty()) { System.out.println("Nenhuma reserva atrasada."); return; }
            System.out.println("\n--- Reservas ATRASADAS ---");
            lista.forEach(System.out::println);
            System.out.println("Total: " + lista.size() + " reserva(s) atrasada(s).");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void reservasPorRequisitante() {
        System.out.print("ID do requisitante: "); int id = lerInteiro();
        try {
            List<Reserva> lista = resService.listarPorRequisitante(id);
            if (lista.isEmpty()) System.out.println("Nenhuma reserva encontrada.");
            else lista.forEach(System.out::println);
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void novaReserva() {
        try {
            System.out.println("\n--- Livros com exemplares disponiveis ---");
            livroService.listarDisponiveis().forEach(System.out::println);

            System.out.print("\nID do livro: ");    int idLivro    = lerInteiro();
            resService.listarExemplaresDisponiveis(idLivro);
            System.out.print("ID do exemplar: "); int idExemplar = lerInteiro();

            if (!resService.exemplarDisponivel(idExemplar)) {
                System.out.println("Exemplar nao disponivel.");
                return;
            }

            resService.listarFuncionarios();
            System.out.print("ID do funcionario: "); int idFunc = lerInteiro();
            System.out.print("ID do requisitante: "); int idReq = lerInteiro();

            LocalDate hoje     = LocalDate.now();
            LocalDate prevista = hoje.plusDays(15);

            System.out.println("Data de saida: "           + hoje);
            System.out.println("Data devolucao prevista: " + prevista + " (15 dias)");
            System.out.print("Confirmar? (s/n): ");
            if (!sc.nextLine().equalsIgnoreCase("s")) {
                System.out.println("Reserva cancelada.");
                return;
            }

            boolean ok = resService.criar(hoje, prevista, idReq, idFunc, idExemplar);
            System.out.println(ok ? "Reserva criada com sucesso!" : "Nao foi possivel criar a reserva.");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void registarDevolucao() {
        try {
            System.out.println("\n--- Reservas em Aberto ---");
            resService.listarEmAberto().forEach(System.out::println);
            System.out.print("\nID da reserva a devolver: "); int id = lerInteiro();
            boolean ok = resService.registarDevolucao(id);
            System.out.println(ok ? "Devolucao registada!" : "Reserva nao encontrada ou ja devolvida.");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void menuRequisitantes() {
        int opcao;
        do {
            System.out.println("\n================================");
            System.out.println("    GESTAO DE REQUISITANTES     ");
            System.out.println("================================");
            System.out.println(" 1. Listar todos");
            System.out.println(" 2. Pesquisar por nome");
            System.out.println(" 3. Adicionar requisitante");
            System.out.println(" 4. Atualizar requisitante");
            System.out.println(" 5. Eliminar requisitante");
            System.out.println(" 0. Voltar");
            System.out.println("================================");
            System.out.print("Opcao: ");
            opcao = lerInteiro();

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
            lista.forEach(System.out::println);
            System.out.println("Total: " + lista.size() + " requisitante(s).");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void pesquisarRequisitante() {
        System.out.print("Nome a pesquisar: ");
        String termo = sc.nextLine();
        try {
            List<Requisitante> lista = reqService.pesquisarPorNome(termo);
            if (lista.isEmpty()) System.out.println("Nenhum resultado encontrado.");
            else lista.forEach(System.out::println);
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void adicionarRequisitante() {
        try {
            reqService.listarCodigosPostais();
            System.out.print("ID Codigo Postal: "); int idCP     = lerInteiro();
            System.out.print("Nome: ");             String nome      = sc.nextLine();
            System.out.print("Contacto: ");         String contacto  = sc.nextLine();
            boolean ok = reqService.inserir(nome, contacto, idCP);
            System.out.println(ok ? "Requisitante adicionado!" : "Nao foi possivel adicionar.");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void atualizarRequisitante() {
        System.out.print("ID do requisitante: ");
        int id = lerInteiro();
        System.out.print("Novo nome: ");
        String nome = sc.nextLine();
        System.out.print("Novo contacto: ");
        String contacto = sc.nextLine();
        try {
            boolean ok = reqService.atualizar(id, nome, contacto);
            System.out.println(ok ? "Requisitante atualizado!" : "Nao encontrado.");
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static void eliminarRequisitante() {
        System.out.print("ID do requisitante a eliminar: "); int id = lerInteiro();
        System.out.print("Tem a certeza? (s/n): ");
        if (sc.nextLine().equalsIgnoreCase("s")) {
            try {
                boolean ok = reqService.eliminar(id);
                System.out.println(ok ? "Requisitante eliminado." : "Nao encontrado.");
            } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        }
    }

    private static void menuRequisitante() {
        System.out.print("O seu ID de requisitante: "); int idReq = lerInteiro();
        try {
            Requisitante req = reqService.buscarPorId(idReq);
            if (req == null) { System.out.println("Requisitante nao encontrado."); return; }
            System.out.println("Bem-vindo/a, " + req.getNome() + "!");

            int opcao;
            do {
                System.out.println("\n================================");
                System.out.println("       AREA REQUISITANTE        ");
                System.out.println("================================");
                System.out.println(" 1. Ver livros disponiveis");
                System.out.println(" 2. Pesquisar livro por titulo");
                System.out.println(" 3. Ver as minhas reservas");
                System.out.println(" 0. Voltar");
                System.out.println("================================");
                System.out.print("Opcao: ");
                opcao = lerInteiro();

                switch (opcao) {
                    case 1 -> listarLivrosDisponiveis();
                    case 2 -> pesquisarLivro();
                    case 3 -> {
                        try {
                            List<Reserva> reservas = resService.listarPorRequisitante(idReq);
                            if (reservas.isEmpty()) System.out.println("Nenhuma reserva encontrada.");
                            else reservas.forEach(System.out::println);
                        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
                    }
                    case 0 -> System.out.println("Ate logo!");
                    default -> System.out.println("Opcao invalida.");
                }
            } while (opcao != 0);

        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    private static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Valor invalido. Tente novamente: ");
            }
        }
    }
}