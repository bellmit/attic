package ca.grimoire.bom.sample;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import ca.grimoire.bom.BomLoader;

public class Sample {
    public static void main(String[] args) throws IOException {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out,
                "UTF-8"));

        out.println("=== Texts ===");
        dumpResources(out, Sample.class.getClassLoader(), "META-INF/bom/texts");

        out.println("=== Non-Texts ===");
        dumpResources(out, Sample.class.getClassLoader(), "META-INF/bom/non-texts");

        out.println("=== Texts ===");
        dumpResources(out, Sample.class.getClassLoader(), "META-INF/bom/everything");
        
        out.close();
}

    private static void dumpResources(PrintWriter out,
            ClassLoader classLoader,
            String bom) throws IOException, UnsupportedEncodingException {
        for (URL textResource : BomLoader.findResources(classLoader, bom)) {
            out.println("--- " + textResource + " ---");
            Reader r = new InputStreamReader(textResource.openStream(), "UTF-8");
            try {
                char[] buffer = new char[8192];
                for (int read = r.read(buffer); read >= 0; read = r
                        .read(buffer)) {
                    out.write(buffer, 0, read);
                }
            } finally {
                r.close();
            }
        }
    }
}
