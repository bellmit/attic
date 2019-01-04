<html>
<head>
    <title>${self.page_title()}</title>
    <link rel="stylesheet" type="text/css" href="styles/redpaste.css">
    <link rel="stylesheet" type="text/css" href="styles/syntax/default">
</head>
<body>
    <h1>${self.page_title()}</h1>

    ${list_latest(latest)}
    ${next.body()}
    ${new()}
    ${footer()}
</body>
</html>

<%def name="page_title()">
    Redpaste
</%def>

<%def name="list_latest(pastes)">
<div id="latest">
    <h2>Recent Pastes</h2>
    <ul>
        % for paste in pastes:
            <li><a href="${url_for('paste', id=paste.id)}">${paste.author |h}</a><br>
                ${format_datetime(paste.timestamp) |h}</li>
        % endfor
    </ul>
</div>
</%def>

<%def name="new(author='Anonymous Coward')">
<div id="new">
    <form action="." method="post">
        <fieldset>
            <legend>New Paste</legend>
            <table>
                <tr>
                    <th><label for="author">Your Name</label></th>
                    <td><input id="author" name="author" value='${author |h}'></td>
                </tr>
                <tr>
                    <th><label for="syntax">Language</label></th>
                    <td>
                        <select id="syntax" name="syntax">
                            <option select="selected" value="text">Plain Text</option>
                            <option value="python">Python 2.x</option>
                            <option value="python3">Python 3.x</option>
                            <option value="perl">Perl</option>
                            <option value="ruby">Ruby</option>
                            <option value="c">C</option>
                            <option value="cpp">C++</option>
                            <option value="java">Java</option>
                            <option value="objective-c">Objective-C</option>
                            <option value="scala">Scala</option>
                            <option value="csharp">C#</option>
                            <option value="common-lisp">Common Lisp</option>
                            <option value="erlang">Erlang</option>
                            <option value="haskell">Haskell</option>
                            <option value="ocaml">OCaml</option>
                            <option value="brainfuck">BrainFuck</option>
                            <option value="sql">SQL</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <textarea cols="80" rows="15" name="body"></textarea>
                    </td>
                </tr>
            </table>
            <input type="submit" value="Paste it!">
        </fieldset>
    </form>
</div>
</%def>

<%def name="footer()">
    <address>Redpaste is powered by
        <a href="http://pythonpaste.org/">Paster</a>,
        <a href="http://werkzeug.pocoo.org/">Werkzeug</a>,
        <a href="http://www.sqlalchemy.org/">SQLAlchemy</a>,
        <a href="http://www.makotemplates.org/">Mako</a>,
        <a href="http://pygments.org/">Pygments</a>,
        <a href="http://code.google.com/p/python-redfox/">Redfox</a>,
        and, of course, <a href="http://www.python.org">Python</a>.
        Use Redpaste for <strong>awesome</strong>.</address>
</%def>

<%def name="format_datetime(datetime)">
    ${datetime.strftime("%b %d %I:%M:%S %p")}
</%def>
