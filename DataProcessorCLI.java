import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataProcessorCLI {

    private static long startTime;
    private static int filesProcessed = 0;
    private static List<String> allResults = new ArrayList<>();

    private static String[] normalizarDados(String dominio, String usuario, String senha) {
        Pattern urlPattern = Pattern.compile("^(https?://)?(www\\.)?[\\w.-]+(\\.[a-z]{2,})+/?");
        Matcher matcher;

        matcher = urlPattern.matcher(dominio);
        if (!matcher.find() && (urlPattern.matcher(usuario).find() || urlPattern.matcher(senha).find())) {
            if (urlPattern.matcher(usuario).find()) {
                String temp = dominio;
                dominio = usuario;
                usuario = senha;
                senha = temp;
            } else if (urlPattern.matcher(senha).find()) {
                String temp = dominio;
                dominio = senha;
                senha = usuario;
                usuario = temp;
            }
        }
        return new String[]{dominio, usuario, senha};
    }

    private static String criarJson(String dominio, String usuario, String senha, String linha) {
        return String.format("{\"url\": \"%s\", \"username\": \"%s\", \"password\": \"%s\", \"line\": \"%s\"}", dominio, usuario, senha, linha);
    }

    private static void processarArquivo(File file, String palavraChave) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.toLowerCase().contains(palavraChave.toLowerCase())) {
                    String[] partes = linha.split("[:;|, ]", 3);
                    if (partes.length >= 3) {
                        String[] dados = normalizarDados(partes[0], partes[1], partes[2]);
                        allResults.add(criarJson(dados[0], dados[1], dados[2], linha));
                    }
                }
            }
        }
        filesProcessed++;
    }

    private static void updateProgress(int filesProcessed, int totalFiles) {
        long elapsed = System.currentTimeMillis() - startTime;
        int minutes = (int) (elapsed / 60000);
        int seconds = (int) ((elapsed / 1000) % 60);
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        int progressPercentage = (int) ((double) filesProcessed / totalFiles * 100);
        String progressBar = new String(new char[progressPercentage / 5]).replace("\0", "█")
                             + new String(new char[20 - (progressPercentage / 5)]).replace("\0", " ");
        System.out.printf("\rProgresso - %d resultados encontrados: %d%%|%s| [%s /time, %d/file-searched ]", allResults.size(), progressPercentage, progressBar, timeFormatted, filesProcessed);
    }

    private static void salvarResultados() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nDeseja salvar os resultados em um arquivo? (S/N): ");
        String resposta = scanner.nextLine().trim().toLowerCase();
        if ("s".equals(resposta)) {
            System.out.print("Digite o nome do arquivo para salvar (sem extensão): ");
            String nomeArquivo = scanner.nextLine().trim();
            File arquivo = new File("F:/programacao/python/kadu/cloud/" + nomeArquivo + ".txt");
            try (PrintWriter out = new PrintWriter(arquivo)) {
                allResults.forEach(out::println);
                System.out.println("Os resultados foram salvos em " + arquivo.getAbsolutePath());
            } catch (FileNotFoundException e) {
                System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java DataProcessorCLI <file|dir> <path> <keyword>");
            return;
        }

        String type = args[0];
        File path = new File(args[1]);
        String keyword = args[2];

        startTime = System.currentTimeMillis();

        try {
            if (type.equalsIgnoreCase("file") && path.isFile()) {
                processarArquivo(path, keyword);
                updateProgress(filesProcessed, 1);
            } else if (type.equalsIgnoreCase("dir") && path.isDirectory()) {
                File[] files = path.listFiles(f -> f.getName().endsWith(".txt"));
                if (files != null) {
                    int totalFiles = files.length;
                    for (File file : files) {
                        processarArquivo(file, keyword);
                        updateProgress(filesProcessed, totalFiles);
                    }
                }
            } else {
                System.out.println("\nPath inválido ou não corresponde ao tipo especificado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        salvarResultados();
    }
}
