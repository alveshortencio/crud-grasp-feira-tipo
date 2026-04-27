package feira.graspcrud.service;

import feira.graspcrud.domain.TipoFeira;
import feira.graspcrud.dto.TipoFeiraRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.repository.FeiraRepository;
import feira.graspcrud.repository.TipoFeiraRepository;

import java.util.List;

/**
 * Servico de aplicacao responsavel pelos casos de uso de TipoFeira.
 *
 * <p>Padrao GRASP: Low Coupling — depende de {@link TipoFeiraRepository} e
 * {@link FeiraRepository} por suas interfaces, sem conhecer a implementacao
 * concreta de persistencia. Isso facilita testes e substituicao de tecnologia.
 *
 * <p>Padrao GRASP: High Cohesion — cada metodo tem responsabilidade unica
 * e bem definida dentro dos casos de uso de TipoFeira.
 */
public class TipoFeiraService {

    private final TipoFeiraRepository tipoRepository;
    private final FeiraRepository feiraRepository;

    /**
     * Cria o servico com as dependencias fornecidas por abstracao.
     *
     * @param tipoRepository  repositorio de tipos de feira
     * @param feiraRepository repositorio de feiras (necessario para validar remocao)
     */
    public TipoFeiraService(TipoFeiraRepository tipoRepository, FeiraRepository feiraRepository) {
        this.tipoRepository = tipoRepository;
        this.feiraRepository = feiraRepository;
    }

    /**
     * Cadastra um novo tipo de feira, garantindo unicidade do nome.
     *
     * <p>Regras aplicadas:
     * <ul>
     *   <li>Nome do tipo deve ser unico no cadastro.</li>
     *   <li>Validacoes de dominio delegadas a {@link TipoFeira#validar()}.</li>
     * </ul>
     *
     * @param request dados de entrada com nome e descricao
     * @return tipo de feira cadastrado com id gerado
     * @throws RegraNegocioException se o nome ja estiver em uso
     */
    public TipoFeira criar(TipoFeiraRequest request) {
        if (tipoRepository.existeNome(request.getNome(), null)) {
            throw new RegraNegocioException("Ja existe um tipo de feira com este nome.");
        }
        TipoFeira tipo = new TipoFeira(null, request.getNome(), request.getDescricao());
        tipo.validar();
        return tipoRepository.salvar(tipo);
    }

    /**
     * Lista todos os tipos de feira cadastrados.
     *
     * @return lista de tipos de feira
     */
    public List<TipoFeira> listarTodos() {
        return tipoRepository.listarTodos();
    }

    /**
     * Busca um tipo de feira pelo seu identificador.
     *
     * @param id identificador do tipo de feira
     * @return tipo de feira encontrado
     * @throws RegraNegocioException se o tipo nao existir
     */
    public TipoFeira buscarPorId(long id) {
        TipoFeira tipo = tipoRepository.buscarPorId(id);
        if (tipo == null) {
            throw new RegraNegocioException("Tipo de feira nao encontrado.");
        }
        return tipo;
    }

    /**
     * Remove um tipo de feira pelo seu identificador.
     *
     * <p>Regras aplicadas:
     * <ul>
     *   <li>Nao e permitido remover tipo que esteja em uso por alguma feira.</li>
     * </ul>
     *
     * @param id identificador do tipo de feira
     * @throws RegraNegocioException se o tipo estiver em uso ou nao existir
     */
    public void remover(long id) {
        if (feiraRepository.existePorTipo(id)) {
            throw new RegraNegocioException("Nao e permitido remover tipo de feira em uso por alguma feira.");
        }
        if (!tipoRepository.remover(id)) {
            throw new RegraNegocioException("Tipo de feira nao encontrado.");
        }
    }
}
