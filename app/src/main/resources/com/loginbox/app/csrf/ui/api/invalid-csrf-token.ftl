<#ftl strip_whitespace=true>
<#import '/ftl/ui.ftl' as ui>
<#-- @ftlvariable name="" type="com.loginbox.app.csrf.ui.api.InvalidCsrfTokenResponse" -->
<#escape name as name?html>
<@ui.page title="Request blocked by cross-site forgery detection">
    <div class="container">
        <h1>Request blocked by cross-site forgery detection</h1>

        <p>You appear to have been directed to this service by a malicious third party attempting to forge requests.</p>

        <p>Or, perhaps, you submitted the same request twice.</p>

        <p>Either way, this request cannot proceed safely and has been aborted. Use your browser's "back" button to return to what you were doing.</p>
    </div>
</@ui.page>
</#escape>
