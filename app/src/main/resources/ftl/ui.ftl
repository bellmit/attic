<#ftl strip_text=true strip_whitespace=true>
<#-- @ftlvariable name="" type="com.loginbox.app.views.ViewConvention" -->

<#macro html>
    <#escape name as name?html>
<!DOCTYPE html>
<html>
    <#nested>
</html>
    </#escape>
</#macro>

<#macro stylesheet href>
    <#escape name as name?html>
        <link href="${href}" rel="stylesheet">
    </#escape>
</#macro>

<#macro head title="Login Box">
    <#escape name as name?html>
<head>
    <title>${title}</title>
    <base href="${links.base}">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <@stylesheet href="${links.base}assets/css/site.css" />
    <#nested>
</head>
    </#escape>
</#macro>

<#macro page title="Login Box">
    <#escape name as name?html>
<@html>
    <@head title=title />
    <body>
        <#nested>
    </body>
</@html>
    </#escape>
</#macro>
