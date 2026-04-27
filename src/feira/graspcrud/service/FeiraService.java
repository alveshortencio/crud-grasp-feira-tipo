package feira.graspcrud.service;

import feira.graspcrud.domain.Feira;
import feira.graspcrud.dto.FeiraRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.repository.FeiraRepository;
import feira.graspcrud.repository.TipoFeiraRepository;

import java.util.List;

/**
 * Servico de aplicacao responsavel pelos casos de uso de Feira.
 *
 * <p>Padrao GRASP: Low Coupling — depende de {@link FeiraRepository} e
 * {@link TipoFeiraRepository} por suas interfaces, sem conhecer a implementacao
 * concreta de persistencia. Isso facilita testes e substituicao de tecnologia.
 *
 * <p>Padrao GRASP: High Cohesion — cada metodo tem responsabilidade unica
 * e bem definida dentro dos casos de uso de Feira.
 */
public class FeiraService {

    private final FeiraRepository feiraRepository;
    private final TipoFeiraRepository tipoRepository;

    /**
     * Cria o servico com as dependencias fornecidas por abstracao.
     *
     * @param feiraRepository repositorio de feiras
     * @param tipoRepository  repositorio de tipos de feira
     */
    public FeiraService(FeiraRepository feiraRepository, TipoFeiraRepository tipoRepository) {
        this.feiraRepository = feiraRepository;
        this.tipoRepository = tipoRepository;
    }

    /**
     * Cadastra uma nova feira aplicando todas as regras de negocio.
     *
     * <p>Regras aplicadas:
     * <ul>
     *   <li>O tipo de feira informado deve existir.</li>
     *   <li>Nao e permitido cadastrar duas feiras ativas no mesmo endereco (logradouro + bairro).</li>
     *   <li>Demais validacoes de dominio delegadas a {@link Feira#validar()}.</li>
     * </ul>
     *
     * @param request dados de entrada da feira
     * @return feira cadastrada com id gerado
     * @throws RegraNegocioException se alguma regra for violada
     */
    public Feira criar(FeiraRequest request) {
        validarTipoExiste(request.getTipoFeiraId());
        if (request.isAtiva() && feiraRepository.existeFeiraAtivaNoEndereco(
                request.getLogradouro(), request.getBairro(), null)) {
            throw new RegraNegocioException(
                    "Ja existe uma feira ativa no endereco: " + request.getLogradouro() + ", " + request.getBairro() + ".");
        }
        Feira feira = new Feira(null, request.getNome(), request.getLogradouro(),
                request.getBairro(), request.isAtiva(), request.getTipoFeiraId());
        feira.validar();
        return feiraRepository.salvar(feira);
    }

    /**
     * Atualiza os dados de uma feira existente.
     *
     * <p>Regras aplicadas:
     * <ul>
     *   <li>A feira deve existir.</li>
     *   <li>O tipo de feira informado deve existir.</li>
     *   <li>Nao e permitido atualizar para um endereco ja ocupado por outra feira ativa.</li>
     *   <li>Demais validacoes de dominio delegadas a {@link Feira#validar()}.</li>
     * </ul>
     *
     * @param id      identificador da feira a ser atualizada
     * @param request novos dados da feira
     * @return feira atualizada
     * @throws RegraNegocioException se alguma regra for violada
     */
    public Feira atualizar(long id, FeiraRequest request) {
        buscarPorId(id);
        validarTipoExiste(request.getTipoFeiraId());
        if (request.isAtiva() && feiraRepository.existeFeiraAtivaNoEndereco(
                request.getLogradouro(), request.getBairro(), id)) {
            throw new RegraNegocioException(
                    "Ja existe uma feira ativa no endereco: " + request.getLogradouro() + ", " + request.getBairro() + ".");
        }
        Feira feira = new Feira(id, request.getNome(), request.getLogradouro(),
                request.getBairro(), request.isAtiva(), request.getTipoFeiraId());
        feira.validar();
        return feiraRepository.salvar(feira);
    }

    /**
     * Busca uma feira pelo seu identificador.
     *
     * @param id identificador da feira
     * @return feira encontrada
     * @throws RegraNegocioException se a feira nao existir
     */
    public Feira buscarPorId(long id) {
        Feira feira = feiraRepository.buscarPorId(id);
        if (feira == null) {
            throw new RegraNegocioException("Feira nao encontrada.");
        }
        return feira;
    }

    /**
     * Lista todas as feiras cadastradas.
     *
     * @return lista de feiras
     */
    public List<Feira> listarTodos() {
        return feiraRepository.listarTodos();
    }

    /**
     * Remove uma feira pelo seu identificador.
     *
     * @param id identificador da feira
     * @throws RegraNegocioException se a feira nao existir
     */
    public void remover(long id) {
        if (!feiraRepository.remover(id)) {
            throw new RegraNegocioException("Feira nao encontrada.");
        }
    }

    private void validarTipoExiste(long tipoId) {
        if (tipoRepository.buscarPorId(tipoId) == null) {
            throw new RegraNegocioException("Tipo de feira nao encontrado.");
        }
    }
}
