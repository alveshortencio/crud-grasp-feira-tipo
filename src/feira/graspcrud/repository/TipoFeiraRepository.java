package feira.graspcrud.repository;

import feira.graspcrud.domain.TipoFeira;

import java.util.List;

/**
 * Abstracao de persistencia para tipos de feira.
 *
 * <p>Padrao GRASP: Protected Variations + Indirection — ao definir esta interface,
 * os servicos ficam protegidos contra variacoes na implementacao de persistencia.
 * Trocar JSON por banco de dados exige apenas criar uma nova classe implementadora,
 * sem alterar dominio ou servicos.
 */
public interface TipoFeiraRepository {

    /**
     * Salva ou atualiza um tipo de feira.
     *
     * @param tipo entidade a ser persistida
     * @return entidade persistida com id gerado
     */
    TipoFeira salvar(TipoFeira tipo);

    /**
     * Lista todos os tipos de feira cadastrados.
     *
     * @return lista de tipos de feira
     */
    List<TipoFeira> listarTodos();

    /**
     * Busca um tipo de feira pelo seu identificador.
     *
     * @param id identificador do tipo de feira
     * @return tipo encontrado ou null se nao existir
     */
    TipoFeira buscarPorId(long id);

    /**
     * Verifica se ja existe um tipo de feira com o nome informado,
     * ignorando o registro com o id especificado (util para atualizacao).
     *
     * @param nome      nome a verificar
     * @param ignorarId id a ignorar na comparacao (null para nenhum)
     * @return true se existir duplicidade
     */
    boolean existeNome(String nome, Long ignorarId);

    /**
     * Remove um tipo de feira pelo seu identificador.
     *
     * @param id identificador do tipo de feira
     * @return true se removido com sucesso, false se nao encontrado
     */
    boolean remover(long id);
}
