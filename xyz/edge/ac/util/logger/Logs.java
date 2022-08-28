package xyz.edge.ac.util.logger;

import xyz.edge.ac.util.SystemLogsUtil;
import java.util.Arrays;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import xyz.edge.ac.Edge;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public final class Logs
{
    public static boolean logToFile(final TextFile file, final String text) {
        try {
            file.addLine(text);
            file.write();
            return true;
        }
        catch (final Exception ex) {
            return false;
        }
    }
    
    public static class TextFile
    {
        private File file;
        private String name;
        private final List<String> lines;
        
        public TextFile(final String name) {
            this.lines = new ArrayList<String>();
            Edge.getInstance().getPacketExecutor().execute(() -> {
                (this.file = new File("plugins/Edge/logs/")).mkdirs();
                new File("plugins/Edge/logs/", name + ".txt");
                final File file;
                this.file = file;
                try {
                    if (!this.file.exists()) {
                        this.file.createNewFile();
                    }
                }
                catch (final Exception ex) {}
                this.name = name;
                this.readTextFile();
            });
        }
        
        public void clear() {
            this.lines.clear();
        }
        
        public void addLine(final String line) {
            this.lines.add(line);
        }
        
        public void write() {
            Edge.getInstance().getPacketExecutor().execute(() -> {
                try {
                    final Writer fw = new OutputStreamWriter(Files.newOutputStream(this.file.toPath(), new OpenOption[0]), StandardCharsets.UTF_8);
                    final BufferedWriter bw = new BufferedWriter(fw);
                    this.lines.iterator();
                    final Iterator iterator;
                    while (iterator.hasNext()) {
                        final String line = iterator.next();
                        bw.write(line);
                        bw.newLine();
                    }
                    bw.close();
                    fw.close();
                }
                catch (final Exception ex) {}
            });
        }
        
        public void readTextFile() {
            this.lines.clear();
            Edge.getInstance().getPacketExecutor().execute(() -> {
                try {
                    final FileReader fr = new FileReader(this.file);
                    final BufferedReader br = new BufferedReader(fr);
                    while (true) {
                        final String line = br.readLine();
                        final Object o;
                        if (o != null) {
                            this.lines.add(line);
                        }
                        else {
                            break;
                        }
                    }
                    br.close();
                    fr.close();
                }
                catch (final Exception e) {
                    SystemLogsUtil.createNewLog(Arrays.toString(e.getStackTrace()), "Logs (ReadTextFile)", e.getMessage());
                }
            });
        }
        
        public String getText() {
            final StringBuilder text = new StringBuilder();
            for (int i = 0; i < this.lines.size(); ++i) {
                final String line = this.lines.get(i);
                text.append(line).append((this.lines.size() - 1 == i) ? "" : "\n");
            }
            return text.toString();
        }
        
        public List<String> getLines() {
            return this.lines;
        }
    }
}
