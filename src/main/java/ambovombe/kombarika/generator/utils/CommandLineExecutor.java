package ambovombe.kombarika.generator.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CommandLineExecutor {
    public static void addIonicPage(String pageName) {
        try {
            String[] commands = {
                    "cmd",
                    "/c",
                    "ionic",
                    "g",
                    "page",
                    pageName
            };
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.directory(new File("I:\\CodeGenerated\\AngularIonic\\TestApp"));
            builder.redirectErrorStream(true);
            Process process = builder.start();

            // Lire la sortie de la console
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Attendre que le processus se termine
            process.waitFor();
            System.out.println("Page " + pageName + " créée avec succès !");
            CommandLineExecutor.writeTypescript(pageName);
            CommandLineExecutor.copyView(pageName);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void writeTypescript(String pageName) {
        try (BufferedReader sourceReader = new BufferedReader(new FileReader("I:\\CodeGenerated\\React\\typescriptsrc\\carburant.page.ts"));
             BufferedReader destinationReader = new BufferedReader(new FileReader("I:\\CodeGenerated\\AngularIonic\\TestApp\\src\\app\\"+pageName+"\\"+pageName+".page.ts"));
             BufferedWriter writer = new BufferedWriter(new FileWriter("I:\\CodeGenerated\\AngularIonic\\TestApp\\src\\app\\"+pageName+"\\"+pageName+".page.ts"))) {

            String sourceLine;
            String destinationLine;

            while ((sourceLine = sourceReader.readLine()) != null) {
                destinationLine = destinationReader.readLine();
                if (destinationLine == null || !destinationLine.equals("selector: 'app-"+pageName+"',")) {
                    writer.write(CommandLineExecutor.renommerString(sourceLine, pageName));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyView(String pageName) {
        Path source = Paths.get("F:\\Scaffolding\\SCAFFOLDING-main\\kombarika\\view\\"+pageName+".page.html");
        Path destination = Paths.get("I:\\CodeGenerated\\AngularIonic\\TestApp\\src\\app\\"+pageName);

        try {
            Files.copy(source, destination.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Le fichier a été copié avec succès !");
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de la copie du fichier : " + e.getMessage());
        }
    }

    public static String renommerString(String chaine, String pageName) {
        if(chaine.contains("carburant"))
            return chaine.replace("carburant", pageName);
        else if(chaine.contains("Carburant"))
            return chaine.replace("Carburant", ObjectUtility.capitalize(ObjectUtility.formatToCamelCase(pageName)));
        return chaine;
    }

    public static void copySrc(String fileBatPath) {
        ProcessBuilder pb = new ProcessBuilder(fileBatPath);
        try {
            Process p = pb.start();

            StringBuilder sortie = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                sortie.append(ligne).append("\\n");
            }
            int codeRetour = p.waitFor();

            if (codeRetour == 0) {
                System.out.println(sortie);
                System.exit(0);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
