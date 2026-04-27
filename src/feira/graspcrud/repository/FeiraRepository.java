package feira.graspcrud.repository;

import feira.graspcrud.domain.Feira;

import java.util.List;

/**
 * Abstracao de persistencia para feiras livres.
 *
 * <p>Padrao GRASP: Protected Variations + Indirection — ao definir esta interface,
 * os servicos ficam protegidos contra variacoes na implementacao de persistencia.
 * Trocar JSON por banco de dados exige apenas criar uma nova classe implementadora,
 * sem alterar dominio ou servicos.
 */
public interface FeiraRepository {

    /**
     * Salva ou atualiza uma feira.
     *
     * @param feira entidade a ser persistida
     * @return entidade persistida com id gerado
     */
    Feira salvar(Feira feira);

    /**
     * Lista todas as feiras cadastradas.
     *
     * @return lista de feiras
     */
    List<Feira> listarTodos();

    /**
     * Busca uma feira pelo seu identificador.
     *
     * @param id identificador da feira
     * @return feira encontrada ou null se nao existir
     */
    Feira buscarPorId(long id);

    /**
     * Remove uma feira pelo seu identificador.
     *
     * @param id identificador da feira
     * @return true se removida com sucesso, false se nao encontrada
     */
    boolean remover(long id);

    /**
     * Verifica se ja existe uma feira ativa no mesmo endereco (logradouro + bairro),
     * ignorando o registro com o id especificado (util para atualizacao).
     *
     * @param logradouro logradouro a verificar
     * @param bairro     bairro a verificar
     * @param ignorarId  id a ignorar na comparacao (null para nenhum)
     * @return true se existir feira ativa no mesmo endereco
     */
    boolean existeFeiraAtivaNoEndereco(String logradouro, String bairro, Long ignorarId);

    /**
     * Verifica se ha alguma feira vinculada ao tipo de feira informado.
     *
     * @param tipoId identificador do tipo de feira
     * @return true se existir vinculo
     */
    boolean existePorTipo(long tipoId);
}
