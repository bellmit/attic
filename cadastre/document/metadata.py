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
# The interesting metadata fields are `message_id` (a freeform string), which
# uniquely identifies the original, `date` (a UTC-ified DateTime object), which
# dates its publication, and `subject` (a string), which summarizes the
# document.

import collections as c
import datetime as dt
import email

# Take an arbitrary datetime and ensure that it's in UTC. We do this instead of
# relying on the server timestamp for simple predictability.
def utc_normalize(datetime):
    return datetime.astimezone(dt.timezone.utc)

# Convert non-None values using a simple function, while leaving None unchanged.
def convert_nonnull(value, converter):
    if value is None:
        return value
    return converter(value)

# Remove leading and trailing angle brackets from message IDs, if present.
#
# For reasons known only to the authors of RFC 822, the Message-ID header
# generally has `<this sort of@format>`, where the actual message identifier is
# taken to be the part without the angle brackets. This function checks whether
# those characters are present and balanced, and if so, removes them.
def bare_message_id(message_id):
    if message_id.startswith('<') and message_id.endswith('>'):
        return message_id[1:-1]
    return message_id

class Metadata(c.namedtuple('Metadata', ['message_id', 'date', 'subject'])):
    # Automatically do some widespread varieties of cleanup. The message ID is
    # stripped of angle brackets, and the incoming datetime in `date` to UTC.
    # Doing it here means we only have to write the code once.
    #
    # The ugly `__new__` dance is a consequence of the choice to subclass a
    # namedtuple type.
    def __new__(cls, message_id, date, subject):
        return super().__new__(
            cls,
            message_id = convert_nonnull(message_id, bare_message_id),
            date = convert_nonnull(date, utc_normalize),
            subject = subject,
        )

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
            subject=coalesce(self.subject, onto.subject),
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
        date=extract_message_date(message),
        subject=message['Subject'],
    )

# Parses a string to a datetime object, following the interface of
# email.utils.parsedate_to_datetime. However, if the input contains no date,
# this returns None rather than vomiting up a type error.

import email

def parsedate_to_datetime(str):
    try:
        return email.utils.parsedate_to_datetime(str)
    except TypeError:
        return None

# Given a single Received: header value, attempt to extract the date part. The
# RFC grammar for this header is
#
# received        =       "Received:" name-val-list ";" date-time CRLF
#
# However, this parser permits whitespace after the final semicolon, for
# compatibility with the huge range of mail systems that generate such headers.

import re

def extract_received_date(header):
    parts = re.split(';\s+', header)
    candidate_date = parts[-1]
    return parsedate_to_datetime(candidate_date)

# Try to infer the date of an email message. For messages with a valid RFC2822
# Date: header, this is easy, use that header. Otherwise, run down the Received:
# headers and pick the one with the most recent date. Failing that, give up and
# return None.
#
# This algorithm exists to cope with garbage Date headers, messages without a
# Date, and other anomalies that definitely exist in the wild. Using the
# received headers is a hack, but a reasonable one.

def extract_message_date(message):
    if message['Date']:
        rfc_date = parsedate_to_datetime(message['Date'])
        if rfc_date is not None:
            return rfc_date

    received = message.get_all('Received')
    if received is not None:
        received.sort(key=extract_received_date)
        return extract_received_date(received[-1])

    return None

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
import email

class HeaderMetadata(Metadata):
    pass

def header_metadata(
    message_id: http.Header = None,
    date: http.Header = None,
    subject: http.Header = None
):
    return Metadata(
        message_id=message_id,
        date=convert_nonnull(date, email.utils.parsedate_to_datetime),
        subject=subject,
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
