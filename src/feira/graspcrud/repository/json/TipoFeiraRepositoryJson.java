package feira.graspcrud.repository.json;

import feira.graspcrud.domain.TipoFeira;
import feira.graspcrud.repository.TipoFeiraRepository;
import feira.graspcrud.util.JsonMini;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacao de {@link TipoFeiraRepository} com persistencia em arquivo JSON.
 *
 * <p>Padrao GRASP: Pure Fabrication — classe fabricada para isolar os detalhes
 * de persistencia em arquivo JSON, mantendo o dominio livre de infraestrutura.
 * Padrao GRASP: Indirection — serve como intermediario entre o servico (que
 * depende da interface) e o mecanismo concreto de persistencia JSON.
 */
public class TipoFeiraRepositoryJson implements TipoFeiraRepository {

    private final Path arquivo;
    private final List<TipoFeira> banco;
    private long proximoId;

    /**
     * Cria o repositorio carregando os dados existentes do arquivo JSON.
     * Se o arquivo nao existir, inicia com lista vazia.
     *
     * @param arquivo caminho para o arquivo JSON de tipos de feira
     */
    public TipoFeiraRepositoryJson(Path arquivo) {
        this.arquivo = arquivo;
        this.banco = JsonMini.carregarTiposFeira(arquivo);
        this.proximoId = banco.stream()
                .mapToLong(t -> t.getId() == null ? 0 : t.getId())
                .max().orElse(0) + 1;
    }

    /**
     * Salva ou atualiza um tipo de feira no arquivo JSON.
     * Se o id for nulo, gera novo id e insere; caso contrario, atualiza.
     *
     * @param tipo entidade a ser persistida
     * @return entidade persistida com id atribuido
     */
    @Override
    public TipoFeira salvar(TipoFeira tipo) {
        if (tipo.getId() == null) {
            tipo.setId(proximoId++);
            banco.add(tipo);
        } else {
            int idx = indexPorId(tipo.getId());
            if (idx >= 0) {
                banco.set(idx, tipo);
            } else {
                banco.add(tipo);
            }
        }
        JsonMini.salvarTiposFeira(arquivo, banco);
        return tipo;
    }

    /**
     * Retorna copia da lista de todos os tipos de feira.
     *
     * @return lista de tipos de feira
     */
    @Override
    public List<TipoFeira> listarTodos() {
        return new ArrayList<>(banco);
    }

    /**
     * Busca tipo de feira pelo id.
     *
     * @param id identificador
     * @return tipo encontrado ou null
     */
    @Override
    public TipoFeira buscarPorId(long id) {
        for (TipoFeira t : banco) {
            if (t.getId() != null && t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    /**
     * Verifica se ja existe tipo com o mesmo nome, ignorando o id informado.
     *
     * @param nome      nome a verificar
     * @param ignorarId id a ignorar (null para nenhum)
     * @return true se houver nome duplicado
     */
    @Override
    public boolean existeNome(String nome, Long ignorarId) {
        if (nome == null) return false;
        String alvo = nome.trim().toLowerCase();
        for (TipoFeira t : banco) {
            if (t.getNome() != null && t.getNome().trim().toLowerCase().equals(alvo)) {
                if (ignorarId == null || !ignorarId.equals(t.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Remove tipo de feira pelo id e atualiza o arquivo JSON.
     *
     * @param id identificador
     * @return true se removido com sucesso
     */
    @Override
    public boolean remover(long id) {
        int idx = indexPorId(id);
        if (idx < 0) return false;
        banco.remove(idx);
        JsonMini.salvarTiposFeira(arquivo, banco);
        return true;
    }

    private int indexPorId(long id) {
        for (int i = 0; i < banco.size(); i++) {
            if (banco.get(i).getId() != null && banco.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
