# search-keyword-cloud

## Objetivo

O projeto Pugn0 foi desenvolvido com o objetivo de fornecer uma ferramenta robusta para processamento e análise de dados extraídos de arquivos de texto. Ele é capaz de identificar informações específicas dentro de grandes volumes de dados, facilitando a filtragem e extração de dados em formato JSON para fins específicos, como segurança da informação e análise de dados.

## Requisitos

- Java JDK 11 ou superior
- Acesso ao sistema de arquivos para leitura de arquivos .txt

## Como Usar

### Configuração

Primeiro, certifique-se de que o Java está instalado em seu sistema. Você pode verificar isso executando:

```bash
java -version
```
### Compilação
Para compilar o programa, navegue até o diretório do projeto e execute o seguinte comando:
```bash
javac DataProcessorCLI.java
```
Isso irá gerar o arquivo DataProcessorCLI.class, que você pode executar.
```bash
java DataProcessorCLI <file|dir> <path> <keyword>
```
* ```<file|dir>```: Especifique file para processar um único arquivo ou dir para processar todos os arquivos em um diretório.
* ```<path>```: O caminho para o arquivo ou diretório que você deseja processar.
* ```<keyword>```: A palavra-chave para filtrar os dados.
Exemplo de uso:
```bash
java DataProcessorCLI dir "F:/programacao/python/kadu/cloud/db/filtro/http" "unitybankng.com"
```
### Salvando os Resultados
Após a execução, o programa perguntará se você deseja salvar os resultados em um arquivo. Se desejar salvar, responda S e forneça um nome para o arquivo. Os resultados serão salvos no formato JSON no local especificado.
## Contribuições
Contribuições são bem-vindas! Por favor, leia o CONTRIBUTING.md para saber como contribuir para o projeto.


