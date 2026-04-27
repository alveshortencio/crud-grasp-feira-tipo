package feira.graspcrud.domain;

import feira.graspcrud.exception.RegraNegocioException;

/**
 * Entidade que representa uma feira livre cadastrada no sistema.
 *
 * <p>Padrao GRASP: Information Expert — esta classe conhece seus proprios dados
 * (nome, logradouro, bairro, tipoFeiraId) e e responsavel por validar suas
 * proprias regras de negocio. Nenhuma regra de dominio da Feira deve estar
 * no Controller ou no Repositorio.
 */
public class Feira {

    private Long id;
    private String nome;
    private String logradouro;
    private String bairro;
    private boolean ativa;
    private long tipoFeiraId;

    /**
     * Cria uma Feira com os dados fornecidos.
     *
     * @param id          identificador unico (pode ser nulo para novo cadastro)
     * @param nome        nome da feira (obrigatorio, minimo 3 caracteres)
     * @param logradouro  logradouro do endereco da feira (obrigatorio)
     * @param bairro      bairro do endereco da feira (obrigatorio)
     * @param ativa       indica se a feira esta ativa
     * @param tipoFeiraId identificador do tipo de feira associado (obrigatorio)
     */
    public Feira(Long id, String nome, String logradouro, String bairro, boolean ativa, long tipoFeiraId) {
        this.id = id;
        this.nome = nome == null ? null : nome.trim();
        this.logradouro = logradouro == null ? null : logradouro.trim();
        this.bairro = bairro == null ? null : bairro.trim();
        this.ativa = ativa;
        this.tipoFeiraId = tipoFeiraId;
    }

    /**
     * Valida as regras de negocio da Feira.
     *
     * <p>Regras aplicadas:
     * <ul>
     *   <li>Nome e obrigatorio e deve ter ao menos 3 caracteres.</li>
     *   <li>Logradouro e obrigatorio e deve ter ao menos 3 caracteres.</li>
     *   <li>Bairro e obrigatorio e deve ter ao menos 3 caracteres.</li>
     *   <li>O TipoFeira associado e obrigatorio (id valido).</li>
     * </ul>
     *
     * @throws RegraNegocioException se alguma regra for violada
     */
    public void validar() {
        if (nome == null || nome.length() < 3) {
            throw new RegraNegocioException("Nome da feira deve ter ao menos 3 caracteres.");
        }
        if (logradouro == null || logradouro.isBlank()) {
            throw new RegraNegocioException("Logradouro e obrigatorio.");
        }
        if (bairro == null || bairro.isBlank()) {
            throw new RegraNegocioException("Bairro e obrigatorio.");
        }
        if (tipoFeiraId <= 0) {
            throw new RegraNegocioException("Tipo de feira invalido.");
        }
    }

    /**
     * Retorna o identificador unico da feira.
     *
     * @return id da feira
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador da feira.
     *
     * @param id identificador gerado pelo repositorio
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna o nome da feira.
     *
     * @return nome da feira
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o logradouro do endereco da feira.
     *
     * @return logradouro da feira
     */
    public String getLogradouro() {
        return logradouro;
    }

    /**
     * Retorna o bairro do endereco da feira.
     *
     * @return bairro da feira
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * Retorna se a feira esta ativa.
     *
     * @return true se a feira estiver ativa
     */
    public boolean isAtiva() {
        return ativa;
    }

    /**
     * Retorna o identificador do tipo de feira associado.
     *
     * @return id do tipo de feira
     */
    public long getTipoFeiraId() {
        return tipoFeiraId;
    }
}
