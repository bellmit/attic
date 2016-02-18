# Welcome to Redfox

Redfox provides a simple, declarative routing mechanism for creating WSGI
entry points into applications. It's broadly similar to microframeworks like
[juno](https://github.com/breily/juno) or [CherryPy](http://www.cherrypy.org).

# Features

1. It's tiny. The redfox package contains under 100 lines of code. Redfox
    builds heavily on the [Werkzeug](http://werkzeug.pocoo.org) tools to implement
    its features, rather than re-reinventing the wheel.
2. It's clean. The following is a valid Redfox application:

        from werkzeug import Response
        from redfox import WebApplication
        from redfox import get, post

        class Example(WebApplication):
            @get('/')
            def index(self, request):
                return Response("Hello, world!")
        
        if __name__ == '__main__':
            from redfox import server
            server.run(Example())

3. It's minimal. Redfox does not impose an ORM model, or a templating system.
Bring your own, or create your own tools.

See the [Getting Started
guide](https://github.com/ojacobson/redfox/wiki/GettingStarted) for a
step-by-step guide for creating a complete Redfox application.

# Spite-Driven Development

I'm resuming work on this project basically out of spite: I had a discussion
on Twitter with the delightful and very smart @mitsuhiko about
[Flask](http://flask.pocoo.org/)'s use of global state and he [said
something](http://twitter.com/mitsuhiko/status/268018102309097472) that deeply
bothered me:

> I never used context globals before Flask because I thought I could
> avoid them.  I was wrong and that took me 5 years to realize.

There's not much room in 140 characters for complete and well-reasoned
arguments, so for completeness' sake I'll refer you to Flask's rationale for
"context local" global symbols at
http://flask.pocoo.org/docs/design/#thread-locals:

> Flask uses thread local objects (context local objects in fact, they
> support greenlet contexts as well) for request, session and an extra
> object you can put your own things on (`g`). Why is that and isn’t
> that a bad idea?
>
> Yes it is usually not such a bright idea to use thread locals. They
> cause troubles for servers that are not based on the concept of
> threads and make large applications harder to maintain. However
> Flask is just not designed for large applications or asynchronous
> servers. Flask wants to make it quick and easy to write a traditional
> web application.

Armin's a smart guy, and Flask is some seriously good shit. In fact, you
should probably use it: his design decisions bother me, but the resulting API
is quite pleasant to write for.

But I think he's wrong on this one, so in the spirit of the open-source
do-ocracy I'm going to make my case by example rather than by prose.

(@mitsuhiko: if you feel I'm misrepresenting your argument, by all means: get
in touch! I'd love more input from you on this stuff; you've spent five years
I haven't thinking about it.)

Web frameworks exist to make writing applications that interact over HTTP
easier, and HTTP is a fundamentally stateless protocol. In a "classic"
synchronous web framework, there's a clear and obvious relationship between
the HTTP request/response cycle and a function call and return value.
Additional context for the request can be passed in like any other function
parameter, and additional data for the response can be returned to the
framework like any other return value.

Places where it's "convenient" to use global state or global proxies can be
implemented with less indirection using more normal Python features. For
example, rather than having a global "request" proxy, application endpoints
can receive the current request as a parameter at the start of request
handling. Rather than having a global "app" object, applications can be
constructed as Python objects and use `self` to refer to themselves.

## What Sucks

Of course, I wouldn't be concerned by any of that if Flask didn't have a lot
of really good ideas in it. There are a few things Redfox does badly, or that
it doesn't do at all, that I'd like to fix. My "2.0" wishlist:

* Modularization and composition hurts real bad. The "best" way to break a
  complex app into multiple objects in Redfox right now is to define several
  application classes, then derive the "real" application from them using
  multiple inheritance:

        class Foo(WebApplication):
            @get('/')
            def index(self, request):
                return Response("index")
        
        class Bar(WebApplication):
            @get('/styles/<path>')
            def styles(self, request, path):
                return Response(path)
        
        class Application(Foo, Bar):
            pass
    
    This is ugly.
    
    It only gets worse when you realize that endpoint names have to be unique
    across all of the composed application classes. The endpoint naming in
    Flask blueprints is pretty elegant, where endpoint names are optionally
    scoped to a blueprint, and there is a syntax for "the endpoint named foo
    in the current component".

* As the Getting Started guide shows, you have to write a lot of glue to get
  off the ground with Redfox in the usual modern development environment. Some
  ready-to-use modules and best practices for templating and response creation
  might help with that. Similarly, working directly with Werkzeug request and
  response objects feels weirdly low-level in Redfox.

* No development server. While this isn't a showstopper (and I don't really
  like "dev server" modes; people tend to use them in inappropriate ways), it
  does make the "getting started" app more complex. While I used Paster for
  the example app, it'd be nice if there was an easy way to make a Redfox
  application self-hosting for simple/demonstration cases.

* Like it or not, modern web applications need session management. There are
  lots of session frameworks out there; I should demonstrate how to bolt them
  into Redfox. Same goes for login support.

* Somehow, avoid proliferating magic symbols in `WebApplication` classes or
  placing too many demands on construction. The simplicity there is a virtue.
