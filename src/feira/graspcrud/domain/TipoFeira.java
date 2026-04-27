package feira.graspcrud.domain;

import feira.graspcrud.exception.RegraNegocioException;

/**
 * Entidade de classificacao para feiras livres.
 *
 * <p>Padrao GRASP: Information Expert — esta classe conhece seus proprios dados
 * e, portanto, e responsavel por validar suas proprias regras de negocio.
 * Nenhuma regra de dominio de TipoFeira deve estar no Controller ou no Repositorio.
 */
public class TipoFeira {

    private Long id;
    private String nome;
    private String descricao;

    /**
     * Cria um TipoFeira com os dados fornecidos.
     *
     * @param id         identificador unico (pode ser nulo para novo cadastro)
     * @param nome       nome do tipo de feira (obrigatorio, minimo 3 caracteres)
     * @param descricao  descricao opcional do tipo de feira
     */
    public TipoFeira(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome == null ? null : nome.trim();
        this.descricao = descricao == null ? "" : descricao.trim();
    }

    /**
     * Valida as regras de negocio do TipoFeira.
     *
     * <p>Regras aplicadas:
     * <ul>
     *   <li>Nome e obrigatorio e deve ter ao menos 3 caracteres.</li>
     * </ul>
     *
     * @throws RegraNegocioException se alguma regra for violada
     */
    public void validar() {
        if (nome == null || nome.length() < 3) {
            throw new RegraNegocioException("Nome do tipo de feira deve ter ao menos 3 caracteres.");
        }
    }

    /**
     * Retorna o identificador unico do tipo de feira.
     *
     * @return id do tipo de feira
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador do tipo de feira.
     *
     * @param id identificador gerado pelo repositorio
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna o nome do tipo de feira.
     *
     * @return nome do tipo de feira
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descricao do tipo de feira.
     *
     * @return descricao do tipo de feira
     */
    public String getDescricao() {
        return descricao;
    }
}
