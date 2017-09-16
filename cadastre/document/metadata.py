# To support automatically detecting metadata in incoming messages, we provide a
# system of metadata extractors. The protocol for metadata extraction is simple:
# given a single argument (the raw original, as a byte stream) as input, the
# extractor returns a `Metadata` object whose fields are either `None` if the
# value could not be detected, or set to the value extracted from the original.
#
# Detection failure is not an error: passing the wrong type of document, or an
# unparseable document, or a document with problematic metadata, to an extractor
# returns an empty `Metadata` result.
#
# The interesting metadata fields are `message_id`, which uniquely identifies
# the original, and `date`, which dates its publication.

import collections as c

class Metadata(c.namedtuple('Metadata', ['message_id', 'date'])):
    # Metadata objects can be merged. Merging a Metadata object creates a new
    # Metadata object whose fields are the value of the corresponding field in
    # self, if set, or the value of the corresponding field in the merged
    # Metadata, otherwise.
    #
    # We'll use this later on to override detected metadata from an original
    # document with explicitly-given metadata from a request.
    def merge(self, onto):
        return type(self)(
            message_id=coalesce(self.message_id, onto.message_id),
            date=coalesce(self.date, onto.date),
        )

# Return the first non-None value of its arguments, or `None` if all arguments
# are `None`. This is inspired by the SQL function of the same name.
def coalesce(*values):
    for value in values:
        if value is not None:
            return value

# This extractor attempts to find the Message-ID and the Date of an RFC-822
# original by inspecting its top-level headers.

import email

def extract_rfc822_metadata(original):
    message = email.message_from_bytes(original)
    return Metadata(
        message_id=message['Message-ID'],
        date=message['Date'],
    )

# This extractor always finds nothing. It's used as a fallback extractor when no
# extractor is appropriate for a given message based on other information known
# about the message.

def extract_null_metadata(original):
    return Metadata(
        message_id=None,
        date=None,
    )

# Extractors are associated with specific content types. This lookup function
# determines the appropriate extractor based on MIME media types. This always
# returns an extractor: for unrecognized MIME media types, this returns the null
# extractor.

metadata_extractors = {
    'message/rfc822': extract_rfc822_metadata
}

def extractor_for_mime_type(mime_type):
    return metadata_extractors.get(mime_type, extract_null_metadata)

# ## METADATA COMPONENTS
#
# An API Star request can also provide metadata from requests in a variety of
# ways.

# A request can provide metadata drawn from request headers.

from apistar import http

class HeaderMetadata(Metadata):
    pass

def header_metadata(message_id: http.Header = None, date: http.Header = None):
    return Metadata(
        message_id=message_id,
        date=date,
    )

# A request can provide metadata detected from the request body.

from apistar import http

class DetectedMetadata(Metadata):
    pass

def detect_metadata(body: http.Body, content_type: http.Header):
    metadata_extractor = extractor_for_mime_type(content_type)
    return metadata_extractor(body)

# A request can provide metadata merged from `HeaderMetadata` and
# `DetectedMetadata`

class MergedMetadata(Metadata):
    pass

def merge_metadata(
    header_metadata: HeaderMetadata,
    detected_metadata: DetectedMetadata,
):
    return header_metadata.merge(detected_metadata)

# Using this canned component registration will allow an API Star application to
# extract various kinds of `Metadata` from requests using the request's headers.
# It allows injection of that `Metadata` into a service's arguments.

from apistar import Component

components = [
    Component(HeaderMetadata, init = header_metadata, preload = False),
    Component(DetectedMetadata, init = detect_metadata, preload = False),
    Component(MergedMetadata, init = merge_metadata, preload = False),
]
