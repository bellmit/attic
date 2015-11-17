package io.github.unacceptable.alias;

public class EmailAddressGenerator implements Generator<String> {
    private static final String DEFAULT_DOMAIN = "example.com";
    private static final UsernameGenerator DEFAULT_USERNAME_GENERATOR = new UsernameGenerator();
    private static final EmailAddressGenerator GENERATOR = new EmailAddressGenerator();
    private final Generator<String> generator;

    /**
     * Generate an email address using a suitable default configuration.
     *
     * @param alias
     *         an alias for the generated address.
     * @return an email address generated using suitable defaults.
     * @see #generate(String)
     */
    public static String defaultGenerate(String alias) {
        return GENERATOR.generate(alias);
    }

    private final UsernameGenerator usernameGenerator;

    public EmailAddressGenerator() {
        this(DEFAULT_USERNAME_GENERATOR);
    }

    public EmailAddressGenerator(UsernameGenerator usernameGenerator) {
        this.usernameGenerator = usernameGenerator;
        generator = new EmptyWrappingGenerator(new AbsentWrappingGenerator(new LiteralWrappingGenerator(this::generateEmailAddress)));
    }

    @Override
    public String generate(final String alias) {
        return generator.generate(alias);
    }

    /**
     * Generates random email addresses, given an address template as an input. The template can either be a bare local
     * part ("bob"), in which case a random suffix will be attached and the domain will default to {@value
     * #DEFAULT_DOMAIN}, or a complete email address, in which case only the local part will be modified to make the
     * address unique. (The domain will be reused as-is.)
     *
     * @param alias
     *         an email address alias such as <code>"alexandra"</code> or <code>"alexandra@example.com"</code>.
     * @return a randomly-modified email address.
     */
    public String generateEmailAddress(String alias) {
        String[] parts = alias.split("@", 2);
        String localPartAlias = parts[0];
        String domain = parts.length > 1 ? parts[1] : DEFAULT_DOMAIN;

        String localPart = usernameGenerator.generate(localPartAlias);
        return localPart + "@" + domain;
    }
}
