<#ftl strip_whitespace=true>
<#import '/ftl/ui.ftl' as ui>
<#-- @ftlvariable name="" type="com.loginbox.app.setup.api.SetupForm" -->
<#escape name as name?html>
<@ui.html>
    <@ui.head title="Set Up Login Box">
        <@ui.stylesheet href="assets/css/cover.css" />
    </@ui.head>
    <body>
        <div class="site-wrapper">
            <div class="site-wrapper-inner">
                <div class="cover-container">
                    <div class="inner cover welcome-message">
                        <h1 class="cover-heading">Welcome to Login Box.</h1>
                        <p class="lead">Before you can use Login Box, you'll need to create a user for
                            yourself. This user will be able to administer this Login Box service.</p>
                        <div class="row">
                            <form class="col-md-6 col-md-offset-3" method="post">
                                <div class="form-group">
                                    <label for="username">Username</label>
                                    <input type="text"
                                           class="form-control"
                                           id="username"
                                           name="username"
                                           placeholder="Enter username">
                                </div>
                                <div class="form-group">
                                    <label for="contactEmail">Email address</label>
                                    <input type="email"
                                           class="form-control"
                                           id="contactEmail"
                                           name="contactEmail"
                                           placeholder="Enter email">
                                </div>
                                <div class="form-group">
                                    <label for="password">Password</label>
                                    <input type="password"
                                           class="form-control"
                                           id="password"
                                           name="password"
                                           placeholder="Password">
                                </div>
                                <div class="form-group">
                                    <label for="confirmPassword">Confirm Password</label>
                                    <input type="password"
                                           class="form-control"
                                           id="confirmPassword"
                                           name="confirmPassword"
                                           placeholder="Password">
                                </div>
                                <input type="hidden" name="csrfToken" value="${csrfToken.secret}">
                                <button type="submit" class="btn btn-lg btn-default">Set It Up</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</@ui.html>
</#escape>
