package awesome.tony.fastfind;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAhoCorasick {

    private static final Logger LOG = LoggerFactory.getLogger(TestAhoCorasick.class);

    static List<SearchTerm> getSearchTerms(final List<String> terms) {
        final List<SearchTerm> searchTerms = new ArrayList<>(terms.size());
        terms.stream().forEach((term) -> {
            searchTerms.add(new SearchTerm(term.getBytes()));
        });
        return searchTerms;
    }

    @Test
    public void testOneMatch() {
        final String text = "this is a pretty stupid text string";
        final List<SearchTerm> phrases = getSearchTerms(Arrays.asList("stupid"));
        final AhoCorasick ac = new AhoCorasick(phrases);
        try {
            final Map<SearchTerm, List<Long>> results = ac.evaluate(new ByteArrayInputStream(text.getBytes()), true);
            assertEquals(1, results.size());
            final Map.Entry<SearchTerm, List<Long>> result = results.entrySet().iterator().next();
            assertEquals(new SearchTerm("stupid".getBytes()), result.getKey());
            assertEquals(1, result.getValue().size());
            assertEquals(Long.valueOf(23L), result.getValue().get(0));
        } catch (final IOException ex) {
            fail(ex.toString());
        }
    }

    @Test
    public void testMultimatchFindAll() {
        final String text = "this is a pretty stupid text string - this is a pretty stupid text string - this is a pretty stupid text string - thanks...ttt";
        final List<SearchTerm> phrases = getSearchTerms(Arrays.asList("stupid", "-", "t", "th", "g - thi", "tt", "this is a long string that won't match"));
        final AhoCorasick ac = new AhoCorasick(phrases);
        try {
            final Map<SearchTerm, List<Long>> results = ac.evaluate(new ByteArrayInputStream(text.getBytes()), true);
            assertEquals(6, results.size());
            for (final Map.Entry<SearchTerm, List<Long>> entry : results.entrySet()) {
                final SearchTerm term = entry.getKey();
                switch (term.toString()) {
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

    @Test
    public void testMultimatchFindOne() throws IOException {
        final String text = "this is a pretty stupid text string - this is a pretty stupid text string - this is a pretty stupid text string - thanks...ttt";
        final List<SearchTerm> phrases = getSearchTerms(Arrays.asList("stupid", "-", "t", "th", "g - thi", "tt", "this is a long string that won't match"));
        final AhoCorasick ac = new AhoCorasick(phrases);
        final Map<SearchTerm, List<Long>> results = ac.evaluate(new ByteArrayInputStream(text.getBytes()), false);
        assertEquals(1, results.size());
    }
}
