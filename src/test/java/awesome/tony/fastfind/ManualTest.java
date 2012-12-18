package awesome.tony.fastfind;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        //build up our dictionary of stuff to search for
        final List<String> dictionaryStrings = Arrays.asList("蕘翁", "思", "a few minutes to settle, taking care", "смерть свою");
        final List<SearchTerm> dictionary = new ArrayList<>(dictionaryStrings.size());
        for (final String str : dictionaryStrings) {
            dictionary.add(new SearchTerm(str.getBytes(UTF8)));
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
                    } catch (final Throwable t) {
                        //ignore
                    }
                }
            }
        }

        final AhoCorasick ac = new AhoCorasick(dictionary);
        for (int i = 0; i < 1000000; i++) {
            for (Map.Entry<String, byte[]> entry : resourceMap.entrySet()) {
                final Map<SearchTerm, List<Long>> result = ac.evaluate(new ByteArrayInputStream(entry.getValue()), true);
                if (i % 10000 == 0) {
                    LOG.info("found {} matching terms in '{}'. {}", result.size(), entry.getKey(), result.toString());
                }
            }
        }

    }
}
