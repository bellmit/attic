# Writing Documentation

This project has two sets of vaguely formal documentation:

1. The README collection, and
2. Javadoc comments.

They have subtly different audiences, and should be written slightly differently.

The README collection covers high-level materials and whole-project concepts and knowledge. The reader can be assumed to be doing nothing _but_ reading the documentation; since it's stored in separate files, it must be read separately from an IDE or build tool message. Using free text (well, Markdown) makes it suitable for longer-form documentation that elaborates extensively on concepts and ideas. The README collection assumes the reader is technical, but not necessarily that the reader has deep knowledge of the codebase.

The Javadocs, by contrast, cover extremely fine-grained information about the use and meaning of single Java-language symbols at a time. The audience is a programmer in the middle of _some other task_, trying to recall how to use a class or what guarantees a method provides. It's likely to be presented in a tooltip or IDE callout, where space is at a premium, and the reader is likely busy with something other than reading the attached code. Conversely, the Javadocs can assume that the reader is deeply knowledgeable about nearby parts of the code: the reader is, after all, probably already reading or writing code.

Effective writing must consider its audience. Keep this in mind when writing both kinds of documentation, and when setting up new documentation sets.
