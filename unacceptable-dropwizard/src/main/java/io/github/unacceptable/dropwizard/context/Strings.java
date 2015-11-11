package io.github.unacceptable.dropwizard.context;

class Strings {
    public static String stripLeadingSlashes(String s) {
        int i = findFirstNonSlash(s);
        return s.substring(i);
    }

    public static String stripTrailingSlashes(String s) {
        int i = findLastNonSlash(s);
        return s.substring(0, i);
    }

    public static int findLastNonSlash(String s) {
        for (int i = s.length(); i > 0; --i)
            if (s.charAt(i - 1) != '/')
                return i;
        return 0;
    }

    public static int findFirstNonSlash(String s) {
        for (int i = 0; i < s.length(); ++i)
            if (s.charAt(i) != '/')
                return i;
        return s.length();
    }

}
