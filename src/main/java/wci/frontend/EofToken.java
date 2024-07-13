package wci.frontend;

/**
 * <h1>EofToken</h1>
 *
 * <p>The generic end-of-file token.</p>
 */
public class EofToken extends Token
{
    /**
     * Constructor.
     * @param source the source from where to fetch subsequent characters.
     * @throws Exception if an error occurred
     */
    public EofToken (Source source)
        throws Exception
    {
        super(source);
    }

    protected void extract (Source source)
        throws Exception
    {
    }
}
