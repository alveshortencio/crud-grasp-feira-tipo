package feira.graspcrud.dto;

/**
 * Objeto de transferencia de dados para cadastro e atualizacao de Feira.
 *
 * <p>Padrao GRASP: Information Expert — carrega apenas os dados de entrada
 * necessarios para a operacao, sem conter regras de negocio.
 */
public class FeiraRequest {

    private final String nome;
    private final String logradouro;
    private final String bairro;
    private final boolean ativa;
    private final long tipoFeiraId;

    /**
     * Cria o DTO com os dados coletados no menu.
     *
     * @param nome        nome da feira
     * @param logradouro  logradouro do endereco
     * @param bairro      bairro do endereco
     * @param ativa       indica se a feira esta ativa
     * @param tipoFeiraId identificador do tipo de feira
     */
    public FeiraRequest(String nome, String logradouro, String bairro, boolean ativa, long tipoFeiraId) {
        this.nome = nome;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.ativa = ativa;
        this.tipoFeiraId = tipoFeiraId;
    }

    /**
     * Retorna o nome da feira.
     *
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o logradouro do endereco.
     *
     * @return logradouro
     */
    public String getLogradouro() {
        return logradouro;
    }

    /**
     * Retorna o bairro do endereco.
     *
     * @return bairro
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * Retorna se a feira esta ativa.
     *
     * @return true se ativa
     */
    public boolean isAtiva() {
        return ativa;
    }

    /**
     * Retorna o identificador do tipo de feira.
     *
     * @return tipoFeiraId
     */
    public long getTipoFeiraId() {
        return tipoFeiraId;
    }
}
