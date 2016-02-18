"""Development server support for redfox apps.

WSGI applications in production should be served by a production-ready
container such as gunicorn or mod_wsgi, but for development it's often
convenient to be able to serve an app directly, without additional
dependencies. This module wraps Werkzeug's ``run_simple`` function.

Generally, apps that support a "development server" mode look something like

    from redfox import WebApplication, get
    from werkzeug import Response
    
    class MyApp(WebApplication):
        @get('/')
        def hello(request):
            return Response("Hello, world!")
    
    if __name__ == '__main__':
        from redfox import server
        server.run(MyApp())

"""

from werkzeug import serving as ws

def run(
    application,
    hostname='127.0.0.1',
    port=9000,
    debug=False,
    **options
):
    """Serve ``application`` until interrupted. This is a wrapper around
    ``werkzeug.serving.run_simple`` that provides some default values:
    
        * ``hostname`` defaults to ``'127.0.0.1'``.
        * ``port`` defaults to ``9000``.
    
    Other parameters have their defaults as per Werkzeug's documentation, and
    can be overridden by passing the corresponding keyword parameters to this
    function.
    
    As a shortcut, you can pass ``debug=True`` to enable both the reloader and
    the debugger feature. This parameter will not be passed through to
    Werkzeug.
    """
    options.setdefault('use_reloader', debug)
    options.setdefault('use_debugger', debug)
    ws.run_simple(
        hostname,
        port,
        application,
        **options
    )
