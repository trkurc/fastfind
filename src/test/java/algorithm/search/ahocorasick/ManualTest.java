package algorithm.search.ahocorasick;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author
 */
public class ManualTest {

    private static final Logger LOG = LoggerFactory.getLogger(ManualTest.class);
    private static final Charset UTF8 = Charset.forName("utf-8");

    public static void main(final String args[]) throws IOException {
        final List<SearchTerm> dictionary = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader((Thread.currentThread().getContextClassLoader().getResourceAsStream("longmatchlist.txt"))));
            String lineRead;
            while ((lineRead = reader.readLine()) != null) {
                if (lineRead.length() > 4) {
                    dictionary.add(new SearchTerm(lineRead.getBytes(UTF8)));
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException t) {
                    //ignore
                }
            }
        }

        //build up our content to search against
        final List<String> resources = Arrays.asList("GUTINDEX.ALL", "chinese.txt", "english.txt", "russian.txt", "us.jpg");
        final Map<String, byte[]> resourceMap = new HashMap<>(resources.size());
        for (final String resource : resources) {
            InputStream stream = null;
            try {
                stream = new BufferedInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource));
                final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int byteRead;
                while ((byteRead = stream.read()) >= 0) {
                    bos.write(byteRead);
                }
                resourceMap.put(resource, bos.toByteArray());
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (final IOException t) {
                        //ignore
                    }
                }
            }
        }
        final long pre = System.currentTimeMillis();
        final AhoCorasick ac = new AhoCorasick(dictionary);
        for (int i = 0; i < 1; i++) {
            for (Map.Entry<String, byte[]> entry : resourceMap.entrySet()) {
                final Map<SearchTerm, List<Long>> result = ac.evaluate(new ByteArrayInputStream(entry.getValue()), true);
                if (i % 10000 == 0) {
                    LOG.info("found {} matching terms in '{}'. {}", result.size(), entry.getKey(), result.toString());
                }
            }
        }
        final long post = System.currentTimeMillis();
        LOG.info("executed in {}ms", (post - pre));

    }
}
