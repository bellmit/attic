<#ftl strip_whitespace=true>
<#import '/ftl/ui.ftl' as ui>
<#-- @ftlvariable name="" type="com.loginbox.app.landing.api.LandingPage" -->
<#escape name as name?html>
<@ui.page title="Login Box">
    <div class="container">
        <h1>Login Box</h1>
        <p>If you are seeing this page, <em>congratulations</em>! You have completed first-time setup. Your setup will
            be preserved in future releases.</p>
        <p>This page is a temporary placeholder during development. In the completed system, this page will be replaced:</p>
        <ul>
            <li>with a redirect to a login page, if you are human and not logged in</li>
            <li>with a redirect to your profile, if you are human and logged in</li>
            <li>with API info about this Login Box instance, if you are a machine and logged in</li>
            <li>with a 401 Not Authorized, if you are a machine and not logged in</li>
        </ul>
        <p>... Well, probably.</p>
    </div>
</@ui.page>
</#escape>
