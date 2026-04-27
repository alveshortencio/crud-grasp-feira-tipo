package feira.graspcrud.controller;

import feira.graspcrud.domain.Feira;
import feira.graspcrud.domain.TipoFeira;
import feira.graspcrud.dto.FeiraRequest;
import feira.graspcrud.dto.TipoFeiraRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.service.FeiraService;
import feira.graspcrud.service.TipoFeiraService;

import java.util.List;
import java.util.Scanner;

/**
 * Controller responsavel por receber a entrada do menu textual e delegar
 * as operacoes aos servicos correspondentes.
 *
 * <p>Padrao GRASP: Controller — unico ponto de entrada para o menu textual.
 * Nao implementa regras de negocio; apenas conecta entrada do usuario aos
 * servicos e exibe resultados no terminal.
 *
 * <p>Padrao GRASP: Low Coupling — depende de {@link FeiraService} e
 * {@link TipoFeiraService} sem conhecer repositorios ou detalhes de persistencia.
 */
public class FeiraController {

    private final FeiraService feiraService;
    private final TipoFeiraService tipoService;
    private final Scanner scanner;

    /**
     * Cria o controller com as dependencias necessarias.
     *
     * @param feiraService servico de feira
     * @param tipoService  servico de tipo de feira
     * @param scanner      leitor de entrada do terminal
     */
    public FeiraController(FeiraService feiraService, TipoFeiraService tipoService, Scanner scanner) {
        this.feiraService = feiraService;
        this.tipoService = tipoService;
        this.scanner = scanner;
    }

    /**
     * Inicia o loop principal do menu textual, mantendo a aplicacao
     * em execucao ate o usuario escolher a opcao de sair.
     */
    public void iniciarMenu() {
        boolean executando = true;
        while (executando) {
            mostrarMenu();
            String opcao = scanner.nextLine().trim();
            try {
                switch (opcao) {
                    case "1": cadastrarTipo();    break;
                    case "2": listarTipos();      break;
                    case "3": cadastrarFeira();   break;
                    case "4": listarFeiras();     break;
                    case "5": buscarFeira();      break;
                    case "6": atualizarFeira();   break;
                    case "7": excluirFeira();     break;
                    case "8": excluirTipo();      break;
                    case "9": executando = false; break;
                    default:  System.out.println("Opcao invalida. Tente novamente."); break;
                }
            } catch (RegraNegocioException e) {
                System.out.println("[ERRO DE NEGOCIO] " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("[ERRO] Valor numerico invalido. Digite apenas numeros onde solicitado.");
            } catch (Exception e) {
                System.out.println("[ERRO] " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void mostrarMenu() {
        System.out.println("============================================");
        System.out.println("   SISTEMA DE GESTAO DE FEIRAS LIVRES");
        System.out.println("============================================");
        System.out.println("1. Cadastrar tipo de feira");
        System.out.println("2. Listar tipos de feira");
        System.out.println("3. Cadastrar feira");
        System.out.println("4. Listar feiras");
        System.out.println("5. Buscar feira por ID");
        System.out.println("6. Atualizar feira");
        System.out.println("7. Excluir feira");
        System.out.println("8. Excluir tipo de feira");
        System.out.println("9. Sair");
        System.out.print("Escolha uma opcao: ");
    }

    private void cadastrarTipo() {
        System.out.print("Nome do tipo de feira: ");
        String nome = scanner.nextLine();
        System.out.print("Descricao do tipo (opcional): ");
        String descricao = scanner.nextLine();

        TipoFeira tipo = tipoService.criar(new TipoFeiraRequest(nome, descricao));
        System.out.println("Tipo de feira cadastrado com ID: " + tipo.getId());
    }

    private void listarTipos() {
        List<TipoFeira> tipos = tipoService.listarTodos();
        if (tipos.isEmpty()) {
            System.out.println("Nenhum tipo de feira cadastrado.");
            return;
        }
        System.out.println("--- Tipos de Feira ---");
        for (TipoFeira t : tipos) {
            System.out.printf("ID: %d | Nome: %s | Descricao: %s%n",
                    t.getId(), t.getNome(), t.getDescricao());
        }
    }

    private void cadastrarFeira() {
        if (tipoService.listarTodos().isEmpty()) {
            throw new RegraNegocioException("Cadastre ao menos um tipo de feira antes de cadastrar uma feira.");
        }
        listarTipos();

        System.out.print("Nome da feira: ");
        String nome = scanner.nextLine();
        System.out.print("Logradouro: ");
        String logradouro = scanner.nextLine();
        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();
        System.out.print("Feira ativa? (s/n): ");
        boolean ativa = scanner.nextLine().trim().equalsIgnoreCase("s");
        System.out.print("ID do tipo de feira: ");
        long tipoId = Long.parseLong(scanner.nextLine());

        Feira feira = feiraService.criar(new FeiraRequest(nome, logradouro, bairro, ativa, tipoId));
        System.out.println("Feira cadastrada com ID: " + feira.getId());
    }

    private void listarFeiras() {
        List<Feira> feiras = feiraService.listarTodos();
        if (feiras.isEmpty()) {
            System.out.println("Nenhuma feira cadastrada.");
            return;
        }
        System.out.println("--- Feiras Cadastradas ---");
        for (Feira f : feiras) {
            TipoFeira t = tipoService.buscarPorId(f.getTipoFeiraId());
            System.out.printf("ID: %d | Nome: %s | %s, %s | Ativa: %s | Tipo: %s%n",
                    f.getId(), f.getNome(), f.getLogradouro(), f.getBairro(),
                    f.isAtiva() ? "Sim" : "Nao", t.getNome());
        }
    }

    private void buscarFeira() {
        System.out.print("ID da feira: ");
        long id = Long.parseLong(scanner.nextLine());
        Feira f = feiraService.buscarPorId(id);
        TipoFeira t = tipoService.buscarPorId(f.getTipoFeiraId());

        System.out.println("--- Dados da Feira ---");
        System.out.printf("ID: %d%n", f.getId());
        System.out.printf("Nome: %s%n", f.getNome());
        System.out.printf("Endereco: %s, %s%n", f.getLogradouro(), f.getBairro());
        System.out.printf("Ativa: %s%n", f.isAtiva() ? "Sim" : "Nao");
        System.out.printf("Tipo: %s%n", t.getNome());
    }

    private void atualizarFeira() {
        System.out.print("ID da feira para atualizar: ");
        long id = Long.parseLong(scanner.nextLine());

        listarTipos();

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Novo logradouro: ");
        String logradouro = scanner.nextLine();
        System.out.print("Novo bairro: ");
        String bairro = scanner.nextLine();
        System.out.print("Feira ativa? (s/n): ");
        boolean ativa = scanner.nextLine().trim().equalsIgnoreCase("s");
        System.out.print("Novo ID do tipo de feira: ");
        long tipoId = Long.parseLong(scanner.nextLine());

        feiraService.atualizar(id, new FeiraRequest(nome, logradouro, bairro, ativa, tipoId));
        System.out.println("Feira atualizada com sucesso.");
    }

    private void excluirFeira() {
        System.out.print("ID da feira para excluir: ");
        long id = Long.parseLong(scanner.nextLine());
        feiraService.remover(id);
        System.out.println("Feira excluida com sucesso.");
    }

    private void excluirTipo() {
        System.out.print("ID do tipo de feira para excluir: ");
        long id = Long.parseLong(scanner.nextLine());
        tipoService.remover(id);
        System.out.println("Tipo de feira excluido com sucesso.");
    }
}
