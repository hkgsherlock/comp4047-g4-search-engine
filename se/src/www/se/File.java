package se;

import org.jsoup.helper.StringUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Demonstrate basic file operations.
 */
public class File {
    private static final boolean DEFAULT_WRITABLE = false;

    private final boolean preventWrite;
    java.io.File file;

    public File(String fileName) throws IOException {
        this(Paths.get(fileName));
    }

    public File(String fileName, boolean write) throws IOException {
        this(Paths.get(fileName), write);
    }

    public File(java.io.File file) throws IOException {
        this(file.toPath());
    }

    public File(java.io.File file, boolean write) throws IOException {
        this(file.toPath(), write);
    }

    public File(Path filePath) throws IOException {
        this(filePath, DEFAULT_WRITABLE);
    }

    public File(Path filePath, boolean write) throws IOException {
        String startDir = System.getProperty("user.dir");
        if (!filePath.toString().equals(filePath.toAbsolutePath().toString()))
            filePath = Paths.get(startDir, filePath.toString());

        if (!Files.exists(filePath)) {
            {
                Stack<Path> parents = new Stack<>();

                for (Path parent = filePath.getParent(); parent != null; parent = parent.getParent()) {
                    parents.push(parent);
                }

                while (!parents.isEmpty()) {
                    Path p = parents.pop();
                    if (!Files.exists(p, LinkOption.NOFOLLOW_LINKS))
                        Files.createDirectory(p);
                }
            }

            Files.createFile(filePath);
        }
        this.file = filePath.toFile();
        this.preventWrite = !write;
    }


    public File(File file) {
        this.file = file.file;
        this.preventWrite = file.preventWrite;
    }

    /**
     * Write lines to a file (create it if not exists).
     */
    public boolean writeTexts(String[] lines) throws IOException {
        return this.writeTexts(Arrays.asList(lines));
    }

    public boolean writeTexts(Iterable<? extends CharSequence> lines) throws IOException {
        if (this.preventWrite)
            return false;
        Files.write(this.file.toPath(), lines);
        return true;
    }

    public boolean writeText(String text) throws IOException {
        if (this.preventWrite)
            return false;
        FileWriter fw = new FileWriter(file);
        fw.write(text);
        fw.close();
        System.out.println("File created at " + file.getAbsolutePath());
        return true;
    }

    public boolean writeBytes(byte[] bytes) throws IOException {
        if (this.preventWrite)
            return false;
        Files.write(this.file.toPath(), bytes);
        return true;
    }

    /**
     * Read line by line from a file and print to console.
     */
    public String readTexts() throws IOException {
        List<String> strings = this.readLinesListString();
        return StringUtil.join(strings, "\n");
    }

    public String[] readLines() throws IOException {
        List<String> strings = this.readLinesListString();
        return strings.toArray(new String[strings.size()]);
    }

    private List<String> readLinesListString() throws IOException {
        return Files.readAllLines(this.file.toPath());
    }

    public byte[] readBytes(byte[] bytes) throws IOException {
        return Files.readAllBytes(this.file.toPath());
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(this.file);
    }

    public OutputStream getOutputStream() throws FileNotFoundException {
        return new FileOutputStream(this.file);
    }

    /**
     * Test entry point for this particular single module
     */
    public static void main(String[] args) throws IOException {
        String text = "Example File\n" +
                "\n" +
                "This file is created to illustrate basic file operations.\n";

        File fileDemo = new File("example.txt");
        System.out.println("### Write lines to a file.");
        fileDemo.writeText(text);
        System.out.println("### Read lines from a file.");
        fileDemo.readTexts();
    }
}
