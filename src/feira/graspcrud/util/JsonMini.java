package feira.graspcrud.util;

import feira.graspcrud.domain.Feira;
import feira.graspcrud.domain.TipoFeira;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitario de leitura e escrita JSON sem uso de bibliotecas externas.
 *
 * <p>Padrao GRASP: Pure Fabrication — classe criada exclusivamente para lidar
 * com persistencia em arquivo JSON, mantendo o dominio (Feira, TipoFeira)
 * completamente livre de detalhes de infraestrutura.
 *
 * <p>Suporta apenas a estrutura JSON necessaria para este sistema (arrays de
 * objetos simples com campos primitivos e string). Nao e um parser JSON generico.
 */
public class JsonMini {

    private static final Pattern OBJ_PATTERN = Pattern.compile("\\{([^}]*)}");
    private static final Pattern FIELD_PATTERN = Pattern.compile(
            "\"([^\"]+)\"\\s*:\\s*(\"(?:\\\\.|[^\"])*\"|-?\\d+(?:\\.\\d+)?|true|false|null)");

    /**
     * Carrega a lista de tipos de feira a partir do arquivo JSON informado.
     * Retorna lista vazia se o arquivo nao existir ou estiver vazio.
     *
     * @param arquivo caminho do arquivo JSON
     * @return lista de tipos de feira carregados
     */
    public static List<TipoFeira> carregarTiposFeira(Path arquivo) {
        List<TipoFeira> tipos = new ArrayList<>();
        String content = lerArquivo(arquivo);
        if (content == null || content.trim().isEmpty()) return tipos;
        for (Map<String, String> m : parseArray(content)) {
            Long id = parseLong(m.get("id"));
            String nome = parseString(m.get("nome"));
            String descricao = parseString(m.get("descricao"));
            tipos.add(new TipoFeira(id, nome, descricao));
        }
        return tipos;
    }

    /**
     * Carrega a lista de feiras a partir do arquivo JSON informado.
     * Retorna lista vazia se o arquivo nao existir ou estiver vazio.
     *
     * @param arquivo caminho do arquivo JSON
     * @return lista de feiras carregadas
     */
    public static List<Feira> carregarFeiras(Path arquivo) {
        List<Feira> feiras = new ArrayList<>();
        String content = lerArquivo(arquivo);
        if (content == null || content.trim().isEmpty()) return feiras;
        for (Map<String, String> m : parseArray(content)) {
            Long id = parseLong(m.get("id"));
            String nome = parseString(m.get("nome"));
            String logradouro = parseString(m.get("logradouro"));
            String bairro = parseString(m.get("bairro"));
            boolean ativa = parseBoolean(m.get("ativa"));
            Long tipoId = parseLong(m.get("tipoFeiraId"));
            feiras.add(new Feira(id, nome, logradouro, bairro, ativa, tipoId == null ? 0L : tipoId));
        }
        return feiras;
    }

    /**
     * Persiste a lista de tipos de feira no arquivo JSON informado.
     * Cria o diretorio pai se necessario. Sobrescreve o arquivo existente.
     *
     * @param arquivo caminho do arquivo JSON
     * @param tipos   lista de tipos de feira a persistir
     */
    public static void salvarTiposFeira(Path arquivo, List<TipoFeira> tipos) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < tipos.size(); i++) {
            TipoFeira t = tipos.get(i);
            sb.append("  {\"id\": ").append(t.getId())
              .append(", \"nome\": \"").append(escape(t.getNome())).append("\"")
              .append(", \"descricao\": \"").append(escape(t.getDescricao())).append("\"}");
            if (i < tipos.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]\n");
        escreverArquivo(arquivo, sb.toString());
    }

    /**
     * Persiste a lista de feiras no arquivo JSON informado.
     * Cria o diretorio pai se necessario. Sobrescreve o arquivo existente.
     *
     * @param arquivo caminho do arquivo JSON
     * @param feiras  lista de feiras a persistir
     */
    public static void salvarFeiras(Path arquivo, List<Feira> feiras) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < feiras.size(); i++) {
            Feira f = feiras.get(i);
            sb.append("  {\"id\": ").append(f.getId())
              .append(", \"nome\": \"").append(escape(f.getNome())).append("\"")
              .append(", \"logradouro\": \"").append(escape(f.getLogradouro())).append("\"")
              .append(", \"bairro\": \"").append(escape(f.getBairro())).append("\"")
              .append(", \"ativa\": ").append(f.isAtiva())
              .append(", \"tipoFeiraId\": ").append(f.getTipoFeiraId())
              .append("}");
            if (i < feiras.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]\n");
        escreverArquivo(arquivo, sb.toString());
    }

    // -------------------------------------------------------------------------
    // Metodos privados de suporte
    // -------------------------------------------------------------------------

    private static List<Map<String, String>> parseArray(String json) {
        List<Map<String, String>> list = new ArrayList<>();
        Matcher objMatcher = OBJ_PATTERN.matcher(json);
        while (objMatcher.find()) {
            String body = objMatcher.group(1);
            Map<String, String> map = new LinkedHashMap<>();
            Matcher fieldMatcher = FIELD_PATTERN.matcher(body);
            while (fieldMatcher.find()) {
                map.put(fieldMatcher.group(1), fieldMatcher.group(2));
            }
            list.add(map);
        }
        return list;
    }

    private static String lerArquivo(Path arquivo) {
        try {
            if (!Files.exists(arquivo)) return null;
            return Files.readString(arquivo, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler JSON: " + arquivo, e);
        }
    }

    private static void escreverArquivo(Path arquivo, String content) {
        try {
            Path parent = arquivo.getParent();
            if (parent != null) Files.createDirectories(parent);
            Files.writeString(arquivo, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar JSON: " + arquivo, e);
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }

    private static String unquote(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.equals("null")) return null;
        if (raw.startsWith("\"") && raw.endsWith("\"") && raw.length() >= 2) {
            String x = raw.substring(1, raw.length() - 1);
            return x.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
        }
        return raw;
    }

    private static String parseString(String raw) {
        return unquote(raw);
    }

    private static Long parseLong(String raw) {
        if (raw == null || raw.equals("null")) return null;
        return Long.parseLong(raw.replace("\"", "").trim());
    }

    private static boolean parseBoolean(String raw) {
        if (raw == null) return false;
        return raw.trim().equals("true");
    }
}
