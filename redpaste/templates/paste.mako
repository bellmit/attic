<%inherit file="layout.mako"/>
<%namespace name="syntax" module="redpaste.highlight"/>
<div id="paste">
    <h2>Pasted at ${self.format_datetime(paste.timestamp) |h} 
        by ${paste.author |h}</h2>
    ${syntax.highlight(paste.body, paste.syntax)}
</div>

<%def name="page_title()">
    Redpaste - ${paste.id |h} (${paste.syntax |h})
</%def>
