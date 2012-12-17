package awesome.tony.fastfind;

import java.util.Arrays;

/**
 * Class to hold/wrap a byte array representing a given search phrase. This
 * class does not protect the given array on creation or when passing to a caller.
 * Thus if the phrase is getting modified then unexpected behavior will occur.
 * @author
 */
public class SearchPhrase {

    private final byte[] phrase;
    private final int hashCode;

    public SearchPhrase(final byte[] phrase) {
        this.phrase = phrase;
        this.hashCode = Arrays.hashCode(this.phrase);
    }
    
    public byte[] getPhrase(){
        return phrase;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SearchPhrase other = (SearchPhrase) obj;
        if (this.hashCode != other.hashCode) {
            return false;
        }
        return Arrays.equals(this.phrase, other.phrase);
    }
}
