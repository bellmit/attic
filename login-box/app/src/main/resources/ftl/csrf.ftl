<#ftl strip_text=true strip_whitespace=true>
<#-- @ftlvariable name="" type="com.loginbox.app.views.FormConvention" -->

<#macro token>
    <#escape name as name?html>
        <#if csrfToken??>
            <input type="hidden" name="csrfToken" value="${csrfToken.secret}">
        </#if>
    </#escape>
</#macro>
