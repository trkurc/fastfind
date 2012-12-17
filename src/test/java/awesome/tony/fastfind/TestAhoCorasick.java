package awesome.tony.fastfind;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAhoCorasick {

    private static final Logger LOG = LoggerFactory.getLogger(TestAhoCorasick.class);

    @Test
    public void testOneMatch() {
        final String text = "this is a pretty stupid text string";
        final List<byte[]> phrases = new ArrayList<>(5);
        phrases.add("stupid".getBytes());
        final AhoCorasick ac = new AhoCorasick(phrases);
        try {
            ac.evaluate(new ByteArrayInputStream(text.getBytes()));
            final Map<SearchPhrase, List<Long>> results = ac.getResults();
            assertEquals(1, results.size());
            final Map.Entry<SearchPhrase, List<Long>> result = results.entrySet().iterator().next();
            assertEquals("stupid", new String(result.getKey().getPhrase()));
            assertEquals(1, result.getValue().size());
            assertEquals(Long.valueOf(23L), result.getValue().get(0));
        } catch (final IOException ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testMultimatchSamePhrase() {
        final String text = "this is a pretty stupid text string - this is a pretty stupid text string - this is a pretty stupid text string - thanks...ttt";
        final List<byte[]> phrases = new ArrayList<>(5);
        phrases.add("stupid".getBytes());
        phrases.add("-".getBytes());
        phrases.add("t".getBytes());
        phrases.add("th".getBytes());
        phrases.add("g - thi".getBytes());
        phrases.add("tt".getBytes());
        phrases.add("this string is not in here but is kinda long".getBytes());
        final AhoCorasick ac = new AhoCorasick(phrases);
        try {
            ac.evaluate(new ByteArrayInputStream(text.getBytes()));
            final Map<SearchPhrase, List<Long>> results = ac.getResults();
            assertEquals(6, results.size());
            for (final Map.Entry<SearchPhrase, List<Long>> entry : results.entrySet()) {
                final String phraseVal = new String(entry.getKey().getPhrase());
                switch (phraseVal) {
                    case "stupid":
                        assertEquals(3, entry.getValue().size());
                        LOG.info("stupid matched at indexes {}", entry.getValue().toString());
                        break;
                    case "-":
                        assertEquals(3, entry.getValue().size());
                        LOG.info("- matched at indexes {}", entry.getValue().toString());
                        break;
                    case "t":
                        LOG.info("t matched at indexes {}", entry.getValue().toString());
                        assertEquals(25, entry.getValue().size());
                        break;
                    case "tt":
                        LOG.info("tt matched at indexes {}", entry.getValue().toString());
                        assertEquals(5, entry.getValue().size());
                        break;
                    case "th":
                        LOG.info("th matched at indexes {}", entry.getValue().toString());
                        assertEquals(4, entry.getValue().size());
                        break;
                    case "g - thi":
                        LOG.info("g - thi matched at indexes {}", entry.getValue().toString());
                        assertEquals(2, entry.getValue().size());
                        break;
                    default:
                        fail();
                }
            }
        } catch (final IOException ex) {
            fail(ex.toString());
        }
    }
}
