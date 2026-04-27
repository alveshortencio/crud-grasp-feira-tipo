package feira.graspcrud.dto;

/**
 * Objeto de transferencia de dados para cadastro e atualizacao de TipoFeira.
 *
 * <p>Padrao GRASP: Information Expert — carrega apenas os dados de entrada
 * necessarios para a operacao, sem conter regras de negocio.
 */
public class TipoFeiraRequest {

    private final String nome;
    private final String descricao;

    /**
     * Cria o DTO com os dados coletados no menu.
     *
     * @param nome      nome do tipo de feira
     * @param descricao descricao do tipo de feira
     */
    public TipoFeiraRequest(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    /**
     * Retorna o nome do tipo de feira.
     *
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descricao do tipo de feira.
     *
     * @return descricao
     */
    public String getDescricao() {
        return descricao;
    }
}
