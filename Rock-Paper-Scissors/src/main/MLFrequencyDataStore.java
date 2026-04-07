import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MLFrequencyDataStore {
    private final String dataFile;

    public MLFrequencyDataStore(String dataFile) {
        this.dataFile = dataFile;
    }

    public Map<String, Integer> load() {
        Map<String, Integer> sequenceCounts = new HashMap<>();
        File file = resolveDataFile();

        if (!file.exists()) {
            return sequenceCounts;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");

                if (parts.length == 2) {
                    String sequence = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    sequenceCounts.put(sequence, count);
                }
            }
        } 
        
        catch (IOException | NumberFormatException e) {
            System.out.println("Could not load ML data.");
        }

        return sequenceCounts;
    }


    public void save(Map<String, Integer> sequenceCounts) {
        File file = resolveDataFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Integer> entry : sequenceCounts.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } 
        catch (IOException e) {
            System.out.println("Could not save ML data.");
        }
    }

    private File resolveDataFile() {
        Path projectDataFile = Paths.get("src", "main", dataFile);
        Path localDataFile   = Paths.get(dataFile);
        if (projectDataFile.toFile().exists()) {
            return projectDataFile.toFile();
        }
        if (localDataFile.toFile().exists()) {
            return localDataFile.toFile();
        }
        if (projectDataFile.getParent().toFile().exists()) {
            return projectDataFile.toFile();
        }
        return localDataFile.toFile();
    }
}