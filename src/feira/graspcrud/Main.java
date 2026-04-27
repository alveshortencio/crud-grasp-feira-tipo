package feira.graspcrud;

import feira.graspcrud.controller.FeiraController;
import feira.graspcrud.repository.FeiraRepository;
import feira.graspcrud.repository.TipoFeiraRepository;
import feira.graspcrud.repository.json.FeiraRepositoryJson;
import feira.graspcrud.repository.json.TipoFeiraRepositoryJson;
import feira.graspcrud.service.FeiraService;
import feira.graspcrud.service.TipoFeiraService;

import java.nio.file.Path;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicacao de gestao de feiras livres.
 *
 * <p>Padrao GRASP: Creator — esta classe e responsavel por instanciar e
 * conectar todas as dependencias manualmente (sem framework de injecao),
 * pois e quem possui os dados necessarios para criacao de cada objeto.
 */
public class Main {

    private static final Path TIPOS_FILE = Path.of("data", "tipos-feira.json");
    private static final Path FEIRAS_FILE = Path.of("data", "feiras.json");

    /**
     * Inicializa as dependencias e executa o menu textual no terminal.
     *
     * @param args argumentos de linha de comando (nao utilizados)
     */
    public static void main(String[] args) {
        TipoFeiraRepository tipoRepository = new TipoFeiraRepositoryJson(TIPOS_FILE);
        FeiraRepository feiraRepository = new FeiraRepositoryJson(FEIRAS_FILE);

        TipoFeiraService tipoService = new TipoFeiraService(tipoRepository, feiraRepository);
        FeiraService feiraService = new FeiraService(feiraRepository, tipoRepository);

        try (Scanner scanner = new Scanner(System.in)) {
            FeiraController controller = new FeiraController(feiraService, tipoService, scanner);
            controller.iniciarMenu();
        }

        System.out.println("Aplicacao finalizada.");
    }
}
