<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="title"
    rtexprvalue="true"
    type="java.lang.String"
    required="true"
    description="The page title."%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${title}</title>
    <c:url var="cssUrl" value="/css/site.css" />
    <link href="${cssUrl}" rel="stylesheet">
  </head>

  <body>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="#">Project name</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active"><a href="#">Home</a></li>
              <li><a href="#about">About</a></li>
              <li><a href="#contact">Contact</a></li>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <div class="container">

    <jsp:doBody />

    </div>

    <c:url var="scriptUrl" value="/js/require.js" />
    <script data-main="js/site" src="${scriptUrl}"></script>
  </body>
</html>
