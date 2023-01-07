import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        List<String> filePath = new ArrayList<>();
        Stream.of(
                new GameProgress(1, 1, 1, 1),
                new GameProgress(1, 2, 2, 2),
                new GameProgress(1, 3, 3, 3)
        ).forEach(gameProgress -> {
            String path = "C:/Users/User/Desktop/Games/savegames/game" + gameProgress.hashCode() + ".dat";
            filePath.add(path);
            saveGame(path, gameProgress);
        });

        zipFiles("C:/Users/User/Desktop/Games/savegames/zip.zip", filePath);

        File dir = new File("C:/Users/User/Desktop/Games/savegames");

        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(file -> !file.getName().equalsIgnoreCase("zip.zip"))
                .forEach(File::delete);
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutputStream.writeObject(gameProgress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void zipFiles(String path, List<String> files) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path))) {
            files.forEach(f -> {
                try (FileInputStream fileInputStream = new FileInputStream(f)) {
                    zipOutputStream.putNextEntry(new ZipEntry(String.valueOf(f.hashCode())));
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    zipOutputStream.write(buffer);
                    zipOutputStream.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
