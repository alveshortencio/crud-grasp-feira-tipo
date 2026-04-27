# Atividade CRUD Grasp — GRUPO E

## Integrantes

- Rafael Lira (rafaelira1)
- Gabriel Alves (alveshortencio)

## Turma

T200-88

## Requisitos

- JDK 11 ou superior instalado
- Nenhuma dependencia externa necessaria (Java puro)

## Compilar 

No terminal, dentro desta pasta:

**Linux / macOS (bash):**
```bash
javac -d out src/feira/graspcrud/Main.java src/feira/graspcrud/controller/*.java src/feira/graspcrud/domain/*.java src/feira/graspcrud/dto/*.java src/feira/graspcrud/exception/*.java src/feira/graspcrud/repository/*.java src/feira/graspcrud/repository/json/*.java src/feira/graspcrud/service/*.java src/feira/graspcrud/util/*.java
```

**Windows (Prompt de Comando):**
```cmd
javac -d out src\feira\graspcrud\Main.java src\feira\graspcrud\controller\*.java src\feira\graspcrud\domain\*.java src\feira\graspcrud\dto\*.java src\feira\graspcrud\exception\*.java src\feira\graspcrud\repository\*.java src\feira\graspcrud\repository\json\*.java src\feira\graspcrud\service\*.java src\feira\graspcrud\util\*.java
```

**Windows (PowerShell):**
```powershell
javac -d out (Get-ChildItem -Recurse -Filter "*.java" src).FullName
```

## Executar

Após Compilar, execute o sistema com o comando:

```bash
java -cp out feira.graspcrud.Main
```

Os arquivos de dados serao criados automaticamente na pasta data/ ao realizar o primeiro cadastro:

- `data/tipos-feira.json`
- `data/feira.json`

## Padroes GRASP Aplicados

| Padrão GRASP | Classes | Justificativa |
|---|---|---|
| Information Expert | Feira, TipoFeira | Cada entidade valida suas proprias regras de negocio no metodo validar() |

| Creator | Main | Instancia e conecta todas as dependencias manualmente, sem framework de injecao |

| Controller | FeiraController | Unico ponto de entrada do menu; delega para servicos sem implementar regras de negocio |
| Low Coupling | FeiraService, TipoFeiraService | Dependem apenas de interfaces de repositorio, sem conhecer implementacoes concretas |
| High Cohesion | Todas as classes | Cada classe tem responsabilidade unica e bem definida |
| Pure Fabrication | JsonMini, FeiraRepositoryJson, TipoFeiraRepositoryJson | Classes fabricadas para isolar a persistencia JSON do dominio |
| Indirection | FeiraRepository, TipoFeiraRepository (interfaces) | Intermediam servicos e implementacoes; permitem troca de tecnologia sem alterar o dominio |
| Protected Variations | FeiraRepository, TipoFeiraRepository (interfaces) | Protegem o sistema de variacoes na persistencia; nova implementacao nao afeta servicos ou dominio |

 