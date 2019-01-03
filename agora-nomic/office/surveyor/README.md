# Land Records Archive

This archive operates in three parts.

## Documents

The `documents` directory contains unaltered copies of original
documents. For the most part, these are complete emails, including
headers, as delivered. They are filed using a document-specific unique
identifier:

* Documents originating as email are named for their Message-ID, with
  the suffix `.eml`.

To file a new document:

1. Drop the original, under any name with the correct suffix, in the
    `documents` directory.

2. Run `bin/file-document <path to document>` using the file-document
    script in this repository. This will canonicalize the document's name,
    or abort if a document with the same canonical name already exists.

## Estates

The `estates` directory contains one directory per Estate, as defined
by the rules, documenting the history of that Estate. Each event is
recorded as a group of files, based on the timestamp of the event:

* `YYYY-MM-DD-hh:mm:ss.eml` is a symbolic link to the original document
  in the `documents` directory.

* `YYYY-MM-DD-hh:mm:ss.yaml` is a YAML file containing two keys:

    * The `event` key, describing the event. This presently contains a
      single key, `summary`, containing a one-line prose description of
      the event.

    * The `diff` key, describing the changes to the state of the
      Estate's key properties. This is a YAML encoding of an RFC 7386
      JSON Merge-Patch object, which can be applied in sequence to
      build a YAML representation of the Estate.

All timestamps are in UTC.

The YAML representation of an estate describes its properties at a
specific point in time. It can be computed by starting with an empty
YAML document and applying the diff section, as a JSON Merge-Patch
document, of each event, in chronological order. The resulting object
has, at present, two important keys:

* `name`, giving the short name of the Estate, and

* `owner`, naming the entity which owns the Estate.

To create a new event from a document:

1. File the document.

2. Run `bin/link-event <path to document> <path to estate>`. If the
    timestamp cannot be detected, or needs to be overridden, use the
    `--timestamp` option with an ISO 8601 timestamp.

3. Edit the resulting YAML file.

## Reports

The `reports` directory contains subdirectories for each report
required by the rules for the office of Surveyor. The contents of these
directories are date-stamped report editions, as plain text, plus an
optional draft of the next report that will be sent.

For the office of Surveyor, _all_ reports are generated from the data
in the `estates` and `documents` directory programmatically. Tweaks may
be applied before transmission but should be kept to a bare minimum.
