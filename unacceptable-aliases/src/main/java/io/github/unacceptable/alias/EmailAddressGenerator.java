package io.github.unacceptable.alias;

public class EmailAddressGenerator {
    private static final String DEFAULT_DOMAIN = "example.com";
    private static final UsernameGenerator USERNAME_GENERATOR = new UsernameGenerator();

    private EmailAddressGenerator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Generates random email addresses, given an address template as an input. The template can either be a bare local
     * part ("bob"), in which case a random suffix will be attached and the domain will default to {@value
     * #DEFAULT_DOMAIN}, or a complete email address, in which case only the local part will be modified to make the
     * address unique. (The domain will be reused as-is.)
     * <p>
     * This method is suitable for use in an {@link AliasStore}.
     *
     * @param alias
     *         an email address alias such as <code>"alexandra"</code> or <code>"alexandra@example.com"</code>.
     * @return a randomly-modified email address.
     */
    public static String generate(String alias) {
        String[] parts = alias.split("@", 2);
        String localPartAlias = parts[0];
        String domain = parts.length > 1 ? parts[1] : DEFAULT_DOMAIN;

        String localPart = USERNAME_GENERATOR.generate(localPartAlias);
        return localPart + "@" + domain;
    }
}
