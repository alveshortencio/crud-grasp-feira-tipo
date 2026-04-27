package feira.graspcrud.exception;

/**
 * Excecao de dominio para representar violacoes de regras de negocio.
 *
 * <p>Padrao GRASP: Pure Fabrication — classe criada para isolar o tratamento
 * de erros de dominio sem depender de nenhuma infraestrutura ou framework.
 * E capturada no Controller para exibir mensagens claras ao usuario no terminal.
 */
public class RegraNegocioException extends RuntimeException {

    /**
     * Cria a excecao com uma mensagem amigavel ao usuario.
     *
     * @param message descricao da violacao da regra de negocio
     */
    public RegraNegocioException(String message) {
        super(message);
    }
}
