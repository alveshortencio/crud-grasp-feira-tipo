package feira.graspcrud.repository.json;

import feira.graspcrud.domain.Feira;
import feira.graspcrud.repository.FeiraRepository;
import feira.graspcrud.util.JsonMini;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacao de {@link FeiraRepository} com persistencia em arquivo JSON.
 *
 * <p>Padrao GRASP: Pure Fabrication — classe fabricada para isolar os detalhes
 * de persistencia em arquivo JSON, mantendo o dominio livre de infraestrutura.
 * Padrao GRASP: Indirection — serve como intermediario entre o servico (que
 * depende da interface) e o mecanismo concreto de persistencia JSON.
 */
public class FeiraRepositoryJson implements FeiraRepository {

    private final Path arquivo;
    private final List<Feira> banco;
    private long proximoId;

    /**
     * Cria o repositorio carregando os dados existentes do arquivo JSON.
     * Se o arquivo nao existir, inicia com lista vazia.
     *
     * @param arquivo caminho para o arquivo JSON de feiras
     */
    public FeiraRepositoryJson(Path arquivo) {
        this.arquivo = arquivo;
        this.banco = JsonMini.carregarFeiras(arquivo);
        this.proximoId = banco.stream()
                .mapToLong(f -> f.getId() == null ? 0 : f.getId())
                .max().orElse(0) + 1;
    }

    /**
     * Salva ou atualiza uma feira no arquivo JSON.
     * Se o id for nulo, gera novo id e insere; caso contrario, atualiza.
     *
     * @param feira entidade a ser persistida
     * @return entidade persistida com id atribuido
     */
    @Override
    public Feira salvar(Feira feira) {
        if (feira.getId() == null) {
            feira.setId(proximoId++);
            banco.add(feira);
        } else {
            int idx = indexPorId(feira.getId());
            if (idx >= 0) {
                banco.set(idx, feira);
            } else {
                banco.add(feira);
            }
        }
        JsonMini.salvarFeiras(arquivo, banco);
        return feira;
    }

    /**
     * Retorna copia da lista de todas as feiras.
     *
     * @return lista de feiras
     */
    @Override
    public List<Feira> listarTodos() {
        return new ArrayList<>(banco);
    }

    /**
     * Busca feira pelo id.
     *
     * @param id identificador
     * @return feira encontrada ou null
     */
    @Override
    public Feira buscarPorId(long id) {
        for (Feira f : banco) {
            if (f.getId() != null && f.getId() == id) {
                return f;
            }
        }
        return null;
    }

    /**
     * Remove feira pelo id e atualiza o arquivo JSON.
     *
     * @param id identificador
     * @return true se removida com sucesso
     */
    @Override
    public boolean remover(long id) {
        int idx = indexPorId(id);
        if (idx < 0) return false;
        banco.remove(idx);
        JsonMini.salvarFeiras(arquivo, banco);
        return true;
    }

    /**
     * Verifica se existe feira ativa com o mesmo logradouro e bairro,
     * ignorando o id informado (para permitir atualizacao no mesmo endereco).
     *
     * @param logradouro logradouro a verificar
     * @param bairro     bairro a verificar
     * @param ignorarId  id a ignorar (null para nenhum)
     * @return true se existir feira ativa no mesmo endereco
     */
    @Override
    public boolean existeFeiraAtivaNoEndereco(String logradouro, String bairro, Long ignorarId) {
        if (logradouro == null || bairro == null) return false;
        String alvoLog = logradouro.trim().toLowerCase();
        String alvoBairro = bairro.trim().toLowerCase();
        for (Feira f : banco) {
            if (!f.isAtiva()) continue;
            if (ignorarId != null && ignorarId.equals(f.getId())) continue;
            if (f.getLogradouro() != null && f.getBairro() != null
                    && f.getLogradouro().trim().toLowerCase().equals(alvoLog)
                    && f.getBairro().trim().toLowerCase().equals(alvoBairro)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se ha alguma feira vinculada ao tipo informado.
     *
     * @param tipoId identificador do tipo de feira
     * @return true se existir vinculo
     */
    @Override
    public boolean existePorTipo(long tipoId) {
        for (Feira f : banco) {
            if (f.getTipoFeiraId() == tipoId) {
                return true;
            }
        }
        return false;
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
